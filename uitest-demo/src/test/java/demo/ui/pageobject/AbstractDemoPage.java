package demo.ui.pageobject;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

import io.ycj28c.uitest.framework.AbstractPage;

/*
 * Abstract Page which is super class for page object
 */
public class AbstractDemoPage extends AbstractPage{
	public AbstractDemoPage(WebDriver driver) {
		super(driver);
		
		driver.manage().timeouts().implicitlyWait(2000, TimeUnit.MILLISECONDS); 
		//driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS); //script time, should be longer enough to avoid the Chrome driver problem
		// Warning: Below option is not supported in Safari webdriver
		//driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS); //page load time, should be longer enough to avoid the Chrome driver problem
		
		//AjaxElementLocatorFactory can set element wait time, only use for the WebElement with @FindBy @CacheLookup Tag
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, ELEMENT_WAIT_TIME), this); 
		log.info("** Debug Initial PageObject {}",this.getClass());
	}	
}
