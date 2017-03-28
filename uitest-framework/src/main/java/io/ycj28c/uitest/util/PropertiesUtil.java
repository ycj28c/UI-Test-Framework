package io.ycj28c.uitest.util;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {
	
	public static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
	
	/**
	 * get the screenshot status: on/off
	 * @return
	 */
	public static boolean getScreenshotStatus(Environment env){
		boolean screenShotStatus = false;
		String screenShotStatusSetting = env.getProperty("screenshot.status");
		if(screenShotStatusSetting == null || screenShotStatusSetting.toLowerCase().isEmpty()){ //default set to off
			screenShotStatusSetting = "off";
		}
		switch(screenShotStatusSetting.toLowerCase()){
			case "on":
				screenShotStatus = true;
				break;
			case "off":
				screenShotStatus = false;
				break;
			default:
				screenShotStatus = false;
				break;
		}
		return screenShotStatus;
	}
	
	/**
	 * get the screenshot status: on/off
	 * @return
	 */
	public static boolean getScreenshotStatus(Properties env){
		boolean screenShotStatus = false;
		String screenShotStatusSetting = env.getProperty("screenshot.status");
		if(screenShotStatusSetting == null || screenShotStatusSetting.toLowerCase().isEmpty()){ //default set to off
			screenShotStatusSetting = "off";
		}
		switch(screenShotStatusSetting.toLowerCase()){
			case "on":
				screenShotStatus = true;
				break;
			case "off":
				screenShotStatus = false;
				break;
			default:
				screenShotStatus = false;
				break;
		}
		return screenShotStatus;
	}
	
	/**
	 * get the slack status: on/off
	 * @param env
	 * @return
	 */
	public static boolean getSlackStatus(Environment env) {
		boolean slackStatus = false;
		String slackStatusSetting = env.getProperty("slack.status");
		if(slackStatusSetting == null || slackStatusSetting.toLowerCase().isEmpty()){ //default set to off
			slackStatusSetting = "off";
		}
		switch(slackStatusSetting.toLowerCase()){
			case "on":
				slackStatus = true;
				break;
			case "off":
				slackStatus = false;
				break;
			default:
				slackStatus = false;
				break;
		}
		return slackStatus;
	}
	
	/**
	 * get the testNG retry status: on/off
	 * @return
	 */
	public static boolean getTestNGRetrySwitch(){
		boolean testNGRetrySwitch = false;
		String testNGRetryStatusSetting = getPropertyConfig("testNG.retry.switch");
		if(testNGRetryStatusSetting == null || testNGRetryStatusSetting.toLowerCase().isEmpty()){ //default set to off
			testNGRetryStatusSetting = "off";
		}
		switch(testNGRetryStatusSetting.toLowerCase()){
			case "on":
				testNGRetrySwitch = true;
				break;
			case "off":
				testNGRetrySwitch = false;
				break;
			default:
				testNGRetrySwitch = false;
				break;
		}
		return testNGRetrySwitch;
	}
	
	/**
	 * get the testNG max retry times
	 * @return
	 */
	public static int getTestNGRetryTimes(){
		int maxRetry = 0;
		String testNGRetryTimesSetting = getPropertyConfig("testNG.retry.maxRetry");
		if(testNGRetryTimesSetting == null || testNGRetryTimesSetting.toLowerCase().isEmpty()){ //default set to off
			testNGRetryTimesSetting = "3";
		}
		
		//check if the maxRetry parameter is a positive integer 
		String pattern = "[0-9]*";
		Pattern p = Pattern.compile(pattern);  
		Matcher m = p.matcher(testNGRetryTimesSetting);  
        boolean b = m.matches(); 
        if(!b){  
        	log.error(" {} is not a valid positive integer", testNGRetryTimesSetting);
        	return maxRetry;
        } 
        
        //transfer the string to integer
		try{
        	maxRetry = Integer.parseInt(testNGRetryTimesSetting);
        } catch(Exception ex){
        	log.error(" can not transfer the {} to Integer", testNGRetryTimesSetting);
        }
		return maxRetry;
	}
	
	/**
	 * get the event listener switch status: on/off
	 * @return
	 */
	public static boolean getEventListenerSwitch(Environment env){
		boolean eventListenerSwitch = false;
		String eventListenerStatus = env.getProperty("event.listener.switch");
		if(eventListenerStatus == null || eventListenerStatus.toLowerCase().isEmpty()){ //default set to off
			eventListenerStatus = "off";
		}
		switch(eventListenerStatus.toLowerCase()){
			case "on":
				eventListenerSwitch = true;
				break;
			case "off":
				eventListenerSwitch = false;
				break;
			default:
				eventListenerSwitch = false;
				break;
		}
		return eventListenerSwitch;
	}
	
	/**
	 * get the selenium grid switch status: on/off
	 * @return
	 */
	public static boolean getSeleniumGridSwitch(Environment env){
		boolean seleniumGridSwitch = false;
		String seleniumGridSwitchStr = env.getProperty("selenium.grid.switch");
		if(seleniumGridSwitchStr == null || seleniumGridSwitchStr.toLowerCase().isEmpty()){ //default set to off
			seleniumGridSwitchStr = "off";
		}
		switch(seleniumGridSwitchStr.toLowerCase()){
			case "on":
				seleniumGridSwitch = true;
				break;
			case "off":
				seleniumGridSwitch = false;
				break;
			default:
				seleniumGridSwitch = false;
				break;
		}
		return seleniumGridSwitch;
	}
	
	/**
	 * get the event listener log switch status: on/off
	 * @return
	 */
	public static boolean getEventListenerLogSwitch(Environment env){
		boolean eventListenerLogSwitch = false;
		String eventListenerLogStatus = env.getProperty("event.listener.log.switch");
		if(eventListenerLogStatus == null || eventListenerLogStatus.toLowerCase().isEmpty()){ //default set to off
			eventListenerLogStatus = "off";
		}
		switch(eventListenerLogStatus.toLowerCase()){
			case "on":
				eventListenerLogSwitch = true;
				break;
			case "off":
				eventListenerLogSwitch = false;
				break;
			default:
				eventListenerLogSwitch = false;
				break;
		}
		return eventListenerLogSwitch;
	}
	
	/**
	 * get the retry type setting
	 * @return
	 */
	public static String getRetryType() {
		String retryType = PropertiesUtil.getPropertyConfig("testNG.retry.retryType");
		if(retryType == null||retryType.trim().equals("")){
			retryType = "default";
		}
		return retryType.trim();
	}
	
	/**
	 * get the slack file upload api url
	 * @return
	 */
	public static String getSlackFileUploadAPI() {
		String slackFuAPI = PropertiesUtil.getPropertyConfig("slack.files.upload.api");
		if(slackFuAPI == null||slackFuAPI.trim().equals("")){
			log.error("getSlackFileUploadAPI error: slack.files.upload.api didn't config in properties");
			return null;
		}
		return slackFuAPI.trim();
	}
	
	/**
	 * get the slack token
	 * @return
	 */
	public static String getSlackToken() {
		String slackToken = PropertiesUtil.getPropertyConfig("slack.token");
		if(slackToken == null||slackToken.trim().equals("")){
			log.error("getSlackToken error: slack.token didn't config in properties");
			return null;
		}
		return slackToken.trim();
	}
	
	/**
	 * get the webhook url
	 * @return
	 */
	public static String getSlackWebHookURL() {
		String slackWebHook = PropertiesUtil.getPropertyConfig("slack.webhook.url");
		if(slackWebHook == null||slackWebHook.trim().equals("")){
			log.error("getSlackWebHookURL error: slack.webhook.url didn't config in properties");
			return null;
		}
		return slackWebHook.trim();
	}
	
	/**
	 * get the slack channel send message to
	 * @return
	 */
	public static String getSlackChannel() {
		String slackChannel = PropertiesUtil.getPropertyConfig("slack.send.channel");
		if(slackChannel == null||slackChannel.trim().equals("")){
			log.error("getSlackChannel error: slack.send.channel didn't config in properties");
			return null;
		}
		return slackChannel.trim();
	}
	
	/**
	 * get the slack user send message to
	 * @return
	 */
	public static String getSlackUser() {
		String slackUser = PropertiesUtil.getPropertyConfig("slack.send.user");
		if(slackUser == null||slackUser.trim().equals("")){
			log.error("getSlackUser error: slack.send.user didn't config in properties");
			return null;
		}
		return slackUser.trim();
	}
	
	/**
	 * get the slack bot display name
	 * @return
	 */
	public static String getSlackDisplayName() {
		String slackDisplayName= PropertiesUtil.getPropertyConfig("slack.display.name");
		if(slackDisplayName == null||slackDisplayName.trim().equals("")){
			log.error("getSlackDisplayName error: slack.display.name didn't config in properties");
			return null;
		}
		return slackDisplayName.trim();
	}
	
	/** 
	 * read the configuration file, get the value
	 * @param key
	 * @return the value of the key
	 */
	public static String getPropertyConfig(String key) {
		String envName = System.getProperty("env") == null ? "env-qa.properties"
				: "env-" + System.getProperty("env") + ".properties";
		
        Resource resource = new ClassPathResource(envName);
        Properties props = null;
        try {
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
        	log.error("getPropertyConfig {} error:", envName, e);
        }
        String propertyValue = null;
        try{
        	propertyValue = props.getProperty(key);
        } catch(Exception ex){
        	log.error("Can't get property {}:", key, ex);
        }
        return propertyValue;
    }
	
}
