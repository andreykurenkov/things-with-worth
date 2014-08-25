package org.thingswithworth.circuitscan.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileIO {
	
	public static BufferedImage getImage(File file){
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean saveImage(BufferedImage image, File file){
		try {
			ImageIO.write(image, "png", file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
		
}
