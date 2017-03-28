package io.ycj28c.uitest.framework;

import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import io.ycj28c.uitest.util.PropertiesUtil;

public class TestNGRetryAnalyzer implements IRetryAnalyzer {
	public final Logger log = LoggerFactory.getLogger(TestNGRetryAnalyzer.class);
    public static int maxRetryCount;

    AtomicInteger count = new AtomicInteger(maxRetryCount);
   
    public boolean isRetryAvailable() {
        return (count.intValue() > 0);
    }
    
    @Override
    public boolean retry(ITestResult result) {
    	boolean retry = false;
		//Throwable cause = result.getThrowable().getCause();
    	Throwable cause = result.getThrowable();

		log.info(" >> test {} fail cause by exception: {}", result.getName(), result.getThrowable().getClass()); 
		String retryType = PropertiesUtil.getRetryType();
		//currently only check TimeoutException and NoSuchElementException, wait for future modify
		if (isRetryAvailable()) {
			switch(retryType.toLowerCase()){
				case("exception"):
					if(cause instanceof org.openqa.selenium.TimeoutException){ 
						log.info("\n\n >> Going to retry test case: {}, {} out of {}\n", result.getName(), (maxRetryCount - count.intValue() + 1), maxRetryCount);
			            retry = true;
					} else if(cause instanceof org.openqa.selenium.NoSuchElementException){
						log.info("\n\n >> Going to retry test case: {}, {} out of {}\n", result.getName(), (maxRetryCount - count.intValue() + 1), maxRetryCount);
			            retry = true;
					} else {
						retry = false; 
					}
					break;
				case("all"):
					log.info("\n\n >> Going to retry test case: {}, {} out of {}\n", result.getName(), (maxRetryCount - count.intValue() + 1), maxRetryCount);
					retry = true;
					break;
				case("default"):
					log.info("\n\n >> Going to retry test case: {}, {} out of {}\n", result.getName(), (maxRetryCount - count.intValue() + 1), maxRetryCount);
					retry = true;
					break;
				default:
					log.error("\n\n >> The retry type parameter of test {} is not recognized, please check your properties file.", result.getName());
					retry = false; 
					break;
			}
			count.decrementAndGet();
		} 	
        return retry;
    }
}