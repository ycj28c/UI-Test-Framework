package demo.ui.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.ycj28c.uitest.framework.WebElements;

public class UIDemoTest extends AbstractUITest {

	/**
	 * a common htmlUnit test, test google without open browser
	 */
	@Test
	public void googleSomething() {
		WebDriver driver = new HtmlUnitDriver();// open html unit test
		// driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);//set
		// waiting max is 10 seconds
		driver.get("https://www.google.com");// open the url

		// Find the text input element by its name
		WebElement element = driver.findElement(By.name("q"));

		// Enter something to search for
		element.sendKeys("github");

		// Now submit the form. WebDriver will find the form for us from the
		// element
		element.submit();

		String aTitle = driver.getTitle();// get title
		System.out.println("current widnow title is:" + aTitle);
		assert aTitle.toLowerCase().contains("github");
	}

	/**
	 * a test for google login page, default using the firefox browser. to
	 * make this test runs, make sure the right path is settled in
	 * browsers.properties and development.properties/Jenkins.properties
	 */
	@Test(groups = { "ui-functionaltest" })
	public void testGoogleLoginPage() {
		log.info("\n\n ** BEGIN testGoogleLoginPage**");
		String expectStr = "Sign in with a different account";

		By locator = By.id("account-chooser-link");
		String actualStr = WebElements.waitUntilElementFound(driver, locator, 10).getText();

		Assert.assertEquals(actualStr, expectStr, "The Sign in text not correct");
	}

}
