package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import util.ImageUtil;

import BryceImages.Operations.Ditherer;
import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;

/*
 * A lenticular animation by Bryce Summers.
 * 
 * Programmed on 2/2/2015.
 * 
 * Requires the Bryce Code base to compile correctly.
 * https://github.com/Bryce-Summers/Bryce-Summers-Java-Codebase
 */


public class Lenticular
{

	private Graphics g;
	
	private BufferedImage[] images;

	private int index;
	
	final static int frames = 10;
	// Must be of the form x*4 + 2, where x : [0, inf)
	final int path_density = 1*4 + 2;
	
	public static void main(String[] args)
	{
		for(int i = 0; i < frames; i++)
		{
			new Lenticular(1500, i);
		}
	}
	
	private enum Direction{LEFT, UP, RIGHT, DOWN};
	// Upper left, upper right, bottom left, bottom right;
	private enum Location {UL, UR, BL, BR};
	
	public Lenticular(int dimension, int index)
	{
		this.index = index;
		
		BufferedImage output = ImageFactory.blank(dimension, dimension);
		g = output.getGraphics();
		
		
		int path_length = path_density*path_density;
		
		int SIZE = dimension/path_density;
		
		images = new BufferedImage[frames];
		
		for(int i = 0; i < frames; i++)
		{
			images[i] = ImageFactory.ColorRect(Colors.Color_hsv(0, 0, i*100/frames), SIZE, SIZE);
		}
		
		int x = dimension/2 - SIZE;
		int y = dimension/2 - SIZE;
		
		// Stores which direction the path is going.
		Direction dir = Direction.RIGHT;
		Location  loc = Location.UL;
			
		
		int iter_per_side = 1;
		int iters_left_on_side = 1;
		
		for(int i = 0; i < path_length; i++)
		{
						
			// Pattern drawing cases.
			if(dir == Direction.RIGHT && loc == Location.UL)
			{
				drawImage(x, y);
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
				
				loc = Location.UL;
				
				x += SIZE*2;				
			}
			
			else if(dir == Direction.RIGHT && loc == Location.UR)
			{
				drawImage(x + SIZE, y);
				drawImage(x, y);
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);
			
				x += SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.RIGHT && loc == Location.BR)
			{
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);
				drawImage(x, y);
				drawImage(x + SIZE, y);
							
				x += SIZE*2;
				loc = Location.UL;
			}
			
			else if(dir == Direction.RIGHT && loc == Location.BL)
			{
				drawImage(x, y + SIZE);
				drawImage(x, y);
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);
											
				x += SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.DOWN && loc == Location.UL)
			{
				drawImage(x, y);
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);				
											
				y += SIZE*2;
				loc = Location.UL;
			}
			
			else if(dir == Direction.DOWN && loc == Location.UR)
			{
				drawImage(x + SIZE, y);
				drawImage(x, y);
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);

				y += SIZE*2;
				loc = Location.UR;
			}
			
			else if(dir == Direction.DOWN && loc == Location.BR)
			{
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
				drawImage(x, y);
				drawImage(x, y + SIZE);				

				y += SIZE*2;
				loc = Location.UL;
			}
			
			else if(dir == Direction.DOWN && loc == Location.BL)
			{
				drawImage(x, y + SIZE);
				drawImage(x, y);
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);				

				y += SIZE*2;
				loc = Location.UR;
			}
			
			else if(dir == Direction.LEFT && loc == Location.UL)
			{
				drawImage(x, y);
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);				
								

				x -= SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.LEFT && loc == Location.UR)
			{
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);
				drawImage(x, y);					
								
				x -= SIZE*2;
				loc = Location.UR;
			}
			
			else if(dir == Direction.LEFT && loc == Location.BL)
			{
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
				drawImage(x, y);					
								
				x -= SIZE*2;
				loc = Location.UR;
			}
			
			else if(dir == Direction.LEFT && loc == Location.BR)
			{
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
				drawImage(x, y);
				drawImage(x, y + SIZE);						
								
				x -= SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.UP && loc == Location.UL)
			{
				drawImage(x, y);
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
											
				y -= SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.UP && loc == Location.UR)
			{
				drawImage(x + SIZE, y);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);
				drawImage(x, y);			
											
				y -= SIZE*2;
				loc = Location.BR;
			}
			
			else if(dir == Direction.UP && loc == Location.BL)
			{
				drawImage(x, y + SIZE);
				drawImage(x + SIZE, y + SIZE);
				drawImage(x + SIZE, y);
				drawImage(x, y);			
											
				y -= SIZE*2;
				loc = Location.BL;
			}
			
			else if(dir == Direction.UP && loc == Location.BR)
			{
				drawImage(x + SIZE, y + SIZE);
				drawImage(x, y + SIZE);
				drawImage(x, y);
				drawImage(x + SIZE, y);
															
				y -= SIZE*2;
				loc = Location.BR;
			}
			
			iters_left_on_side--;
			
			// Handle state changing logic.
			if(iters_left_on_side == 0)
			{
				// Logic for increasing the iters per side to foster the spiral pattern.
				if(dir == Direction.DOWN || dir == Direction.UP)
				{
					iter_per_side++;
				}
				
				switch(dir)
				{
				case RIGHT: dir = Direction.DOWN;  break;
				case DOWN:  dir = Direction.LEFT;  break;
				case LEFT:  dir = Direction.UP;    break;
				case UP:    dir = Direction.RIGHT; break;
				}
				
				iters_left_on_side = iter_per_side;
			}
		}	
		
		// -- Dither the output on its way out.
		/*
		Ditherer ditherer = new Ditherer();
		ditherer.dither(output);
		*/
		
		ImageUtil.saveImage(output, "Output" + index + ".png");
		System.out.println("Done " + index);
		
	}
	
	// draws the input image onto the output image.
	private void drawImage(int x, int y)
	{
		BufferedImage image = images[index];
		index = (index + 1) % images.length;
		
		g.drawImage(image, x, y, null);
	}

}
