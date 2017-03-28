package demo.restful.test;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.Assert;
import org.testng.annotations.Test;

import demo.restful.auth.NullHostNameVerifier;
import demo.restful.auth.NullX509TrustManager;

/*
 * Rest API test example
 */
public class DemoRestAPITest extends AbstractRestfulTest {

	/**
	 * a common rest test, test google url
	 */
	@Test
	public void testGetURL() {
		log.info("\n\n **BEGIN testGetURL**");

		final String uri = "https://www.google.com";
		String result = restTemplate.getForObject(uri, String.class);

		Assert.assertTrue(result.contains("google"));
	}

	/**
	 * Tomcat health check with the https authorization
	 */
	@Test(groups = { "ui-smoketest", "ui-functionaltest" })
	public void testTomcatActiveCheck() {
		log.info("\n\n **BEGIN testTomcatActiveCheck**");

		disableTrustCheck();

		String serverIP = env.getRequiredProperty("TOMCAT-LVS-IP");
		String url = "https://" + serverIP + "/active";
		log.info("URL:\n{}", url);

		ResponseEntity<String> entity = restTemplate.getForEntity(url,
				String.class);
		HttpStatus statusCode = entity.getStatusCode();
		log.info("Status:\n{}\nBody:\n{}", statusCode, entity.getBody()
				.toString());

		Assert.assertEquals(statusCode, HttpStatus.OK,
				"the statusCode is not 200, connection fail");
	}

	/**
	 * Disable trust checks for SSL connections.
	 */
	private void disableTrustCheck() {
		try {
			SSLContext sslc;

			sslc = SSLContext.getInstance("TLS");

			TrustManager[] trustManagerArray = { new NullX509TrustManager() };
			sslc.init(null, trustManagerArray, null);

			HttpsURLConnection.setDefaultSSLSocketFactory(sslc
					.getSocketFactory());
			HttpsURLConnection
					.setDefaultHostnameVerifier(new NullHostNameVerifier());
		} catch (Exception e) {
			log.error(e.toString());
		}
	}
}
