package com.infra4u.billing_manager_service.config.multi_tenant;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultitenantConfiguration {

    private final TenantPropertySource tenantPropertySource;

    @Value("${default.tenant}")
    private String defaultTenant;

    /**
     * Creates a HikariDataSource with connection pool configuration for a specific
     * tenant
     * 
     * @param tenantId   The tenant identifier
     * @param properties The properties containing datasource and pool configuration
     * @return Configured HikariDataSource with connection pool
     */
    private HikariDataSource createHikariDataSource(String tenantId, Properties properties) {
        HikariConfig hikariConfig = new HikariConfig();

        // Basic datasource configuration
        String driverClassName = properties.getProperty("datasource.driver-class-name", "org.postgresql.Driver");
        hikariConfig.setDriverClassName(driverClassName);
        String url = properties.getProperty("datasource.url");
        if (url == null || url.isEmpty() || "url".equals(url)) {
            log.error("Tenant '{}' has invalid URL: {}", tenantId, url);
        }
        log.info("Configuring DataSource for tenant '{}' with URL: {}", tenantId, url);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(properties.getProperty("datasource.username"));
        hikariConfig.setPassword(properties.getProperty("datasource.password"));

        // Pool configuration with defaults
        hikariConfig.setMaximumPoolSize(
                Integer.parseInt(properties.getProperty("datasource.hikari.maximum-pool-size", "10")));
        hikariConfig.setMinimumIdle(
                Integer.parseInt(properties.getProperty("datasource.hikari.minimum-idle", "5")));
        hikariConfig.setConnectionTimeout(
                Long.parseLong(properties.getProperty("datasource.hikari.connection-timeout", "30000")));
        hikariConfig.setIdleTimeout(
                Long.parseLong(properties.getProperty("datasource.hikari.idle-timeout", "600000")));
        hikariConfig.setMaxLifetime(
                Long.parseLong(properties.getProperty("datasource.hikari.max-lifetime", "1800000")));
        hikariConfig.setPoolName(
                properties.getProperty("datasource.hikari.pool-name", tenantId + "-HikariPool"));

        // Additional recommended settings
        hikariConfig.setAutoCommit(true);
        hikariConfig.setConnectionTestQuery("SELECT 1");

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        log.info("Created HikariCP pool for tenant '{}' - Pool: {}, Max: {}, Min: {}",
                tenantId,
                dataSource.getPoolName(),
                dataSource.getMaximumPoolSize(),
                dataSource.getMinimumIdle());

        return dataSource;
    }

    /**
     * Configures multi-tenant datasource with HikariCP connection pools
     * 
     * @return AbstractRoutingDataSource configured with HikariCP pools for each
     *         tenant
     * @throws IOException if tenant properties cannot be loaded
     * @see <a href=
     *      "https://www.baeldung.com/multitenancy-with-spring-data-jpa">...</a>
     */
    @Bean
    public DataSource dataSource() throws IOException {
        Map<String, Properties> tenantProperties = tenantPropertySource.loadTenantProperties();
        Map<Object, Object> dataSources = new HashMap<>();

        log.info("Initializing multi-tenant datasources with HikariCP for {} tenants", tenantProperties.size());

        for (Map.Entry<String, Properties> entry : tenantProperties.entrySet()) {
            String tenantId = entry.getKey();
            Properties properties = entry.getValue();
            log.info("Creating data source for tenant: {}", tenantId);
            HikariDataSource dataSource = createHikariDataSource(tenantId, properties);
            dataSources.put(tenantId, dataSource);
        }

        AbstractRoutingDataSource dataSource = new MultitenantDataSource();
        dataSource.setDefaultTargetDataSource(dataSources.get(defaultTenant));
        dataSource.setTargetDataSources(dataSources);

        dataSource.afterPropertiesSet();

        log.info("Multi-tenant datasource initialized successfully with default tenant: {}", defaultTenant);

        return dataSource;
    }
}