package io.ycj28c.uitest.framework;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import io.ycj28c.uitest.config.DatabaseConfig;
import io.ycj28c.uitest.config.SeleniumConfig;
import io.ycj28c.uitest.util.ExportDataModal;
import io.ycj28c.uitest.util.PropertiesUtil;
import io.ycj28c.uitest.util.ScreenShotReportUtil;
import io.ycj28c.uitest.util.ScreenShotUtil;
import io.ycj28c.uitest.util.SlackConnection;
import io.ycj28c.uitest.util.SlackUtil;
import io.ycj28c.uitest.util.TimeUtil;

/**
 * Base class for Selenium tests.  Provides a WebDriver that is wired appropriately.
 */
@ContextConfiguration(classes = { 
	SeleniumConfig.class,
	DatabaseConfig.class
})
@Listeners({TestNGListener.class})
abstract public class AbstractSeleniumTest extends AbstractTest{

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	/* can use Bean to autowired the datasource, but not easy to manage */
//	@Autowired
//	protected Hashtable<String, JdbcTemplate> jdbcTemplateMaps;
	
//	@Autowired
//	protected RestTemplate restTemplate;
	
	@Autowired
	@Qualifier("driveFactory") //use the drivefactory bean
	private Supplier<WebDriver> driverFactory;

	protected WebDriver driver;
//	private Boolean driver_active = false;
	
	@Parameters({ "browser", "nodeUrl"})
	@BeforeMethod(alwaysRun = true)
	protected void initDriver(@Optional String browser, @Optional String nodeUrl) {
		String currentClass = this.getClass().getName();
		try{
			destroyDriver();
			
			if (hasQuit(driver)) {		
				boolean isRunInGrid = PropertiesUtil.getSeleniumGridSwitch(env);
				if(isRunInGrid){ //if run in grid, if run in grid, do not need to use seleniumConfig.java, will get properties directly from xml
					/* if already set browser in testSuiteXml, then run the browser 
					 * defined in testSuiteXml, if not, run the browser set in -Dbrowser 
					 */
					if(browser == null||browser.equals("")){
						browser = System.getProperty("browser");
					} 
					/* if nodeUrl is null, then run in local mode */
					if(nodeUrl == null||nodeUrl.equals("")){
						driver = driverFactory.get();
						log.info("\n\n** Test {} | Single Machine Mode | {} | {}\n", currentClass, nodeUrl, browser);
					} else {
						driver = setRemoteDriver(browser,nodeUrl);
						log.info("\n\n** Test {} | Selenium Grid Mode | {} | {}\n", currentClass, nodeUrl, browser);
					}
				} else { //run in local
					driver = driverFactory.get();
					log.info("\n\n** Test {} | Single Machine Mode | {} | {}\n", currentClass, nodeUrl, browser);
				}
				
//				driver_active = true;
	
				// add the event listener for webDriver test 
				if(PropertiesUtil.getEventListenerSwitch(env)){
					EventFiringWebDriver myTestDriver = new EventFiringWebDriver(driver);
			        SeleniumEventListener myListener = new SeleniumEventListener();
			        myListener.eventLogSwitch = PropertiesUtil.getEventListenerLogSwitch(env); // open /close the listener log
			        myTestDriver.register(myListener);
			        driver = myTestDriver;  
				}	
			}
		} catch(Exception e){
			log.error("Error occurs when test {} try to initial the browser driver.", currentClass);
			e.printStackTrace();
		}
	}
	
	private boolean hasQuit(WebDriver driver) {
//		try{
//			if(driver instanceof RemoteWebDriver){
//				return ((RemoteWebDriver) driver).getSessionId() == null;
//			} else {
//				return driver.toString().contains("(null)");
//			}
//		
//		} catch(java.lang.NullPointerException e){
//			return true;
//		}
		try {
            driver.getTitle();
            return false;
        } catch (org.openqa.selenium.NoSuchSessionException e) {
            return true;
        } catch (java.lang.NullPointerException ex){
        	return true;
        }
	}
	
	private RemoteWebDriver setRemoteDriver(String browser, String nodeUrl) {
		String url = nodeUrl+"/wd/hub";
		DesiredCapabilities capabilities = null;
		switch(browser){
			case "firefox":
				capabilities = DesiredCapabilities.firefox();
				//capabilities.setPlatform(Platform.WINDOWS); //use default
				break;
			case "chrome":
				capabilities = DesiredCapabilities.chrome();
				break;
			case "safari":
				capabilities = DesiredCapabilities.safari();
				break;
			case "ie":
				capabilities = DesiredCapabilities.internetExplorer();
				break;
			case "headless":
				capabilities = DesiredCapabilities.htmlUnit();
				break;
			case "android":
				capabilities = DesiredCapabilities.android();
				break;
			case "iphone":
				capabilities = DesiredCapabilities.iphone();
				break;	
			case "ipad":
				capabilities = DesiredCapabilities.ipad();
				break;
			default:
				throw new IllegalArgumentException("No browser specified");
		}
		try	{
			return new RemoteWebDriver(new URL(url), capabilities);
		} catch(MalformedURLException e){
			log.error("Error occurs when test {} generate the browser driver.", this.getClass().getName());
			return null;
		}
	}

	@AfterMethod(alwaysRun = true)
	protected void statusCheck(ITestResult testResult) {
		try{
			if (testResult.getStatus() == ITestResult.FAILURE) {
				//set export data
				ExportDataModal edModal = new ExportDataModal();
				edModal.setMethodName(testResult.getInstanceName()+"."+testResult.getName());
				edModal.setErrorLog( testResult.getThrowable().getMessage());
				edModal.setStartTime(TimeUtil.millisecondsToTime(testResult.getStartMillis()));
				edModal.setEndTime(TimeUtil.millisecondsToTime(testResult.getEndMillis()));
				edModal.setDescription(testResult.getMethod().getDescription());
				edModal.setCurrentUrl(driver.getCurrentUrl());
				edModal.setPageTitle(driver.getTitle());
	
				if(PropertiesUtil.getScreenshotStatus(env)){ 	//if testNG fails, screen shot
					log.info("\n\n ** Screen Shot! for test {}", testResult.getName());
					String imageName = ScreenShotUtil.captureScreenshot(SCREEN_SHOT_FOLDER, testResult, driver);
					edModal.setImageName(imageName);
					String imagePath = SCREEN_SHOT_FOLDER + "/" + imageName;
					edModal.setLocalImagePath(imagePath);
					ScreenShotReportUtil.addToReport(edModal);
				}	
				
				if (PropertiesUtil.getSlackStatus(env)
						&& !System.getProperty("slack").trim().equalsIgnoreCase("off")) { // slack support
					log.info("\n\n ** Slack Message Prepare! for test {}", testResult.getName());
					// upload the image to slack
					SlackConnection slackCon = new SlackConnection(env);
					SlackUtil slack = new SlackUtil();
					/*
					 * slack API change, it still can upload, but not able to share to public
					 * 
					 * if(PropertiesUtil.getScreenshotStatus(env)){ //if has screenshot, then allow upload image to slack  
					 * 	 String uploadImageUrl = slack.uploadImage(slackCon, edModal.getLocalImagePath(), edModal.getImageName());
					 * 	 edModal.setUploadImageUrl(uploadImageUrl);
					 * } 
					 *
					 * if(PropertiesUtil.getSlackChannel()!=null&&!PropertiesUtil.getSlackChannel().trim().isEmpty()){
					 *	 log.info("\n\n ** Slack Message Send To Channel {}!", PropertiesUtil.getSlackChannel());
					 *	 slack.sendResultToChannel(edModal, slackCon); //send to channel
					 * }
					 * if(PropertiesUtil.getSlackUser()!=null&&!PropertiesUtil.getSlackUser().trim().isEmpty()){
					 *	 log.info("\n\n ** Slack Message Send To User {}!", PropertiesUtil.getSlackUser());
					 *	 slack.sendResultToUser(edModal, slackCon); //send to user
					 * }
					 */
					if(PropertiesUtil.getSlackChannel()!=null&&!PropertiesUtil.getSlackChannel().trim().isEmpty()){
						log.info("\n\n ** Slack Message for test {}, Send To Channel {}!", testResult.getName(), PropertiesUtil.getSlackChannel());
						slack.sendResultToChannel(edModal, slackCon); //send to channel
						if(PropertiesUtil.getScreenshotStatus(env)){ 
							slack.uploadImageToChannel(slackCon, edModal);
						} 
					}
					if(PropertiesUtil.getSlackUser()!=null&&!PropertiesUtil.getSlackUser().trim().isEmpty()){
						log.info("\n\n ** Slack Message for test {}, Send To User {}!", testResult.getName(), PropertiesUtil.getSlackUser());
						slack.sendResultToUser(edModal, slackCon); //send to user
						if(PropertiesUtil.getScreenshotStatus(env)){ 
							slack.uploadImageToUser(slackCon, edModal);
						} 
					}
					
				}	
			}       
		} catch(Exception e){
			log.error("Error occurs when test {} try to present the test result.", testResult.getName());
			e.printStackTrace();
		}
		try{
			LogoutHelper.directLogout(env, driver);
		} catch(org.openqa.selenium.TimeoutException e){
			log.error("Error occurs when test {} try to log out.", testResult.getName());
			e.printStackTrace();
		}
//		driver.manage().deleteAllCookies();
		destroyDriver(); 
	}
	
	private void destroyDriver() {
		if (!hasQuit(driver)) {
//			this.driver.close();	
			driver.quit();
//			driver_active = false;
		}
	}

}

