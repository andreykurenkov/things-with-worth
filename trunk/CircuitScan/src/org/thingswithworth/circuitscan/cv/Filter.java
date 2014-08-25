package org.thingswithworth.circuitscan.cv;
import java.awt.image.BufferedImage;
import org.thingswithworth.circuitscan.model.ImageUtil;

import java.awt.Color;
public class Filter {

	public static BufferedImage toBW(BufferedImage image, boolean clone){
		if(image==null)
			return null;
		
		if(clone)
			image = ImageUtil.clone(image);

		double average = 0;
		double averageDif = 0;
		for(int x=0;x<image.getWidth();x++){
			for(int y=0;y<image.getHeight();y++){
				Color c = new Color(image.getRGB(x,y));
				average+= c.getRed()+c.getGreen()+c.getBlue();
				averageDif+=  Math.abs((c.getRed()-c.getGreen()+
						c.getRed()-c.getBlue()+
						c.getGreen()-c.getBlue())/3.0);
			}
		}
		average/=(image.getWidth()*image.getHeight());
		averageDif/=(image.getWidth()*image.getHeight());
		for(int x=0;x<image.getWidth();x++){
			for(int y=0;y<image.getHeight();y++){
				Color c = new Color(image.getRGB(x,y));
				double dif =  Math.abs((c.getRed()-c.getGreen()+
						c.getRed()-c.getBlue()+
						c.getGreen()-c.getBlue())/3.0);
				double mag =  c.getRed()+c.getGreen()+c.getBlue();
				if(mag<average*0.8 && dif<(averageDif+5)*0.99)
					image.setRGB(x,y,Color.black.getRGB());
				else
					image.setRGB(x,y,Color.white.getRGB());
			}
		}

		return image;
	}
}
