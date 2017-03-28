package io.ycj28c.uitest.framework;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.ycj28c.uitest.config.TestConfig;
import io.ycj28c.uitest.util.CompressUtil;
import io.ycj28c.uitest.util.DirectoryUtil;
import io.ycj28c.uitest.util.PropertiesUtil;
import io.ycj28c.uitest.util.ScreenShotReportUtil;

/**
 * Base class for all tests.  Provides spring support for TestNG, SLF4J logging, and target property support.
 */
@ContextConfiguration(classes = {
	TestConfig.class,
})
@TestPropertySource(locations = {"classpath:env-${env:default}.properties"})
abstract public class AbstractTest extends AbstractTestNGSpringContextTests {
	
	/* TODO
	 * need to separate the selenium test and rest test report 
	 */
	protected final String SCREEN_SHOT_FOLDER = "target/surefire-reports/screenshots"; //it is better to hard coding it as same as other surefire report
	protected final String ZIP_FILE_PATH = "target/surefire-reports/screenShotReport.zip"; //it is better to hard coding it as same as other surefire report

	// Not static: we want log information included from each of the subclasses, not from this one.
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected Environment env;
	
	@BeforeSuite(alwaysRun = true)
	protected void cleanFolder(){
		//remove the screenshot report folder
		log.info("** \n\n ** Begin Test Suite \n");
		DirectoryUtil.deleteDir(new File(SCREEN_SHOT_FOLDER));
	}
	
	@AfterSuite(alwaysRun = true)
	protected void outPutReport(){
		log.info("\n\n ** All the tests completed!");
		if (env != null && PropertiesUtil.getScreenshotStatus(env)) {
			// generate screenshot report
			ScreenShotReportUtil.generateReport(SCREEN_SHOT_FOLDER);
			// compress the report folder
			CompressUtil.zipCompress(SCREEN_SHOT_FOLDER, ZIP_FILE_PATH);
		} else if (System.getProperty("env") != null && !System.getProperty("env").trim().equals("")) {
			log.info("the test env is empty, move the system property");
			try {
				Properties prop = new Properties();
				prop.load(getClass().getClassLoader().getResourceAsStream("env-"+System.getProperty("env")+".properties"));
				if(PropertiesUtil.getScreenshotStatus(prop)){
					// generate screenshot report
					ScreenShotReportUtil.generateReport(SCREEN_SHOT_FOLDER);
					// compress the report folder
					CompressUtil.zipCompress(SCREEN_SHOT_FOLDER, ZIP_FILE_PATH);
				}
			} catch (IOException e) {
				log.error("Error", e);
			}
		} else {
			log.error("Error, the env parameter is null.");
		}
	}
}
