package demo.ui.pageobject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import io.ycj28c.uitest.framework.WebElements;

/*
 * Page object for the google login page
 */
public class LoginPage extends AbstractDemoPage {
	
	public LoginPage(WebDriver driver) {
		super(driver);
	}
	
	private static final Logger log = LoggerFactory.getLogger(LoginPage.class);
	
	// google Login
	private By loginLogoLocator            = By.cssSelector("body > div > div.google-header-bar.centered > div > div");
	private By loginBtnLocator       	   = By.id("signIn");
	private By usernameFieldLocator        = By.cssSelector("body > div > div.main.content.clearfix > div.card.signin-card > p");
	private By passwordFieldLocator        = By.cssSelector("#Passwd");

	/**
	 * goto the login page
	 * @param env
	 * @param driver
	 * @return
	 */
	public static LoginPage gotoLoginPage(Environment env, WebDriver driver) {
		final String loginURL = env.getRequiredProperty("loginUrl");
		driver.get(loginURL);
		return new LoginPage(driver);
	}
	
	/**
	 * do login with the username and password
	 * @param username
	 * @param password
	 */
	private void doLogin(String username, String password) {
		log.info("** doLogin: do login with the username and password");
		enterUsername(username);
		enterPassword(password);
		clickLoginButton();
	}
	
	/**
	 * doLoginFromEnv gets the url, username, and password from the environment, and then logs into
	 * the page using the doLogin() method. 
	 * 
	 * @param env
	 * @param driver
	 * @param usernameProperty
	 * @param passwordProperty
	 */
	public static void doLoginFromEnv(Environment env, WebDriver driver, String usernameProperty, String passwordProperty) {
		log.info("** doLoginFromEnv: do login behavior by account {}/{}", usernameProperty, passwordProperty);
		final String url = env.getRequiredProperty("loginUrl");
	    final String username = env.getRequiredProperty(usernameProperty);
	    final String password = env.getRequiredProperty(passwordProperty);
	    
		driver.get(url);
		
		LoginPage loginPage = new LoginPage(driver);
		loginPage.waitForLoginPageToLoad();
		
		loginPage.doLogin(username, password);
	}
    
	public LoginPage enterUsername(String name) {
		log.info("** enterUsername:{}",name);
		WebElement unfield = WebElements
				.waitUntilElementFound(driver, usernameFieldLocator, ELEMENT_WAIT_TIME);
		if (unfield == null) {
			throw new IllegalArgumentException("Cannot login in, no username field found");
		}
		unfield.sendKeys(name);
		return this;
	}
	
	public LoginPage enterPassword(String password) {
		log.info("** enterPassword");
		WebElement pwfield = WebElements
				.waitUntilElementFound(driver, passwordFieldLocator, ELEMENT_WAIT_TIME);
		if (pwfield == null) {
			throw new IllegalArgumentException("Cannot login in, no password field found");
		}
		pwfield.sendKeys(password);
		return this;
	}
	
	public LoginPage clickLoginButton() {
		log.info("** click login button");
		WebElements.waitAndClick(driver, loginBtnLocator, ELEMENT_WAIT_TIME);
		log.info("** clicked login button");
		return this;
	}
	
	public void waitForLoginPageToLoad() {
		log.info("** waitForLoginPageToLoad: wait For login Page To load");
		WebElements.waitUntilElementFound(driver, loginBtnLocator, ELEMENT_WAIT_TIME);
		WebElements.waitUntilElementFound(driver, usernameFieldLocator, ELEMENT_WAIT_TIME);
		WebElements.waitUntilElementFound(driver, passwordFieldLocator, ELEMENT_WAIT_TIME);
    	
    	//for stable issue, add 2 seconds buffer to wait
    	try{
    		Thread.sleep(2000);
    	} catch(Exception e){}
    }
	
	/**
	 * auto login with username and password, not guarantee to return HomePage
	 */
	public static void loginWithUsernamePassword(Environment env, WebDriver driver, String name, String password){
		final String url = env.getRequiredProperty("loginUrl");
	    
		driver.get(url);
		
		System.out.println("** autoLoginWithUsernamePassword: Wait for login page loading");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.waitForLoginPageToLoad();
	    
		loginPage.doLogin(name, password);
	}
	
	/** Verifications
	 * 
	 * @return
	 */
	public boolean hasCompanyLogo() {
		log.info("** hasCompanyLogo");
		return driver.findElement(loginLogoLocator).isDisplayed();
	}
	
	public boolean hasUsernameField() {
		log.info("** hasUsernameField");
		return driver.findElement(usernameFieldLocator).isDisplayed();
	}
	
	public boolean hasPasswordField() {
		log.info("** hasPasswordField");
		return driver.findElement(passwordFieldLocator).isDisplayed();
	}
	
	public boolean hasLoginButton() {
		log.info("** hasLoginButton");
		return driver.findElement(loginBtnLocator).isDisplayed();
	}
	
}
