package com.infra4u.billing_manager_service.config.multi_tenant;

import com.infra4u.billing_manager_service.exception.UnprocessableEntityException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class TenantPropertySource {

    private final ResourcePatternResolver resourcePatternResolver;

    private final Environment environment;

    public Map<String, Properties> loadTenantProperties() {

        Map<String, Properties> tenantPropertiesMap = new HashMap<>();
        try {
            // Carregar o arquivo de propriedades centralizado
            Resource resource = resourcePatternResolver.getResource("classpath:tenants.properties");
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            // Iterar sobre as propriedades e organizá-las por tenant
            properties.forEach((key, value) -> {
                String[] parts = key.toString().split("\\.");
                if (parts.length == 3) {
                    String tenantId = parts[0];
                    String propertyKey = parts[1] + "." + parts[2];
                    String propertyValue = value.toString();
                    // Verificar se o valor é uma variável de ambiente com valor padrão
                    if (propertyValue.startsWith("${") && propertyValue.endsWith("}")) {
                        String envVar = propertyValue.substring(2, propertyValue.length() - 1);
                        String[] envParts = envVar.split(":");
                        String envKey = envParts[0];
                        String defaultValue = envParts.length > 1 ? envParts[1] : null;
                        // Tentar obter o valor da variável de ambiente
                        propertyValue = environment.getProperty(envKey, Objects.isNull(defaultValue) ? "-empty default value-" : defaultValue);
                    }
                    tenantPropertiesMap.computeIfAbsent(tenantId, k -> new Properties()).setProperty(propertyKey, propertyValue);
                }
            });
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        return tenantPropertiesMap;
    }

    /**
     * get tenant name by {@code clientId}
     *
     * @param clientId client id
     * @return - tenant name
     */
    public String getTenantByClientId(String clientId) {
        Map<String, Properties> tenantPropertiesMap = loadTenantProperties();

        for (Map.Entry<String, Properties> entry : tenantPropertiesMap.entrySet()) {
            Properties properties = entry.getValue();
            String tenantId = entry.getKey();

            String CLIENT_ID_KEY = "http.client-id";
            String configuredClientId = properties.getProperty(CLIENT_ID_KEY);
            if (clientId.equals(configuredClientId)) {
                return tenantId;
            }
        }
        throw new UnprocessableEntityException("Verifique a requisição e tente novamente");
    }
}
