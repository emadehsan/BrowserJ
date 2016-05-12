/**
 * @reference https://github.com/radkovo/CSSBox/blob/master/src/main/java/org/fit/cssbox/demo/SimpleBrowser.java
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.BrowserCanvas;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The main class reponsible for initiating the browser and  
 * @author Emad Ehsan
 *
 */
public class Browser extends JFrame 
{
//	private static final long serialVersionUID = -1336331141597077348L;
	private static final long serialVersionUID = -1347681263597077348L;
	
	// canvas to display rendered document
    private JPanel browserCanvas;
    
    // Scroll Pane for the canvas
    private JScrollPane documentScroll;
    
    // Root DOM Element of the document body 
	private Element docroot;
	
	/** The CSS analyzer of the DOM tree */
    private DOMAnalyzer decoder;

    private static boolean successfullyRendered = false;
    
    private static String url = "";
    
    /** 
     * Creates a new application window and displays the rendered document
     * @param root The root DOM element of the document body
     * @param baseurl The base URL of the document used for completing the relative paths
     * @param decoder The CSS analyzer that provides the effective style of the elements 
     */
    public Browser(Element root, URL baseurl, DOMAnalyzer decoder)
    {
        docroot = root;
        this.decoder = decoder;
        initComponents(baseurl);
    }
    
    /**
     * Creates and initializes the GUI components
     * @param baseurl The base URL of the document used for completing the relative paths
     */
    private void initComponents(URL baseurl) 
    {
        documentScroll = new javax.swing.JScrollPane();
        
        //Create the browser canvas
        browserCanvas = new BrowserCanvas(docroot, decoder, new Dimension(1000, 600), baseurl);
        
        /*
         * event handling to further improve the browser
         * as new functionality; like menu is added;
         */
        browserCanvas.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e)
            {
                System.out.println("Click at: " + e.getX() + ":" + e.getY());
            }
            public void mousePressed(MouseEvent e) {
            	System.out.println("Pressed at: " + e.getX() + ":" + e.getY());
            }
            public void mouseReleased(MouseEvent e) {
            	System.out.println("Released at: " + e.getX() + ":" + e.getY());
            }
            public void mouseEntered(MouseEvent e) {
            	System.out.println("Mouse entered at: " + e.getX() + ":" + e.getY());
            }
            public void mouseExited(MouseEvent e) {
            	System.out.println("Exited at: " + e.getX() + ":" + e.getY());
            }
        });
        
        getContentPane().setLayout(new GridLayout(1, 0));

        setTitle("Custom Browser");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                exitForm(evt);
            }
        });

        documentScroll.setViewportView(browserCanvas);
        getContentPane().add(documentScroll);
        pack();
        
        setSuccessfullyRendered(true);
    }
    
    /** 
     * Exit the Application 
     */
    private void exitForm(WindowEvent evt)
    {
        System.exit(0);
    }
    
    /**
     * Whether or not the browser was able to render the Web page.
     * @param val Boolean
     */
    private static void setSuccessfullyRendered(boolean val) {
    	successfullyRendered = val;
    }
    
    /**
     * Check if rendering was successful?
     * @return boolean
     */
    public static boolean isSuccessfullyRendered() {
    	return successfullyRendered;
    }
    
    public static void main(String args[]) 
    {
    	if (args.length != 0 ) {
    		url = args[0];
    	}
    	else {
        	Scanner scan = new Scanner(System.in);
        	System.out.print("Enter Website url: ");
        	url = scan.next();
    	}
        try {
        	
        	System.out.println("Initialzing browser please wait...");
        	
            //Open the network connection 
            DocumentSource docSource = new DefaultDocumentSource(url);
            
            //Parse the input document
            DOMSource parser = new DefaultDOMSource(docSource);
            Document doc = parser.parse();
            
            //Create the CSS analyzer
            DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
            da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
            da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
            da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
            da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
            da.getStyleSheets(); //load the author style sheets
            
            //Display the result
            Browser test = new Browser(da.getRoot(), docSource.getURL(), da);
            test.setSize(1275, 750);
            test.setVisible(true);
            
            docSource.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
