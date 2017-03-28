package io.ycj28c.uitest.framework;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
/**
 * Notice: because we still using webDriver but not EventFiringWebDriver to operate the Element.
 * 		   So the event below maybe miss the parameter value (the WebElement arg0) can be null, may cause the exception. 
 */
public class SeleniumEventListener implements WebDriverEventListener{
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	public boolean eventLogSwitch = false;
	
	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		if(eventLogSwitch){
			if (arg0 == null) {
	            log.info("@@ eventListener: inside method afterClickOn on browser: {}", arg1.toString());
	        } else {
	        	log.info("@@ eventListener: inside method afterClickOn on: {} on browser: {}", arg0.toString(), arg1.toString());
	        }
		}
	}
 
	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		if(eventLogSwitch){
			if (arg1 == null) {
				log.info("@@ eventListener: After FindBy on: {} on browser: {}", arg0.toString(), arg2.toString());
	        } else {
	        	log.info("@@ eventListener: After FindBy on: {} for element: {} on browser: {}", arg0.toString(), arg1.toString(), arg2.toString());
	        }
		}
	}
 
	@Override
	public void afterNavigateBack(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Inside the after navigate back to {}", arg0.getCurrentUrl());
		}
	}
 
	@Override
	public void afterNavigateForward(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Inside the after navigate forward to {}", arg0.getCurrentUrl());
		}
	}
 
	@Override
	public void afterNavigateTo(String arg0, WebDriver arg1) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Inside the after navigate back to {}", arg0);
		}
	}
	
	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Inside the afterScript to, Script is {}", arg0);
		}
	}
 
	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		if(eventLogSwitch){
			if (arg0 == null) {
	            log.info("@@ eventListener: Before clicking on browser: {}", arg1.toString());
	        } else {
	        	log.info("@@ eventListener: Before clicking on element {} on browser: {}", arg0.toString(), arg1.toString());
	        } 
		}
	}
 
	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		if(eventLogSwitch){
			if (arg1 == null) {
				log.info("@@ eventListener: Before finding element: {} on browser: {}", arg0.toString(), arg2.toString()); 
	        } else {
	        	log.info("@@ eventListener: Before finding element: {} inside {} Web element on browser: {}", 
	        			arg0.toString(), arg1.toString(), arg2.toString()); 
	        }
		}
	}

	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Navigate back {}", arg0.getCurrentUrl()); 
		}
	}
 
	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Navigate forward {}", arg0.getCurrentUrl());  
		}
	}
 
	@Override
	public void beforeNavigateTo(String arg0, WebDriver arg1) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Navigate to {} on {}", arg0, arg1.getCurrentUrl());
		}
	}
 
	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Script {}", arg0); 
		}
	}
 
	@Override
	public void onException(Throwable arg0, WebDriver arg1) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Exception occured at {}", arg0.getMessage());
		}
	}

	@Override
	public void afterAlertAccept(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just after Alert Accept {}", arg0.getCurrentUrl()); 
		}
	}

	@Override
	public void afterAlertDismiss(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just after Alert Dismiss {}", arg0.getCurrentUrl()); 
		}		
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		if(eventLogSwitch){
			if (arg0 == null) {
				log.info("@@ eventListener: After FindBy on: {} on browser: {}", arg2.toString(), arg1.toString());
	        } else {
	        	log.info("@@ eventListener: After FindBy on: {} for element: {} on browser: {}", arg2.toString(), arg0.toString(), arg1.toString());
	        }
		}
	}

	@Override
	public void afterNavigateRefresh(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just after Navigate Refresh {}", arg0.getCurrentUrl()); 
		}	
	}

	@Override
	public void beforeAlertAccept(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Alert Accept {}", arg0.getCurrentUrl()); 
		}	
	}

	@Override
	public void beforeAlertDismiss(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Alert Dismiss {}", arg0.getCurrentUrl()); 
		}			
	}

	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1, CharSequence[] arg2) {
		if(eventLogSwitch){
			if (arg0 == null) {
				log.info("@@ eventListener: After FindBy on: {} on browser: {}", arg2.toString(), arg1.toString());
	        } else {
	        	log.info("@@ eventListener: After FindBy on: {} for element: {} on browser: {}", arg2.toString(), arg0.toString(), arg1.toString());
	        }
		}
	}

	@Override
	public void beforeNavigateRefresh(WebDriver arg0) {
		if(eventLogSwitch){
			log.info("@@ eventListener: Just before Navigate Refresh {}", arg0.getCurrentUrl()); 
		}		
	}
 
}
