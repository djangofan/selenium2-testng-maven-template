package test.qa;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import au.com.bytecode.opencsv.CSVReader;

public class EtsyTest2 extends WebDriverUtilities {

	private static String testName, searchString, ddMatch;

	public EtsyTest2( String tName, String sString, String dMatch ) {
		super();
		testName = tName;
		searchString = sString;
		ddMatch = dMatch;
	}

	@BeforeTest
	public void setUp() {	
		if ( driver == null ) initializeRemoteBrowser( System.getProperty("browser"), 
				  System.getProperty("hubIP"), 
				  Integer.parseInt( System.getProperty("hubPort") ) );
		log.info("Finished setUp EtsyTest2");
	}

	@SuppressWarnings("resource")
	@DataProvider(name = "DataProvider2")
	public static String[][] loadTestsFromFile2() {
		log.info("Loading test data for test EtsyTest2...");
		CSVReader reader = null;
		try {
			reader = new CSVReader( new FileReader("testdata2.csv") );
		} catch ( FileNotFoundException fnfe ) {
			fnfe.printStackTrace();
		}
		List<String[]> rowlist = null;
		try {
			rowlist = reader.readAll();
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
		return (String[][]) rowlist.toArray();
	}  

	@Test(dataProvider = "DataProvider2")
	public void testWithPageObject() {
		log.info( testName + " being run..." );
		driver.get( System.getProperty("testURL") );
		EtsySearchPage gs = new EtsySearchPage();
		gs.setSearchString( searchString );
		gs.selectInEtsyDropdown( ddMatch );  
		gs.clickSearchButton();
		sleepSeconds(2);
		gs.clickEtsyLogo(); // click Etsy logo
		log.info("Page object test " + testName + " is done." );
	}

	@Test(dataProvider = "DataProvider2")
	public void testFluentPageObject() {    	
		log.info( testName + " being run..." );
		driver.get( System.getProperty("testURL") );
		EtsySearchPage esp = new EtsySearchPage();
		esp.withFluent().clickSearchField()
		.setSearchString( searchString ).waitForSeconds(2)
		.selectItem( ddMatch ).clickSearchButton()
		.waitForSeconds(2).clickLogo(); //click Google logo
		log.info("Fluent test " + testName + " is done." );
	}

	@AfterTest
	public void cleanUp() {
		log.info("Finished cleanUp EtsyTest2");
		driver.get("about:about");
		sleepSeconds(2);
	}
	
	@AfterClass
	public static void tearDown() {
		log.info("Finished tearDown");
		driver.quit();
	}
	
}
