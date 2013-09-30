package test.qa;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.Assert;

public abstract class WebDriverUtilities {
	
	public static RemoteWebDriver driver;
    public static Logger log = Logger.getLogger( WebDriverComponent.class.getName() );
    
    WebDriverUtilities() {
    	log.debug( "Calling constructor for WebDriverUtilities ..." );
		log.setLevel(Level.INFO);
    }
    
    public static File loadResource( String fileName ) {
		File junitFile = new File( fileName );
		if ( junitFile.exists() ) {
			log.info( "The file '" + junitFile.getAbsolutePath() + "' exists." );
		} else {
			log.info( "Problem loading Gradle resource: " + junitFile.getAbsolutePath() );
		}	
		return junitFile;
	}
    
    public static void initializeRemoteBrowser( String type, String host, int port ) {
		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability( "takesScreenshot", true );
		dc.setCapability( "webdriver.remote.quietExceptions", false );
		driver = (RemoteWebDriver)new Augmenter().augment(driver);
		try {
			if ( type.equalsIgnoreCase( "firefox" ) ) {
				dc.setBrowserName("firefox");
				driver = new RemoteWebDriver( new URL("http://" + host + ":" + port + "/wd/hub"), dc );

			} else if ( type.equalsIgnoreCase( "ie" ) ) {
				dc.setBrowserName("internetExplorer");
				driver = new RemoteWebDriver( new URL("http://" + host + ":" + port + "/wd/hub"), dc );
			}
			else if ( type.equalsIgnoreCase( "chrome" ) ) {
				dc.setBrowserName("chrome");
				driver = new RemoteWebDriver( new URL("http://" + host + ":" + port + "/wd/hub"), dc );
			} else {
				log.info( "Invalid browser type. Cannot initialize." );
			}
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
	}

	public static void clearAndSetValue( WebElement field, String text ) { 
		field.clear(); 
		field.sendKeys( Keys.chord(Keys.CONTROL, "a" ), text ); 
	}

	public static void clearAndType( WebElement field, String text ) {
		field.submit();
		field.clear(); 
		field.sendKeys( text ); 
	}
	
	public static void assertEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);
	}

	public static void assertTrue(Boolean condition) {
		Assert.assertTrue(condition);
	}

	public static void assertFalse(Boolean condition) {
		Assert.assertFalse(condition);
	}

	public static void sleepSeconds(int d) {
		try { Thread.sleep(d * 1000); } catch ( InterruptedException ie ) { /* do nothing on error */ }
	}

	public static boolean checkRegexp(String regexp, String text) {
		Pattern p = Pattern.compile(regexp);
		Matcher m = p.matcher(text);
		return m.find();
	}	

	public static WebElement getElementByLocator( By locator ) {
        Wait<WebDriver> wait = new FluentWait<WebDriver>( driver )
		    .withTimeout(30, TimeUnit.SECONDS)
		    .pollingEvery(5, TimeUnit.SECONDS)
		    .ignoring( NoSuchElementException.class, StaleElementReferenceException.class );
        WebElement we = wait.until( ExpectedConditions.presenceOfElementLocated( locator ) );
        //WebElement we = wait.until( ExpectedConditions.visibilityOfElementLocated( locator ) );
		return we;
    }

	public static void isTextPresent(String text) {
		driver.findElement(By.xpath("//*[contains(.,'" + text + "')]"));
	}

	public static void isTextNotPresent(String text) {
		boolean found = true;
		try {
			driver.findElement(By.xpath("//*[contains(.,'" + text + "')]"));
		} catch (Exception e) {
			found = false;
		} finally {
			assertFalse(found);
		}
	}

	public static String[][] tableData(List<WebElement> rows) {
		String[][] tablearr = new String[rows.size()][];
		int rown;
		int celln;
		rown = 0;
		for (WebElement row : rows) {
			rown++;
			List<WebElement> cells = row.findElements( By.tagName("td") );
			tablearr[rown - 1] = new String[cells.size()];
			celln = 0;
			for (WebElement cell : cells) {
				celln++;
				tablearr[rown - 1][celln - 1] = cell.getText();
			}
		}
		return tablearr;
	}

	public static String executeJS(String code) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return (String) js.executeScript(code);
	}

	public static void executeJS_no_return(String code) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(code);
	}

	public String getAlertText() {
		Alert alert = driver.switchTo().alert();
		return alert.getText();
	}

	public void acceptAlert() {
		Alert alert = driver.switchTo().alert();
		alert.accept();
	}

	public void dismissAlert() {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	public void sendKeysAlert(String str) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(str);
	}

	public void switchToInnerFrame(String name) {
		driver.switchTo().frame(name);
	}

	public void switchToRootFrame() {
		driver.switchTo().defaultContent();
	}
	
}
