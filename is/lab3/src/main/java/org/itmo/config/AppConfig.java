package org.itmo.config;

import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.itmo")
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    @Value("${dbcp2.max-total:20}")
    private int maxTotal;

    @Value("${dbcp2.max-idle:10}")
    private int maxIdle;

    @Value("${dbcp2.min-idle:5}")
    private int minIdle;

    @Value("${dbcp2.max-wait-millis:30000}")
    private long maxWaitMillis;

    @Value("${dbcp2.test-on-borrow:true}")
    private boolean testOnBorrow;

    @Value("${dbcp2.test-while-idle:true}")
    private boolean testWhileIdle;

    @Value("${dbcp2.validation-query:SELECT 1}")
    private String validationQuery;

    @Value("${dbcp2.time-between-eviction-runs-millis:60000}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${dbcp2.min-evictable-idle-time-millis:300000}")
    private long minEvictableIdleTimeMillis;

    @Value("${dbcp2.remove-abandoned-on-borrow:true}")
    private boolean removeAbandonedOnBorrow;

    @Value("${dbcp2.remove-abandoned-timeout:300}")
    private int removeAbandonedTimeout;

    @Value("${dbcp2.log-abandoned:true}")
    private boolean logAbandoned;

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(dbUrl);
        ds.setUsername(dbUsername);
        ds.setPassword(dbPassword);
        ds.setDriverClassName(driverClassName);
        ds.setMaxTotal(maxTotal);
        ds.setMaxIdle(maxIdle);
        ds.setMinIdle(minIdle);
        ds.setMaxWaitMillis(maxWaitMillis);
        ds.setTestOnBorrow(testOnBorrow);
        ds.setTestWhileIdle(testWhileIdle);
        ds.setValidationQuery(validationQuery);
        ds.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        ds.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        ds.setRemoveAbandonedOnBorrow(removeAbandonedOnBorrow);
        ds.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        ds.setLogAbandoned(logAbandoned);

        logger.info("✓ Apache Commons DBCP2 | maxTotal={} maxIdle={} minIdle={}", maxTotal, maxIdle, minIdle);
        return ds;
    }

}
