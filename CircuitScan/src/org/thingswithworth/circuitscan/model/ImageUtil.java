package org.thingswithworth.circuitscan.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class ImageUtil {

	public static BufferedImage clone(BufferedImage image){
		BufferedImage c = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = c.getGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return c;
	}
}
