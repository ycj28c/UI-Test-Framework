package io.ycj28c.uitest.config;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Selenium requires us to have some system properties set before it'll function right.
 * This class will take a list of properties in the Spring environment and push them into
 * Java system properties.
 */
@Configuration
@PropertySource(value = "classpath:browsers.properties")
public class SeleniumConfig {

	private static final Logger log = LoggerFactory.getLogger(SeleniumConfig.class);
	private static String os = null;

	@Autowired
	ConfigurableEnvironment env;
	
	private static void determineOS() {
		if (os == null) {
			String osProp = System.getProperty("os.name").toLowerCase();
			String osArch = System.getProperty("os.arch");
			
			if (osProp.contains("mac") || osProp.contains("darwin")) {
				log.info("This is mac");
				os = "osx";
			} else if (osProp.contains("linux")) {
				if(osArch.contains("64")){
					log.info("This is 64-bit linux: " + osArch);
					os = "linux64";
				} else {
					log.info("This is 32-bit linux: " + osArch);
					os = "linux32";
				}
			} else if (osProp.contains("win")) {
				if (osArch.contains("amd64")) {
					log.info("This is 64-bit windows: " + osArch);
					os = "windows64";
				} else {
					log.info("This is 32-bit windows: " + osArch);
					os = "windows32";
				}
			} else {
				os = osProp;
			}
		}
	}
	
	/**
	 * Side effects: sets system properties.
	 *
	 * @return The system properties set.
	 */
	@Bean(name = "systemProperties")
	Map<String, String> getApplicationProperties() {
		determineOS();
		
		String supportedOS = env.getProperty("supportedOS");
		if (supportedOS == null) {
			throw new IllegalArgumentException("Properties does not specify a list of supportedOS");
		}
		if (!supportedOS.contains(os)) {
			throw new IllegalArgumentException("Current OS (" + os + ") is not in supported list: " + supportedOS);
		}
		
		Map<String, String> pushMap = new HashMap<>();
		return pushMap;
	}

	@Bean(name = "driveFactory")
	@DependsOn("systemProperties")
	Supplier<WebDriver> getDriverFactory() throws MalformedURLException {
		String selectedBrowser = System.getProperty("browser").toLowerCase();
		if(selectedBrowser == null||selectedBrowser.trim().equals("")){
			selectedBrowser = "chrome";
			System.setProperty("browser", "chrome");
		}
		
		String supportedBrowsers = env.getProperty("supportedBrowsers." + os);
		if (!supportedBrowsers.contains(selectedBrowser)) {
			throw new IllegalArgumentException("Cannot support browser " + selectedBrowser + 
					" on OS " + os + ", only support: " + supportedBrowsers);
		}

		String prefix = os + "." + selectedBrowser;
		
		String propList = env.getProperty(prefix + ".system.properties");
		if (propList != null) {
			for (String prop : propList.split(",")) {
				String key = env.getProperty(prefix + "." + prop + ".key");
				String val = env.getProperty(prefix + "." + prop + ".value");
				log.info("Set property {} to {}", key, val);
				System.setProperty(key, val);
			}
		}

		if (selectedBrowser.equals("firefox")) {
			/* Remote Web Driver example
			 * DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			 * capabilities.setPlatform(Platform.VISTA);
			 * WebDriver driver = new RemoteWebDriver(new URL("http://10.1.6.46:6667/wd/hub"), capabilities);
			 * Supplier<WebDriver> defaultSupplier = new DefaultRemoteSupplier(
	         *		new URL("http://10.1.6.46:6667/wd/hub"), capabilities, capabilities);
			 * Supplier<WebDriver> defaultSupplier = new DefaultRemoteSupplier();
			 * return defaultSupplier;
			 */
			return FirefoxDriver::new;
		} else if (selectedBrowser.equals("safari")) {
			DesiredCapabilities capabilities = DesiredCapabilities.safari();
			capabilities.setBrowserName("safari");
			return SafariDriver::new;
		} else if (selectedBrowser.equals("ie")) {
			return InternetExplorerDriver::new;
		} else if (selectedBrowser.equals("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-extensions");
			//driver = new ChromeDriver(options);
			//return ChromeDriver::new;
			return () -> new ChromeDriver(options);
		} else if (selectedBrowser.equals("headless")) {
			return HtmlUnitDriver::new;
		} else {
			throw new IllegalArgumentException("No browser specified");
		}
	}
	
//	 /**
//	   * Creates basic {@link RemoteWebDriver} instances.
//	   */
//	  private static class DefaultRemoteSupplier implements Supplier<WebDriver> {
//	    @Override
//	    public WebDriver get() {
//	    	DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//	    	RemoteWebDriver driver = null;
//			try {
//				driver = new RemoteWebDriver(new URL("http://10.1.6.46:5556/wd/hub"), capabilities);
//			} catch (MalformedURLException e) {
//				e.printStackTrace();
//			}
//	      return driver;
//	    }
//	  }

}
