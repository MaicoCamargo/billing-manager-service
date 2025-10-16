package com.infra4u.billing_manager_service.config.multi_tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class MultitenantConfiguration {

    private final TenantPropertySource tenantPropertySource;

    @Value("${default.tenant}")
    private String defaultTenant;

    /**
     * @return
     * @throws IOException
     * @see <a href="https://www.baeldung.com/multitenancy-with-spring-data-jpa">...</a>
     */
    @Bean
    public DataSource dataSource() throws IOException {
        Map<String, Properties> tenantProperties = tenantPropertySource.loadTenantProperties();
        Map<Object, Object> dataSources = new HashMap<>();
        for (Map.Entry<String, Properties> entry : tenantProperties.entrySet()) {
            String tenantId = entry.getKey();
            Properties properties = entry.getValue();
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setUrl(properties.getProperty("datasource.url"));
            dataSource.setUsername(properties.getProperty("datasource.username"));
            dataSource.setPassword(properties.getProperty("datasource.password"));
            dataSources.put(tenantId, dataSource);
        }
        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(dataSources.get(defaultTenant));
        dataSource.setTargetDataSources(dataSources);

        dataSource.afterPropertiesSet();
        return dataSource;
    }
}