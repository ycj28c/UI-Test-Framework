package demo.ui.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

import io.ycj28c.uitest.config.DatabaseConfig;
import io.ycj28c.uitest.config.RestClientConfig;
import io.ycj28c.uitest.config.SeleniumConfig;
import io.ycj28c.uitest.config.TestConfig;
import io.ycj28c.uitest.framework.AbstractSeleniumTest;

@ContextConfiguration(classes = { TestConfig.class, SeleniumConfig.class,
		DatabaseConfig.class, RestClientConfig.class })
/*
 * Demo selenium Test super class
 */
public class AbstractUITest extends AbstractSeleniumTest {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	protected RestTemplate restTemplate;

	@BeforeClass(alwaysRun = true)
	protected void setupDriver() {
		driver.manage().window().maximize();
	}

	@AfterMethod(alwaysRun = true)
	protected void CheckBrowser() {
		keepSingleWindow();
		driver.manage().window().maximize();
	}

	/**
	 * only keep one windows to run the test
	 */
	private void keepSingleWindow() {
		log.info("How many windows left:{}", driver.getWindowHandles().size());
		Object[] handles = driver.getWindowHandles().toArray();
		if (handles == null || handles.length < 1) {
			throw new IllegalArgumentException(
					"No window was open currently, this should not happen in product");
		}
		if (handles.length == 1) { // if only has one windows, continue
			return;
		}
		String firstWindow = handles[0].toString(); // first open window
		for (int i = 0; i < handles.length; i++) {
			if (!(handles[i].toString().equalsIgnoreCase(firstWindow))) {
				driver.switchTo().window(handles[i].toString());
				driver.close();
			}
			driver.switchTo().window(firstWindow); // move to the first open
													// window;
		}
		log.info("How many windows left:{}", driver.getWindowHandles().size());
	}

}
