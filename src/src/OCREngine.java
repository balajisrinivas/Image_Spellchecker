import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sourceforge.tess4j.Tesseract;

public class OCREngine extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFrame frame=null;
	JLabel extractLabel=null, spellCheckedLabel=null,imageLabel=null,picLabel=null,picLabel1=null;
	JTextField extractedWord=null, spellCheckedText=null,imagePath=null;
	JButton spellCheck=null,display=null,extractText=null;
	BufferedImage image=null;
		
	public static JFrame mainframe;
	

	  public void printPixelARGB(int pixel) 
	  {
	    int alpha = (pixel >> 24) & 0xff;
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    System.out.println("argb: " + alpha + ", " + red + ", " + green + ", " + blue);
	  }
	
	public OCREngine()
	{
		final Container container=getContentPane();
        BoxLayout layout=new BoxLayout(container,BoxLayout.Y_AXIS);
        container.setLayout(layout);
        
		JPanel PimagePath;
		final JPanel Pimage;
		final JPanel Pextract;
		final JPanel Pspell;
		
		PimagePath=new JPanel();
		Pimage=new JPanel();
		Pextract=new JPanel();
		Pspell=new JPanel();
		
		//JTable table=new JTable(new DefaultTableModel());
	//	table
		imageLabel=new JLabel("Enter the path of the image file");
		imagePath=new JTextField(40);
		display=new JButton("Display image");
		
		picLabel=new JLabel();
		extractText=new JButton("Extract Text");
		
		extractLabel=new JLabel("Extracted word");
		extractedWord=new JTextField(20);
		extractedWord.setEditable(true);
		spellCheck=new JButton("Check Spelling");
		
		
		spellCheckedLabel=new JLabel("Correct Spelling");
		spellCheckedText=new JTextField(20);
		spellCheckedText.setEditable(false);
		
		PimagePath.add(imageLabel);
		PimagePath.add(imagePath);
		PimagePath.add(display);
		
		Pextract.add(extractLabel);
		Pextract.add(extractedWord);
		Pextract.add(spellCheck);
		
		Pspell.add(spellCheckedLabel);
		Pspell.add(spellCheckedText);
		
		container.add(PimagePath);
		display.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try 
				{
					String str=null;
				    //"image" is an object of BufferedImage
				    if((str=imagePath.getText()).isEmpty())
				    {
				    	JOptionPane.showMessageDialog(null,"Enter a valid imagepath","Enter a valid imagepath",JOptionPane.ERROR_MESSAGE);
				    }
				    else
				    {
				    	image = ImageIO.read(new File(imagePath.getText()));
				    	System.out.println(imagePath.getText());
				    	Pimage.add(picLabel1=new JLabel(new ImageIcon(image)));
					  
				    	//Using Canny's Edge Detection
				    	//create the detector 
				    	CannyEdgeDetector detector = new CannyEdgeDetector(); 
					  
				    	//adjust its parameters as desired 
				    	detector.setLowThreshold(0.5f); 
				    	detector.setHighThreshold(1f);
					  
				    	//apply it to an image 
				    	detector.setSourceImage(image); 
				    	detector.process(); 
				    	BufferedImage edges = detector.getEdgesImage();
					  
				    	int w=edges.getWidth();
					  	int h=edges.getHeight();
					  	System.out.println("width, height: " + w + ", " + h);
					  	for(int i=0;i<h;i++)
					  	{
					  		for(int j=0;j<w;j++)
						  	{
							    System.out.println("x,y: " + j + ", " + i);
					    		int pixel = image.getRGB(j, i);
					    		printPixelARGB(pixel);
					    		System.out.println("");
						  	}
					  	}
					  
				    	//adding edge-detected picture and extractText to Piamge
				    	Pimage.add(picLabel=new JLabel(new ImageIcon(edges)));  
				    	Pimage.add(extractText);
					  
				    	//adding all panels to container
				    	container.add(Pimage);
				    	container.add(Pextract);
				    	container.add(Pspell);
				    	revalidate();
				    	//mainframe.repaint();
				    }
			    }
				catch (IOException ex) 
				{
					ex.printStackTrace();
			    }
			}
		});
		extractText.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try 
				{                
					Tesseract instance = Tesseract.getInstance();
					String result = instance.doOCR(image);
		            extractedWord.setText(result);
			    }
				catch (Exception ex) 
				{
					ex.printStackTrace();
			    }
			}
		});
		spellCheck.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ae)
			{
				try 
				{
					String str=null;
					if((str=extractedWord.getText()).isEmpty())
					{
						JOptionPane.showMessageDialog(null,"Invalid word","Invalid word",JOptionPane.ERROR_MESSAGE);
					}
					else
					{
						JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
						String line = jazzySpellChecker.getCorrectedLine(extractedWord.getText());
						spellCheckedText.setText(line);
						BufferedImage newImage=new BufferedImage(200,70,BufferedImage.TYPE_INT_ARGB);
						BufferedImage replacedImage = ImageIO.read(new File("F:/SpellChecker/res/NewImage.jpg"));
						
						Graphics2D g2d=newImage.createGraphics();
						g2d.drawImage(replacedImage,0,0,null);
						FontMetrics fm=g2d.getFontMetrics();
						g2d.setFont(new Font("Calibri",Font.BOLD,50));
						
						g2d.setColor(Color.BLACK);
						int x=(newImage.getWidth()/2)-30;
						int y=newImage.getHeight()-20;
						g2d.drawString(line,x,y);
						g2d.dispose();	
						JPanel Presult=new JPanel();
						Presult.add(picLabel=new JLabel(new ImageIcon(newImage)));  
				    	
					  
				    	//adding panel to container
				    	container.add(Presult);
				    	revalidate();
					} 
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		}
		);
	}
	
	/*protected void paintComponent(Graphics g)
	{
	        //super.paintComponent(g);
	        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	}*/
	public static void main(String [] args) throws Exception
	{
		OCREngine frame=new OCREngine();
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
            	System.exit(0);
            }
        });
	}
}
