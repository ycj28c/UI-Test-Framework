package io.ycj28c.uitest.framework;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * some methods around web elements,
 */
public class ElementHelper {
	private WebDriver driver;
	private int       waitTime = 30;
	
	public ElementHelper(WebDriver driver, int defaultWaitSeconds) {
		this.driver = driver;
		this.waitTime = defaultWaitSeconds;
	}
	
	/**
	 * Waits for element described by locator to be found, then clicks it
	 * Uses default wait time
	 * @param locator
	 * @return WebElement
	 */
	public WebElement waitAndClick(final By locator) {
		return waitAndClick(locator, waitTime);
	}
	
	/**
	 * Waits up to waitSeconds for element described by locator 
	 * to be found, then clicks it.
	 * @param locator
	 * @param waitSeconds
	 * @return WebElement
	 */
	public WebElement waitAndClick(final By locator, int waitSeconds) {
		WebElement element;
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, waitSeconds);
			element = wait.until(ExpectedConditions.elementToBeClickable(locator));
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			if (element != null) {
				element.click();
			} else {
				System.out.println("Failed to click " + locator + " (not found)");
			}
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}
	
	/**
	 * Wait for the element to be present in the DOM, regardless of being displayed or not.
	 * And returns the first WebElement. 
	 * @param locator
	 * @return WebElement
	 */
	public WebElement waitUntilElementFound(final By locator) {
		return waitUntilElementFound(locator, this.waitTime);
	}

	/**
	 * Wait for the element to be present in the DOM, regardless of being displayed or not.
	 * And returns the first WebElement. 
	 * @param locator
	 * @param waitSeconds
	 * @return WebElement
	 */
	public WebElement waitUntilElementFound(final By locator, int seconds) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch(Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}

	/**
	 * Tell us if the element exists
	 * @param locator
	 * @param waitSeconds
	 * @return WebElement
	 */
	public boolean isElementFound(final By locator, int seconds) {
		WebElement element = waitUntilElementFound(locator, seconds);
		return element != null;
	}

	/**
	 * Tell us if the element exists
	 * @param locator
	 * @return WebElement
	 */
	public boolean isElementFound(final By locator) {
		return isElementFound(locator, waitTime);
	}


	/**
	 * Tell us if the element exists and is displayed
	 * @param locator
	 * @param waitSeconds
	 * @return WebElement
	 */
	public boolean isElementDisplayed(final By locator, int seconds) {
		WebElement element = waitUntilElementFound(locator, seconds);
		return element != null && element.isDisplayed();
	}

	/**
	 * Tell us if the element exists and is displayed
	 * @param locator
	 * @return WebElement
	 */
	public boolean isElementDisplayed(final By locator) {
		return isElementDisplayed(locator, waitTime);
	}


    
	/**
	 * Wait for the element to be present in the DOM, and displayed on the page.
	 * And returns the first WebElement
	 * @param locator
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementFoundBy(final By locator) {
		return waitUntilElementFoundBy(locator, waitTime);
	}

	/**
	 * Wait for the element to be present in the DOM, and displayed on the page.
	 * And returns the first WebElement
	 * @param locator
	 * @param seconds
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementFoundBy(final By locator, int seconds) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			WebElement element = wait.until(
				ExpectedConditions.visibilityOfElementLocated(locator)
			);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}
	
	
	/**
	 * Wait for element to display on dynamic page
	 * @param locator
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementDisplayed(final By locator) {
		return waitUntilElementDisplayed(locator, waitTime);
	}

	/**
	 * Wait for element to display on dynamic page
	 * @param locator
	 * @param timeOutInSeconds
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementDisplayed(final By locator, int timeOutInSeconds) {
		return waitForElementDisplayWithRefresh(locator, timeOutInSeconds, false);
	}
	
	
	/**
	 * Wait for element to refresh on dynamic page
	 * @param locator
	 * @return WebElement (null if not found)
	 */
	public WebElement waitForElementRefresh(final By locator) {
		return waitForElementRefresh(locator, waitTime);
	}

	/**
	 * Wait for element to refresh on dynamic page
	 * @param locator
	 * @param timeOutInSeconds
	 * @return WebElement (null if not found)
	 */
	public WebElement waitForElementRefresh(final By locator, int timeOutInSeconds) {
		return waitForElementDisplayWithRefresh(locator, timeOutInSeconds, true);
	}

	/**
	 * Wait for element to be found and displayed.  Optionally will refresh the page each time
	 * @param locator
	 * @param timeOutInSeconds
	 * @param refreshEachIteration
	 * @return WebElement (null if not found)
	 */
	private WebElement waitForElementDisplayWithRefresh(final By locator, int timeOutInSeconds, boolean needsRefresh) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					if (needsRefresh) {
						driver.navigate().refresh();
					}
					return isElementPresentAndDisplay(locator);
				}
			});
			WebElement element = driver.findElement(locator);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}
	
	/**
	* Checks if the element is in the DOM and displayed.
	*
	* @param driver - The driver object to use to perform this element search
	* @param by - selector to find the element
	* @return boolean
	*/
	private Boolean isElementPresentAndDisplay(By locator) {
		try {
			return driver.findElement(locator).isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public WebElement fluentWait(final By locator) {
	    FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	            .withTimeout(60, TimeUnit.SECONDS)
	            .pollingEvery(5, TimeUnit.SECONDS)
	            .ignoring(NoSuchElementException.class);

	    WebElement element = wait.until(new Function<WebDriver, WebElement>() {
	        public WebElement apply(WebDriver webDriver) {
	            return driver.findElement(locator);
	        }
	    });
	    return element;
	}
	
	/**
	 * Wait until element is found and has any data (text, digits, underscore)
	 * @param locator
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementHasData(final By locator) {
		return waitUntilElementHasData(locator, waitTime);
	}

	/**
	 * Wait until element is found and has any data (text, digits, underscore)
	 * @param locator
	 * @param seconds
	 * @return WebElement (null if not found)
	 */
	public WebElement waitUntilElementHasData(final By locator, int seconds) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver d) {
					return isElementPresentAndHasData(locator);
				}
			});
			WebElement element = driver.findElement(locator);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}

	private Boolean isElementPresentAndHasData(final By locator) {
		try {
			String elementText = driver.findElement(locator).getText();
			System.out.println("**isElementPresentAndHasData: Waiting for any text, found: " +
				(elementText == null ? "nothing yet" : elementText));
			return elementText != null && elementText.matches(".*\\w+.*");
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * wait until element is found *and* has text matching expectedText
	 * @param locator
	 * @param expectedText
	 * @return WebElement (null if not found or not matching)
	 */
	public WebElement waitUntilElementFoundWithText(final By locator, String expectedText) {
		return waitUntilElementFoundWithText(locator, waitTime, expectedText);
	}

	/**
	 * wait until element is found *and* has text matching expectedText
	 * @param locator
	 * @param seconds
	 * @param expectedText
	 * @return WebElement (null if not found or not matching)
	 */
	public WebElement waitUntilElementFoundWithText(final By locator, int seconds, String expectedText) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver d) {
					return isElementFoundWithText(locator, expectedText);
				}
			});
			WebElement element = driver.findElement(locator);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}

	/**
	 * wait until element is found *and* has text matching expectedText
	 * @param locator
	 * @param expectedText
	 * @return WebElement (null if not found or not matching)
	 */
	public WebElement waitUntilElementFoundWithTextChanged(final By locator, String expectedText) {
		return waitUntilElementFoundWithText(locator, waitTime, expectedText);
	}

	/**
	 * wait until element is found *and* has text matching expectedText
	 * @param locator
	 * @param seconds
	 * @param expectedText
	 * @return WebElement (null if not found or not matching)
	 */
	public WebElement waitUntilElementFoundWithChanged(final By locator, int seconds, String expectedText) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver d) {
					return isElementFoundWithTextChanged(locator, expectedText);
				}
			});
			WebElement element = driver.findElement(locator);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}


	private boolean isElementFoundWithText(final By locator, String expectedText) {
		try {
			String text = driver.findElement(locator).getText();
			System.out.println("**isElementFoundWithText: Waiting for text " + expectedText +", found:" +
				(text == null ? "--nothing--" : text.trim()));
		 	return (text != null && text.trim().equals(expectedText));
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	private boolean isElementFoundWithTextChanged(final By locator, String expectedText) {
		try {
			String text = driver.findElement(locator).getText();
			System.out.println("**isElementFoundWithTextChanged: Waiting for text to differ from " + expectedText +", got:" +
				(text == null ? "--nothing--" : text.trim()));
		 	return (text != null && !text.trim().equals(expectedText));
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * Wait until element is found and select it.  Return element once it has been selected.
	 * @param locator
	 * @param seconds
	 * @return WebElement (null if not found and selected)
	 */
	public WebElement waitAndClickRadioButton(final By locator) {
		return waitAndClickRadioButton(locator, waitTime);
	}

	/**
	 * Wait until element is found and select it.  Return element once it has been selected.
	 * @param locator
	 * @param seconds
	 * @return WebElement (null if not found and selected)
	 */
	public WebElement waitAndClickRadioButton(final By locator, int seconds) {
		try {
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			WebDriverWait wait = new WebDriverWait(driver, seconds);
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
            			public Boolean apply(WebDriver d) {
					return hasElementBeenSelected(locator);
				}
			});
			wait.until(new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver d) {
					return isElementSelected(locator);
				}
			});
			WebElement element = driver.findElement(locator);
			driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
			return element;
		} catch (Exception e) {
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(12, TimeUnit.SECONDS);
		return null;
	}

	private boolean hasElementBeenSelected(final By locator) {
		try {
			WebElement element = driver.findElement(locator);
			if (element.isSelected()) {
				return true;
			} else if (element.isDisplayed() && element.isEnabled()) {
				element.click();
				return true;
			}
			return false;
                } catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isElementSelected(final By locator) {
		try {
			return driver.findElement(locator).isSelected();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Waits for URL to match partialUrl for up to seconds time
	 * @param partialUrl
	 * @param seconds
	 */
	public void waitForURL(String partialURL, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				String url = d.getCurrentUrl();
				System.out.println("**waitForURL: Waiting for URL to contain: " + partialURL);
				System.out.println("**waitForURL: Found: " + url);
				return url.contains(partialURL);
			}
		});
	}
	
	/**
	 * Waits for title to match Title Page for up to seconds time
	 * @param partialTitle
	 * @param seconds
	 */
	public void waitForTitle(String partialTitle, int seconds) {
		WebDriverWait wait = new WebDriverWait(driver, seconds);
		wait.until(new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver d) {
				String title = d.getTitle();
				System.out.println("**waitForTitle: Waiting for Title to contain: " + partialTitle);
				System.out.println("**waitForTitle: Found: " + title);
				return title.contains(partialTitle);
			}
		});
	}
	
	public static void maximizeBrowserWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}
}
