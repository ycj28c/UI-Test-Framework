package io.ycj28c.uitest.framework;

/**
 * Base class for all pages.  Provides support for logging and common page methods.
 */

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.ycj28c.uitest.util.PropertiesUtil;

public abstract class AbstractPage {
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	protected int DEFAULT_TIMEOUT = 12;
	
	protected WebDriver driver;
	protected ElementHelper helper;
	protected int ELEMENT_WAIT_TIME;
	
	protected AbstractPage(WebDriver driver) {
		this.driver = driver;
		String timeout = PropertiesUtil.getPropertyConfig("ELEMENT_WAIT_TIME");
		this.ELEMENT_WAIT_TIME = (timeout==null||timeout.trim().equals(""))==true? DEFAULT_TIMEOUT:Integer.parseInt(timeout.trim());
		this.helper = new ElementHelper(driver, ELEMENT_WAIT_TIME);
	}
	
	public String pageUrl() {
		return driver.getCurrentUrl();
	}
	
	public String pageTitle() {
		return driver.getTitle();
	}
	
	public boolean pageUrlMatched(String partialUrl, int seconds) {
		helper.waitForURL(partialUrl, seconds);
		return pageUrl().contains(partialUrl);
	}
	
	public boolean pageTitleMatched(String partialTitle, int seconds) {
		helper.waitForTitle(partialTitle, seconds);
		return pageTitle().contains(partialTitle);
	}	
	
	public void pause(int milliSeconds){
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}
	
	public boolean waitPageRefresh(WebElement trigger) {  
	    int refreshTime = 0;  
	    boolean isRefresh = false;  
	    try {  
	        for (int i = 1; i < 60; i++) {  
	            refreshTime = i;  
	            trigger.getTagName();  
	            Thread.sleep(1000);  
	        }  
	    } catch (StaleElementReferenceException e) {  
	        isRefresh = true;  
	        log.info("Page refresh time is: {} seconds!", refreshTime);  
	        return isRefresh;  
	    } catch (WebDriverException e) {  
	        e.printStackTrace();  
	    } catch (InterruptedException e) {  
	        e.printStackTrace();  
	    }  
	    log.error("Page didnt refresh in 60 seconds!");  
	    return isRefresh;  
	}
	
	public boolean waitPageRefresh(By trigger) {  
	    int refreshTime = 0;  
	    boolean isRefresh = false;  
	    try {  
	        for (int i = 1; i < 60; i++) {  
	            refreshTime = i;  
	            driver.findElement(trigger).getTagName();  
	            Thread.sleep(1000);  
	        }  
	    } catch (StaleElementReferenceException e) {  
	        isRefresh = true;  
	        log.info("Page refresh time is: {} seconds!", refreshTime);  
	        return isRefresh;  
	    } catch (WebDriverException e) {  
	        e.printStackTrace();  
	    } catch (InterruptedException e) {  
	        e.printStackTrace();  
	    }  
	    log.error("Page didnt refresh in 60 seconds!");  
	    return isRefresh;  
	}
}
