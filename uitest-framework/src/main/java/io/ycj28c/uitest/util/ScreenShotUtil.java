package io.ycj28c.uitest.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;

public class ScreenShotUtil {
	private static final Logger log = LoggerFactory.getLogger(ScreenShotUtil.class);
	
	/**
	 * web screen shot save to local disk as png format 
	 * @param screenShotFolder
	 * @param testResult
	 * @param driver
	 * @return
	 */
	public static String captureScreenshot(String screenShotFolder, ITestResult testResult, WebDriver driver) {
		String filename = testResult.getInstanceName()+"."+testResult.getName();
		return captureScreenshot(screenShotFolder, filename, driver);
	}
	
	/**
	 * web screen shot save to local disk as png format 
	 * @param screenShotFolder
	 * @param testResult
	 * @param driver
	 * @return
	 */
	public static String captureScreenshot(String screenShotFolder, String filename, WebDriver driver) {
		String fileName = generateRandomFilename(filename)+".png";
		
		File fp = new File(screenShotFolder);  
		if (!fp.exists() &&!fp.isDirectory())  //if folder not existed, create one
		{       
			log.info(" folder {} doesn't exist, create new one", screenShotFolder);
			fp.mkdir();    
		}
		
		String imagePath = screenShotFolder + "/" + fileName;
		log.info(" the target image path is {}", imagePath);
		File image = new File(imagePath);
		
		if(!image.exists())    
		{      
			log.info(" take the screenshot and save to disk {}",imagePath);
		    TakesScreenshot tsDriver = (TakesScreenshot) driver;
		    try {
				 FileUtils.moveFile(tsDriver.getScreenshotAs(OutputType.FILE), image);
			} catch (WebDriverException e) {
				log.error(e.getMessage());
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			//boolean xx = tsDriver.getScreenshotAs(OutputType.FILE).renameTo(image);  //renameTO method doesn't have exception
			return fileName;
		} else {
			log.error(" the file {} is already existed", imagePath);
			return null;
		}	
	}
	
	/**
	 * generate the filename dependent on exception
	 */
	@SuppressWarnings("unused")
	private static String generateRandomFilename(ITestResult testResult) {
        String filename = testResult.getInstanceName()+"."+testResult.getName();
        return generateRandomFilename(filename);
    }
	
	/**
	 * generate the filename dependent on exception
	 */
	private static String generateRandomFilename(String filename) {
        Calendar c = Calendar.getInstance();
        if(filename.length()>50){ //keep most 50 char
        	filename = filename.substring(0, 50).trim();
        }
        filename = filename.replaceAll("\\s", "_").replaceAll(":", "").replaceAll("\\*", "");
        filename = "" + c.get(Calendar.YEAR) + 
        	"-" + (c.get(Calendar.MONTH)+1) + 
            "-" + c.get(Calendar.DAY_OF_MONTH) +
            "-" + c.get(Calendar.HOUR_OF_DAY) +
            "-" + c.get(Calendar.MINUTE) +
            "-" + c.get(Calendar.SECOND) +
            "-" + filename;
        return filename;
    }

}