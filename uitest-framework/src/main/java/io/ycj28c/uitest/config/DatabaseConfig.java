package io.ycj28c.uitest.config;

import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariConfig;

import io.ycj28c.uitest.util.DataSourceUtil;

/**
 * Selenium requires us to have some system properties set before it'll function right.
 * This class will take a list of properties in the Spring environment and push them into
 * Java system properties.
 */
@Configuration
//@PropertySource(value = "classpath:db-${env}.properties") //will use the same property
public class DatabaseConfig {

	private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);
	
	@Autowired
	ConfigurableEnvironment env;
	
	/**
	 * Fetch a JDBC template for this database.
	 *
	 * @return the JDBC template to set the data.
	 * @throws NameNotFoundException 
	 */
	@Bean
	@DependsOn("initialdatsource")
	JdbcTemplate getJdbcTemplate() throws NameNotFoundException {
		return new JdbcTemplate(DataSourceUtil.lookupActiveDS());
	} 
	
	/* can use Bean to autowired the datasource, but not easy to manage */
//	@Bean
//	@DependsOn("initialdatsource")
//	Hashtable<String, JdbcTemplate> getJdbcTemplateMaps() {
//		Hashtable<String, JdbcTemplate> map = new Hashtable<String, JdbcTemplate>();
//		for(Entry<String, DataSource> item:DataSourceUtil.connectionPools.entrySet()){
//			JdbcTemplate jdbcTemp = new JdbcTemplate(item.getValue());
//			map.put(item.getKey(), jdbcTemp);
//		}
//		return map;
//	} 

	@Bean(name = "initialdatsource")
	Hashtable<String, DataSource> getDataSourceMaps()
			throws NameAlreadyBoundException, ClassNotFoundException,
			IllegalAccessException, InstantiationException, SQLException,
			NameNotFoundException {
		if(!DataSourceUtil.toHash().isEmpty()){
			return DataSourceUtil.toHash();
		}
		String product = env.getProperty("product").trim();
		if (product == null || product.trim().equals("")) {
			throwDataBaseConfigException();
		}
		String[] products = product.replaceAll("\\s", "").split(",");
		String activeDS  = products[0];
		if (activeDS == null || activeDS.trim().equals("")) {
			throwDataBaseConfigException();
		}
		
		for(String item: products){
			HikariConfig config = new HikariConfig();
			config.setMaximumPoolSize(( env.getProperty(item + ".maxPoolSize") == null ) ? 15 : env.getProperty(item + ".maxPoolSize", Integer.class));
			config.setDriverClassName(env.getRequiredProperty(item + ".driverClassName"));
			config.setJdbcUrl(env.getRequiredProperty(item + ".url"));
			config.setMaxLifetime(( env.getProperty(item + ".maxLifetime") == null ) ? 60000/*1min*/: env.getProperty(item + ".maxLifetime", Integer.class)); 
			config.addDataSourceProperty("user", env.getRequiredProperty(item + ".username"));
			config.addDataSourceProperty("password", env.getRequiredProperty(item + ".password"));
			log.info("Creating new DataSource with config={}", config);
			
			DataSourceUtil.bind(item, config);
		}
		
		DataSourceUtil.active(activeDS);
		log.debug(DataSourceUtil.getConnectionPoolsInfo());
		return DataSourceUtil.toHash();
	}
	
	private void throwDataBaseConfigException (){
		throw new IllegalArgumentException("\n\n Properties file does not specify 'product'!"
				+ "\n Please add DB connection to your properties such as:"
				+ "\n\n ########### Database ###########"
				+ "\n product=tiger,rabbit"
				+ "\n "
				+ "\n tiger.driverClassName=oracle.jdbc.driver.OracleDriver"
				+ "\n tiger.url=jdbc:oracle:thin:@10.10.10.10:1521:demo"
				+ "\n tiger.username=demo"
				+ "\n tiger.password=demo"
				+ "\n "
				+ "\n rabbit.driverClassName=oracle.jdbc.driver.OracleDriver"
				+ "\n rabbit.url=jdbc:oracle:thin:@10.10.10.10:1521:demo"
				+ "\n rabbit.username=demo"
				+ "\n rabbit.password=demo"
				+ "\n ########################################\n");
	}
}
