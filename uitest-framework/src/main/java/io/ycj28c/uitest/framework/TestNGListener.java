package io.ycj28c.uitest.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import io.ycj28c.uitest.util.PropertiesUtil;

public class TestNGListener extends TestListenerAdapter {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
    public void onTestStart(ITestResult result) {
		String methodName = result.getName();
		log.info("\n\n **BEGIN {}**", methodName);
		
		TestNGRetryAnalyzer.maxRetryCount = PropertiesUtil.getTestNGRetryTimes();
		log.info(" >> TestNGListener: onTestStart");
		if(PropertiesUtil.getTestNGRetrySwitch()){
			if (result.getMethod().getRetryAnalyzer() != null) {
				log.info(" >> The retryAnalyze already existed, will use {}", result.getMethod().getRetryAnalyzer().toString());
	        } else { //set our TestNGRetryAnalyzer as retry function
	        	TestNGRetryAnalyzer retryAnalyzer = new TestNGRetryAnalyzer();
	        	result.getMethod().setRetryAnalyzer(retryAnalyzer);
	        	log.info(" >> Implement TestNGRetryAnalyzer to {}", result.getName());
	        }
		}
    }
	 
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    	String methodName = result.getName();
    	log.info(" >> TestNGListener: onTestFailedButWithinSuccessPercentage");
        if (result.getMethod().getRetryAnalyzer() != null&&PropertiesUtil.getTestNGRetrySwitch()) {
            TestNGRetryAnalyzer retryAnalyzer = (TestNGRetryAnalyzer)result.getMethod().getRetryAnalyzer();

            if(retryAnalyzer.isRetryAvailable()) {
            	log.info(" >> retryAnalyzer is available, set the test "+methodName+" status to 'SKIP'");
                result.setStatus(ITestResult.SKIP);
            } else {
            	log.info(" >> retryAnalyzer is not available, set the test "+methodName+" status to 'FAILURE'");
                result.setStatus(ITestResult.FAILURE);
            }
            Reporter.setCurrentTestResult(result);
        } 
    }
 
    /*
    //remove the skipped and tests run number because of retry, don't need this function currently
	@Override
	public void onFinish(ITestContext context) {
		Iterator<ITestResult> failedTestCases = context.getFailedTests()
				.getAllResults().iterator();
		while (failedTestCases.hasNext()) {
			System.out.println("failedTestCases");
			ITestResult failedTestCase = failedTestCases.next();
			ITestNGMethod method = failedTestCase.getMethod();
			if (context.getFailedTests().getResults(method).size() > 1) {
				System.out.println("failed test case remove as dup:"
						+ failedTestCase.getTestClass().toString());
				failedTestCases.remove();
			} else {
				if (context.getPassedTests().getResults(method).size() > 0) {
					System.out.println("failed test case remove as pass retry:"
							+ failedTestCase.getTestClass().toString());
					failedTestCases.remove();
				}
			}
		}
	}*/
}