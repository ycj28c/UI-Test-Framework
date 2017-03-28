package io.ycj28c.uitest.framework;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Static methods around web elements
 */
public class WebElements {
	/**
	 * wait and click the element
	 * @param driver
	 * @param element
	 * @param seconds
	 */
	public static void waitAndClick(WebDriver driver, WebElement element, int seconds) {
		(new WebDriverWait(driver, seconds)).until(ExpectedConditions.elementToBeClickable(element));
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		element.click();
	}
	
	/**
	 * wait and click the element(By)
	 * @param driver
	 * @param by
	 * @param seconds
	 */
	public static void waitAndClick(WebDriver driver, By by, int seconds) {
		(new WebDriverWait(driver, seconds)).until(ExpectedConditions.elementToBeClickable(by));
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.findElement(by).click();
	}

	/**
	 * wait until the element found
	 * @param driver
	 * @param element
	 * @param seconds
	 */
	public static WebElement waitUntilElementFound(WebDriver driver, WebElement element, int seconds) {
		(new WebDriverWait(driver, seconds)).until(
				ExpectedConditions.visibilityOf(element)
		);
		return element;
	}
	
	/**
	 * wait until all elements found
	 * @param driver
	 * @param locator
	 * @param seconds
	 * @return
	 */
	public static List<WebElement> waitUntilAllElementsFound(WebDriver driver, List<WebElement> elements, int seconds) {
		(new WebDriverWait(driver, seconds)).until(
				ExpectedConditions.visibilityOfAllElements(elements)
		);
		return elements;
	}
	
	/**
	 * wait until element found
	 * @param driver
	 * @param locator
	 * @param seconds
	 * @return
	 */
	public static WebElement waitUntilElementFound(WebDriver driver, By locator, int seconds) {
		(new WebDriverWait(driver, seconds)).until(
				ExpectedConditions.visibilityOfElementLocated(locator)
		);
		return driver.findElement(locator);
	}
	
	/**
	 * wait until all elements found
	 * @param driver
	 * @param locator
	 * @param seconds
	 * @return
	 */
	public static List<WebElement> waitUntilAllElementsFound(WebDriver driver, By locators, int seconds) {
		(new WebDriverWait(driver, seconds)).until(
				ExpectedConditions.visibilityOfAllElementsLocatedBy(locators)
		);
		return driver.findElements(locators);
	}

	/**
	 * wait until element has data
	 * @param driver
	 * @param element
	 * @param seconds
	 */
	public static void waitUntilElementHasData(WebDriver driver, WebElement element, int seconds) {
		(new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
            	String elementText = element.getText();
            	System.out.println("Waiting for any text, have " +
            			(elementText == null ? "nothing yet" : elementText));
            	return elementText != null && elementText.matches(".*\\w+.*");
            }
        });
	}

	/**
	 * Waits until an element is found *and* it has the expected text contained in it
	 * @param driver
	 * @param element
	 * @param seconds
	 * @param expectedText
	 */
	public static void waitUntilElementFoundWithText(
			WebDriver driver, WebElement element, int seconds, String expectedText) {
		 (new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
			 public Boolean apply(WebDriver d) {
				 	String text = element.getText();
				 	System.out.println("Waiting for text " + expectedText +", got:" +
				 			(text == null ? "--nothing--" : text.trim()));
				 	return (text != null && text.trim().equals(expectedText));
             }
         });
    }
	
	/**
	 * Waits until an element is found *and* it has *some* text in it
	 * @param driver
	 * @param element
	 * @param seconds
	 */
	public static void waitUntilElementFoundText(WebDriver driver, WebElement element, int seconds) {
		(new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				String text = element.getText();
				return (text != null && !text.isEmpty() && !text.trim().isEmpty());
			}
		});
	}

	/**
	 * wait and click the radio button
	 * @param driver
	 * @param element
	 * @param seconds
	 */
	public static void waitAndClickRadioButton(WebDriver driver, WebElement element, int seconds) {
		(new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                if (element.isDisplayed() && element.isEnabled()) {
                        element.click();
                        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
                }
                return element.isSelected();
            }
		});
	}
	
	/**
	 * wait until get the url
	 * @param driver
	 * @param partialURL
	 * @param seconds
	 */
	public static void waitForURL(WebDriver driver, String partialURL, int seconds) {
		(new WebDriverWait(driver, seconds)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getCurrentUrl().contains(partialURL);
            }
		});
	}

	/**
	 * max the browser windows
	 * @param driver
	 */
	public static void maximizeBrowserWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}
	
	/**
	 * mouse over the element
	 * @param driver
	 * @param element
	 */
	public static void mouseOver(WebDriver driver, WebElement element){
		//Firefox and IE require multiple cycles, more than twice, to cause a hovering effect
		for (int i = 0; i < 5; i++) {
			Actions builder = new Actions(driver);
			builder.moveToElement(element).build().perform();
		}
	}
	
	/**
	 * mouse over the element
	 * @param driver
	 * @param element
	 */
	public static void mouseOver(WebDriver driver, By locator){
		//Firefox and IE require multiple cycles, more than twice, to cause a hovering effect
		for (int i = 0; i < 5; i++) {
			Actions builder = new Actions(driver);
			builder.moveToElement(driver.findElement(locator)).build().perform();
		}
	}
	
	/**
	 * scroll to the element
	 * @param driver
	 * @param locator
	 */
	public static void scrollToElement(WebDriver driver, By locator){
		WebElement elementPosition = driver.findElement(locator);
		Coordinates coordinate = ((Locatable)elementPosition).getCoordinates(); 
		coordinate.onPage(); 
		coordinate.inViewPort();
	}
}