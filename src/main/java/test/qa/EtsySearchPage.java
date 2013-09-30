package test.qa;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class EtsySearchPage extends WebDriverComponent<EtsySearchPage> {

	public ESPFluentInterface espfi;
	public static final String searchFieldName = "search_query";
	public static final String searchButtonName = "search_submit";
	public static final String suggestIons = "div.nav-search-text div#search-suggestions ul li";
	@FindBy(name = searchFieldName ) public WebElement searchField;
	@FindBy(name = searchButtonName ) public WebElement searchButton;

	public EtsySearchPage() {
		super();
		this.get();
		log.info("EtsySearchPage constructor...");
		espfi = new ESPFluentInterface( this ); // use this only if you want to
	}

	/* Override method in WebDriverComponent class. */
	@Override
	protected void isLoaded() throws Error {    	
		log.info("EtsySearchPage.isLoaded()...");
		boolean loaded = false;
		if ( !(searchField == null ) ) {
			try {
				if ( searchField.isDisplayed() ) {
					loaded = true;
				}
			} catch ( ElementNotVisibleException e ) {
				log.info( "Element may not be displayed yet." );
			}
		}
		Assert.assertTrue( loaded, "Etsy search field is not yet displayed." );
	}

	/* Override method in WebDriverComponent class. */
	@Override
	protected void load() {
		log.info("EtsySearchPage.load()...");
		PageFactory.initElements( driver, this ); // initialize WebElements on page
		sleepSeconds(2);
	}

	public void clickSearchButton() {
		if ( searchButton == null ) {
			searchButton = getElementByLocator( By.id( searchButtonName ) );
		} else {
			try {
				searchButton.click();
			} catch ( ElementNotVisibleException e ) {
				log.info( "Element not visible exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				log.info( "Exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			}
		}
	}

	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void setSearchString( String sstr ) {
		clearAndType( searchField, sstr );
	}

	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void clickEtsyLogo() {
		log.info("Click Etsy logo...");
		WebElement logo = null;
		By locator = By.cssSelector( "h1#etsy a" );
		logo = getElementByLocator( locator );
		logo.click();
		sleepSeconds(2);
	}

	/**
	 * Method: withFluent
	 * Entrypoint for an object that can start a fluent action thread.
	 * @return	ESPFluentInterface
	 * @throws	null
	 */
	public ESPFluentInterface withFluent() {
		return espfi; 
	}	

	/**
	 * Method: selectInEtsyDropdown
	 * Selects element in dropdown using keydowns method (just for fun)
	 * as long as you typed a string first.  The thread sleeps and the 
	 * key arrow down are safe to comment out within the below block.
	 * @return	void
	 * @throws	StaleElementReferenceException
	 */
	public void selectInEtsyDropdown( String match ) {
		log.info("Selecting \"" + match + "\" from Etsy dynamic dropdown.");
		List<WebElement> allSuggestions = driver.findElements( By.cssSelector( suggestIons ) );  
		try {
			for ( WebElement suggestion : allSuggestions ) {
				Thread.sleep(600);
				searchField.sendKeys( Keys.ARROW_DOWN); // show effect of selecting item with keyboard arrow down
				if ( suggestion.getText().contains( match ) ) {
					suggestion.click();
					log.info("Found item and clicked it.");
					sleepSeconds(2); // just to slow it down so human eyes can see it
					break;
				}
			}
		} catch ( StaleElementReferenceException see ) {
			log.info("Error while iterating dropdown list:\n" + see.getMessage() );
		} catch ( InterruptedException ie ) {
			ie.printStackTrace();
		}
		log.info("Finished select in Etsy dropdown.");
	}

	/**
	 *  Inner class
	 *  A fluent API interface that provides methods for calling normal
	 *  page object methods within this class. 
	 */	
	public class ESPFluentInterface {

		public ESPFluentInterface(EtsySearchPage EtsySearchPage) {
			log.info("Initialized fluent interface.");
		}

		public ESPFluentInterface clickLogo() {
			clickEtsyLogo();
			return this;
		}

		public ESPFluentInterface clickSearchButton() {
			searchButton.click();
			return this;
		}

		public ESPFluentInterface clickSearchField() {
			searchField.click();
			return this;
		}

		public ESPFluentInterface selectItem( String match ) {
			log.info("Selecting item in list using fluent API.");
			selectInEtsyDropdown( match );
			return this;
		}

		public ESPFluentInterface setSearchString( String sstr ) {
			clearAndType( searchField, sstr );
			return this;
		}		

		public ESPFluentInterface waitForSeconds( int units ) {
			sleepSeconds(units);
			return this;
		}

	}

}
