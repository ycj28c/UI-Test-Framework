package io.ycj28c.uitest.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import io.ycj28c.uitest.config.DatabaseConfig;
import io.ycj28c.uitest.config.RestClientConfig;

@ContextConfiguration(classes = {
	RestClientConfig.class,
	DatabaseConfig.class
})
public class AbstractRestfulTest extends AbstractTest{

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	/* can use Bean to autowired the datasource, but not easy to manage */
//	@Autowired
//	protected Hashtable<String, JdbcTemplate> jdbcTemplateMaps;
}
