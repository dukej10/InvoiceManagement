package co.com.management.jpa.config.postgresql;

import co.com.management.jpa.config.DBSecret;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(
        basePackages = "co.com.management.jpa.persistence.client", // repositorios postgres
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"
)public class PostgreSQLJPA {

    @Primary
    @Bean(name = "sqlDBSecret")
    public DBSecret dbSecret(Environment env) {
        return DBSecret.builder()
                .url(env.getProperty("spring.datasource.postgresql.url"))
                .username(env.getProperty("spring.datasource.postgresql.username"))
                .password(env.getProperty("spring.datasource.postgresql.password"))
                .build();
    }

    @Bean
    @Primary
    public DataSource datasource(DBSecret secret,
                                 @Value("${spring.datasource.postgresql.driverClassName}") String driverClass) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(secret.getUrl());
        config.setUsername(secret.getUsername());
        config.setPassword(secret.getPassword());
        config.setDriverClassName(driverClass);
        return new HikariDataSource(config);
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
            DataSource dataSource,
            @Value("${spring.jpa.postgresql.databasePlatform}") String dialect) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("co.com.management.jpa");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // TODO: remove this for non auto create schema
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager postgresTransactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
