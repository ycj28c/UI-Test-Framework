package io.ycj28c.uitest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Initializes a REST client.
 */
@Configuration
public class RestClientConfig {

	/**
	 * Create a new REST template for us to issue web calls.
	 *
	 * @return The REST template.
	 */
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
