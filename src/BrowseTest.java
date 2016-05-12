import static org.junit.Assert.*;

import org.junit.Test;

public class BrowseTest {

	/**
	 * Following test uses a number of Coupled Methods from Browser.java
	 * to test whether the Browser was successfully able to render the 
	 * Web page.
	 */
	@Test
	public void test() {
		
		// url of web page to be rendered
		String []args = {"http://www.google.com"};
		
		// Initialize the browser and 
		// render the web page at http://www.google.com
		Browser.main(args);
		
		// check is rendering was successful
		assertTrue(Browser.isSuccessfullyRendered());
	}

}
