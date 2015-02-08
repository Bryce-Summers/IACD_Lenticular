package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import util.ImageUtil;

import BryceImages.Operations.Ditherer;
import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
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


public class Hilbert
{

	private Graphics g;
	BufferedImage output;
	
	private BufferedImage[] images;

	private int index;	
	final static int frames = 60;
	
	// Must be of the form x*4 + 2, where x : [0, inf)
	final int path_density = 2*2*2*2;
	
	public static void main(String[] args)
	{
		System.out.println("Hilbert Curve lenticular.");
		for(int i = 0; i < frames; i++)
		{
			new Hilbert(256, i);
			System.out.println("Done " + i);
		}
	}
	
	// -- Constructor.
	public Hilbert(int dimension, int index)
	{
		this.index = index;
		
		int SIZE = dimension/path_density;
		
		setup(dimension, SIZE);
		
		int total_size = path_density*path_density;
		
		for(int i = 0; i < total_size; i++)
		{
			Box<Integer> x = new Box<Integer>();
			Box<Integer> y = new Box<Integer>();
			
			d2xy(path_density, i, x, y);
			
			drawImage(x.val*SIZE, y.val*SIZE);
		}
		
		ImageUtil.saveImage(output, "Output" + index + ".png");
	}
	
	private void setup(int dimension, int SIZE)
	{
		output = ImageFactory.blank(dimension, dimension);
		g = output.getGraphics();
		images = new BufferedImage[frames];
		
		for(int i = 0; i < frames; i++)
		{
			images[i] = ImageFactory.ColorRect(Colors.Color_hsv(0, 0, i*100/frames), SIZE, SIZE);
		}
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
	
	
	// draws the input image onto the output image.
	private void drawImage(int x, int y)
	{
		index = (index + 1) % images.length;
		BufferedImage image = images[index];
		
		
		g.drawImage(image, x, y, null);
	}

}


