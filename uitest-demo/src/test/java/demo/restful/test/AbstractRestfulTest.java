package demo.restful.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import io.ycj28c.uitest.config.DatabaseConfig;
import io.ycj28c.uitest.config.RestClientConfig;
import io.ycj28c.uitest.config.TestConfig;
import io.ycj28c.uitest.framework.AbstractTest;

@ContextConfiguration(classes = { TestConfig.class, DatabaseConfig.class, RestClientConfig.class })
/*
 * Restful API Test super class
 */
public class AbstractRestfulTest extends AbstractTest {

	@Autowired
	protected RestTemplate restTemplate;

	@Autowired
	protected JdbcTemplate jdbcTemplate;
}
