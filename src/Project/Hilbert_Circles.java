package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

import com.sun.imageio.plugins.gif.GIFImageWriter;

import util.ImageUtil;

import BryceImages.Operations.Ditherer;
import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import BryceMath.Calculations.MathB;
import Data_Structures.Structures.Box;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * A lenticular animation by Bryce Summers.
 * 
 * Programmed on 2/2/2015.
 * 
 * Requires the Bryce Code base to compile correctly.
 * https://github.com/Bryce-Summers/Bryce-Summers-Java-Codebase
 */


public class Hilbert_Circles
{

	private Graphics g;
	public BufferedImage output;
	
	//private BufferedImage[] images;

	private int index;	
	final static int frames = 10;
	
	// Must be of the form x*4 + 2, where x : [0, inf)
	final int path_density = 2*2*2*2;
	
	int SIZE;
	
	double color_index = 0.0;
	int color_num = 10;
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		System.out.println("Hilbert Curve lenticular.");
				
		// Generate the images.
		for(int i = 0; i < frames; i++)
		{
			Hilbert_Circles hilbert = new Hilbert_Circles(512, i);
			ImageUtil.saveImage(hilbert.output, "Output" + i + ".png");
			System.out.println("Done " + i);
		}
		
	    ImageOutputStream output = new FileImageOutputStream(new File("GIF-Output.gif"));
		BufferedImage firstImage = ImageIO.read(new File("Output" + 0 + ".png"));
		GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), 60, true);
		
	    writer.writeToSequence(firstImage);
	    for(int i=1; i < frames; i++) {
	      BufferedImage nextImage = ImageIO.read(new File("Output" + i + ".png"));
	      writer.writeToSequence(nextImage);
	    }

	    writer.close();
	    output.close();
	    
	    System.out.println("Final GIF is complete.");
	}
	
	// -- Constructor.
	public Hilbert_Circles(int dimension, int index)
	{
		this.index = index;
		
		color_index = .01*index;
		
		SIZE = dimension/path_density;
		
		setup(dimension, SIZE);
		
		int total_size = path_density*path_density;
		
		// Entering Circle.
		drawImage(-SIZE, 0, 0, 0);
		
		// Exiting.
		drawImage(SIZE*(path_density-1), 0, SIZE*path_density, 0);
		
		for(int i = 0; i < total_size - 1; i++)
		{
			Box<Integer> x = new Box<Integer>();
			Box<Integer> y = new Box<Integer>();
			
			d2xy(path_density, i, x, y);
			
			Box<Integer> x2 = new Box<Integer>();
			Box<Integer> y2 = new Box<Integer>();
			
			d2xy(path_density, i+1, x2, y2);
			
			drawImage(x.val*SIZE, y.val*SIZE, x2.val*SIZE, y2.val*SIZE);
		}

	}
	
	private void setup(int dimension, int SIZE)
	{
		output = ImageFactory.ColorRect(Color.WHITE, dimension, dimension);
		g = output.getGraphics();
				
		/*
		images = new BufferedImage[frames];
		
		for(int i = 0; i < frames; i++)
		{
			images[i] = ImageFactory.ColorRect(Colors.Color_hsv(0, 0, i*100/frames), SIZE, SIZE);
		}
		*/
	}
	
	//convert (x,y) to d
	int xy2d (int n, int x, int y) {
	    int rx, ry, s, d=0;
	    for (s=n/2; s>0; s/=2) {
	        rx = (x & s) > 0 ? 1 : 0;
	        ry = (y & s) > 0 ? 1 : 0;
	        d += s * s * ((3 * rx) ^ ry);
	        
	        Box<Integer> box_x = new Box<Integer>();
	        Box<Integer> box_y = new Box<Integer>();
	        
	        rot(s, box_x, box_y, rx, ry);
	        
	        x = box_x.val;
	        y = box_y.val;	        
	        
	    }
	    return d;
	}
	 
	//convert d to (x,y)
	void d2xy(int n, int d, Box<Integer> x, Box<Integer> y)
	{
	    int rx, ry, s, t=d;
	    x.val = y.val = 0;
	    
	    for (s=1; s<n; s*=2) {
	        rx = 1 & (t/2);
	        ry = 1 & (t ^ rx);
	        rot(s, x, y, rx, ry);
	        x.val += s * rx;
	        y.val += s * ry;
	        t /= 4;
	    }
	}
	 
	//rotate/flip a quadrant appropriately
	void rot(int n, Box<Integer> x, Box<Integer> y, int rx, int ry)
	{
	    if (ry == 0) {
	        if (rx == 1)
	        {
	            x.val = n-1 - x.val;
	            y.val = n-1 - y.val;
	        }
	 
	        //Swap x and y
	        int t  = x.val;
	        x.val = y.val;
	        y.val = t;
	    }
	}
	
	int obj_count = 0;
	double EPSILON = .00001;
	
	//------------------------------------------------------------------
	double function_DoubleEllipticOgee (double x, double a, double b){
	 

	  double min_param_a = 0.0 + EPSILON;
	  double max_param_a = 1.0 - EPSILON;
	  a = MathB.bound(a, min_param_a, max_param_a); 
	  double y = 0;

	  if (x<=a){
	    y = (b/a) * Math.sqrt(MathB.sqr(a) - MathB.sqr(x-a));
	  } 
	  else {
	    y = 1.0 - ((1.0-b)/(1.0-a))*Math.sqrt(MathB.sqr(1.0-a) - MathB.sqr(x-a));
	  }
	  return y;
	}
	
	
	// draws the input image onto the output image.
	private void drawImage(int x1, int y1, int x2, int y2)
	{
		color_index += .1;
		
		obj_count++;
		
		double per2 = 1.0*((index)% frames)/frames;
		
		int count = obj_count%10;
		
		// Modify the easing curve.

		// -- Used to create an irregularity in the design.		
		//per2 = function_DoubleEllipticOgee(per2, .25 + .5*count/10.0, .5*count/10.0);
		
		per2 = .5 - .5*Math.cos(Math.PI*per2);
						
		double per1 = 1.0 - per2;
					
		//int x = x1 - x1*index/frames + x2*index/frames;
		//int y = y1 - y1*index/frames + y2*index/frames;
		
		int x = (int)(x1*per1 + x2*per2);
		int y = (int)(y1*per1 + y2*per2);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Colors.Color_hsv(0, 0, (int)(30 + 30*Math.cos(color_index*Math.PI*2))));
		
		g.fillOval(x + SIZE/3, y + SIZE/3, SIZE*2/3, SIZE*2/3);
	}

}


