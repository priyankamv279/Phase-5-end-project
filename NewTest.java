package com.example;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NewTest {
	WebDriver wd=null;
	long startTime;
	@BeforeTest
	public void intiate() {
		
		System.out.println("config intiated on chrome");
		// Example for chrome browser
		//register the webdriver =>browser vendor 
		WebDriverManager.chromedriver().setup();
		//creating an object to the object
		wd=new ChromeDriver();
		//maximize the browser
		wd.manage().window().maximize();
		// Set the page load timeout
		wd.manage().timeouts().pageLoadTimeout(5000,TimeUnit.MILLISECONDS);
		
//		System.out.println("config intiated on edge");
//		// Example for edge browser
//		//register the webdriver =>browser vendor 
//		WebDriverManager.edgedriver().setup();
//		//creating an object to the object
//		wd=new EdgeDriver();
//		//maximize the browser
//		wd.manage().window().maximize();
//		// Set the page load timeout
//		wd.manage().timeouts().pageLoadTimeout(5000,TimeUnit.MILLISECONDS);

	}

	@Test(priority = 1)
	public void testurl() {
		System.out.println("test intiated: flipkart Automate");
		startTime = System.currentTimeMillis(); // Capturing start time

		//url naviagte
		wd.get("https://www.flipkart.com/");

		//search the item
		wd.findElement(By.xpath("//*[@id=\"container\"]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div/div[1]/div[1]/header/div[1]/div[2]/form/div/div/input")).sendKeys("iphone 13");
		wd.findElement(By.xpath("//*[@id=\"container\"]/div/div[1]/div/div/div/div/div[1]/div/div[1]/div/div[1]/div[1]/header/div[1]/div[2]/form/div/button")).click();

		// Scroll to the bottom of the page to load all images
		JavascriptExecutor js = (JavascriptExecutor) wd;
		long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
		int scrollCounter = 0; // Counter to track scroll frequency

		while (true) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
			scrollCounter++;

			// Wait for a certain time to ensure images are loaded 
			try {
				Thread.sleep(5000); // Waiting for 5 seconds (adjust according to your page load time)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long newHeight = (long) js.executeScript("return document.body.scrollHeight");
			if (newHeight == lastHeight) {
				break;
			}
			lastHeight = newHeight;
		}   
	    System.out.println("Content was refreshed " + scrollCounter + " times while scrolling.");

		// Wait explicitly for image elements to be present or visible
		WebDriverWait wait = new WebDriverWait(wd, 40); // Adjust timeout as needed
		List<WebElement> images = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("img")));

		// Check if each image is visible within the screen height
		for (WebElement image : images) {
			if (isElementInViewPort(image)&& isImageDownloaded(image)) {
				System.out.println("Image is visible within the screen height and downloaded");
			} else {
				System.out.println("Image is not fully visible within the screen height or not downloaded");
			}
		}
		long endTime = System.currentTimeMillis(); // Capturing end time
		long pageLoadTime = endTime - startTime; // Calculating page load time
		System.out.println("Page load time: " + pageLoadTime + " milliseconds");
		System.out.println("test completed");

	}

	@AfterTest
	public void  derefer() {
		System.out.println("wd closed");
		wd.close();
	}

	// Function to check if an element is in the viewport
	public boolean isElementInViewPort(WebElement element) {
		return (Boolean) ((JavascriptExecutor) wd).executeScript(
				"var rect = arguments[0].getBoundingClientRect();" +
						"return (rect.top >= 0 && rect.bottom <= window.innerHeight)"
						, element);
	}
	
	// Function to check if an image is downloaded
	public boolean isImageDownloaded(WebElement imageElement) {
	    return (Boolean) ((JavascriptExecutor) wd).executeScript(
	            "return arguments[0].complete && typeof arguments[0].naturalWidth !== 'undefined' && arguments[0].naturalWidth > 0"
	            , imageElement);
	}
	
}
