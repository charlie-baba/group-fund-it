/**
 * 
 */
package com.gfi.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Obi
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gfi.web.dao")
@ComponentScan({"com.gfi.config", "com.gfi.config.security"})
@PropertySource(value = {"classpath:server.properties"})
public class AppConfig {
	
	@Value("classpath:appInitialization.sql")
	private Resource initScript;

	@Autowired
	Environment env;
	
	@Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
	@Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{"com.bis.gfi.entities"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        em.setJpaProperties(hibernateProperties());

        return em;
    }
	
	@Bean(destroyMethod = "close", name = "rawDataSource")
    public DataSource rawDataSource() {
        try {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(env.getRequiredProperty("jdbc.driver"));
            ds.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
            ds.setUser(env.getRequiredProperty("jdbc.username"));
            ds.setPassword(env.getRequiredProperty("jdbc.password"));
            ds.setMaxPoolSize(Integer.parseInt(env.getProperty("c3p0.maxPoolSize")));
            ds.setMinPoolSize(Integer.parseInt(env.getProperty("c3p0.minPoolSize")));
            ds.setAcquireIncrement(Integer.parseInt(env.getProperty("c3p0.acquireIncrement")));
            ds.setAcquireRetryAttempts(Integer.parseInt(env.getProperty("c3p0.acquireRetryAttempts")));
            ds.setAcquireRetryDelay(Integer.parseInt(env.getProperty("c3p0.acquireRetryDelay")));
            ds.setBreakAfterAcquireFailure(false);
            ds.setMaxConnectionAge(Integer.parseInt(env.getProperty("c3p0.maxConnectionAge")));
            ds.setMaxIdleTime(Integer.parseInt(env.getProperty("c3p0.maxIdleTime")));
            ds.setMaxIdleTimeExcessConnections(Integer.parseInt(env.getProperty("c3p0.maxIdleTimeExcessConnections")));
            ds.setIdleConnectionTestPeriod(Integer.parseInt(env.getProperty("c3p0.idleConnectionTestPeriod")));
            ds.setTestConnectionOnCheckout(true);
            ds.setPreferredTestQuery("SELECT 1");
            ds.setDebugUnreturnedConnectionStackTraces(true);
            ds.setAutoCommitOnClose(true);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	
	//if you want to configure connection pool, use this instead
	/*@Bean(name = "dataSource")
    public DataSource dataSource() {
        return rawDataSource();
    }*/
	
	@Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("jdbc.driver"));
        dataSource.setUrl(env.getRequiredProperty("jdbc.url"));
        dataSource.setUsername(env.getRequiredProperty("jdbc.username"));
        dataSource.setPassword(env.getRequiredProperty("jdbc.password"));
        DatabasePopulatorUtils.execute(createDatabasePopulator(), dataSource); //for populating the db from app initialization script
        return dataSource;
    }
	
	private DatabasePopulator createDatabasePopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(true);
        databasePopulator.addScript(initScript);
        return databasePopulator;
    }
	
	@SuppressWarnings("serial")
    Properties hibernateProperties() {
        return new Properties() {
            {
               /* setProperty("hibernate.cache.use_second_level_cache", env.getProperty("hibernate.cache.use_second_level_cache"));
                setProperty("hibernate.cache.use_query_cache", env.getProperty("hibernate.cache.use_query_cache"));
                setProperty("hibernate.cache.region.factory_class", env.getProperty("hibernate.cache.region.factory_class"));
                setProperty("hibernate.cache.use_structured_entries", env.getProperty("hibernate.cache.use_structured_entries"));
                setProperty("hibernate.memcached.operationTimeout", env.getProperty("hibernate.memcached.operationTimeout"));
                setProperty("hibernate.memcached.connectionFactory", env.getProperty("hibernate.memcached.connectionFactory"));
                setProperty("hibernate.memcached.hashAlgorithm", env.getProperty("hibernate.memcached.hashAlgorithm"));
                setProperty("hibernate.memcached.servers", env.getProperty("hibernate.memcached.servers"));*/

                setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
                setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
                setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
                setProperty("hibernate.auto_close_session", env.getProperty("hibernate.auto_close_session"));
                setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
                setProperty("hibernate.default_schema", env.getProperty("hibernate.default_schema"));
                setProperty("org.hibernate.envers.do_not_audit_optimistic_locking_field", env.getProperty("org.hibernate.envers.do_not_audit_optimistic_locking_field"));
                setProperty("org.hibernate.envers.store_data_at_delete", env.getProperty("org.hibernate.envers.store_data_at_delete"));
                setProperty("org.hibernate.envers.audit_table_prefix", env.getProperty("org.hibernate.envers.audit_table_prefix"));
                setProperty("org.hibernate.envers.audit_table_suffix", env.getProperty("org.hibernate.envers.audit_table_suffix"));
            }
        };
    }
	
	@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean(name = "mailSender")
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(env.getRequiredProperty("smtp.host"));
        javaMailSender.setPort(Integer.parseInt(env.getRequiredProperty("smtp.port")));
        javaMailSender.setUsername(env.getRequiredProperty("mail.username"));
        javaMailSender.setPassword(env.getRequiredProperty("mail.password"));
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        javaMailSender.setJavaMailProperties(props);
        return javaMailSender;
    }

    @Bean(name = "freeMarkerConfigurationFactoryBean")
    public FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean() {
        FreeMarkerConfigurationFactoryBean fm = new FreeMarkerConfigurationFactoryBean();
        fm.setPreferFileSystemAccess(false);
        fm.setDefaultEncoding("UTF-8");
        fm.setTemplateLoaderPath("classpath:mailtemplates/");
        return fm;
    }
    
}
