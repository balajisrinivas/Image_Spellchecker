import java.awt.Container;
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
	JLabel extractLabel=null, spellCheckedLabel=null,imageLabel=null,picLabel=null;
	JTextField extractedWord=null, spellCheckedText=null,imagePath=null;
	JButton spellCheck=null,display=null,extractText=null;
	BufferedImage image=null;
		
	public static JFrame mainframe;
    
	public OCREngine()
	{
		Container container=getContentPane();
        BoxLayout layout=new BoxLayout(container,BoxLayout.Y_AXIS);
        container.setLayout(layout);
        
		JPanel PimagePath,Pimage,Pextract,Pspell;
		
		PimagePath=new JPanel();
		Pimage=new JPanel();
		Pextract=new JPanel();
		Pspell=new JPanel();
		
		imageLabel=new JLabel("Enter the path of the image file");
		imagePath=new JTextField(40);
		display=new JButton("Display image");
		
		picLabel=new JLabel("Picture area");
		extractText=new JButton("Extract Text");
		
		extractLabel=new JLabel("Extracted word");
		extractedWord=new JTextField(20);
		extractedWord.setEditable(true);
		spellCheck=new JButton("Check Spelling");
		
		spellCheckedLabel=new JLabel("Correct Spelling");
		spellCheckedText=new JTextField(20);
		spellCheckedText.setEditable(false);
		
		/*image = ImageIO.read(new File(imagePath.getText()));
        System.out.println(imagePath.getText());
        picLabel = new JLabel(new ImageIcon(image));*/

		PimagePath.add(imageLabel);
		PimagePath.add(imagePath);
		PimagePath.add(display);
		
		Pimage.add(picLabel);
		Pimage.add(extractText);
		
		Pextract.add(extractLabel);
		Pextract.add(extractedWord);
		Pextract.add(spellCheck);
		
		Pspell.add(spellCheckedLabel);
		Pspell.add(spellCheckedText);
		
		container.add(PimagePath);
		container.add(Pimage);
		container.add(Pextract);
		container.add(Pspell);
		
		//frame.add(imageLabel);
		//frame.add(imagePath);
		//frame.add(display);
		//frame.add(extractText);
		//	frame.add(label);
		//frame.add(extractedWord);
		//frame.add(spellCheck);
		//frame.add(spellCheckedLabel);
		//frame.add(spellCheckedText);
		//panel.add(frame);
		display.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				try 
				{                
			          image = ImageIO.read(new File(imagePath.getText()));
			          System.out.println(imagePath.getText());
			          picLabel = new JLabel(new ImageIcon(image));
			          
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
		Pimage.add(picLabel);
		container.add(Pimage);
		spellCheck.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ae)
			{
				try 
				{
					JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
					String line = jazzySpellChecker.getCorrectedLine(extractedWord.getText());
					spellCheckedText.setText(line);
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
		mainframe=frame;
		
		frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent we)
            {
            System.exit(0);
            }
        });
	}
}
