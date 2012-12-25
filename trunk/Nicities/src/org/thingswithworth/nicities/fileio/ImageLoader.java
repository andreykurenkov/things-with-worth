package org.thingswithworth.nicities.fileio;

import java.net.URL;

import javax.swing.ImageIcon;

public class ImageLoader {
	private final static String folder = "./Res/";

	public static ImageIcon getImageIcon(String name) {
		String path = folder + name;
		return createImageIcon(path);
	}

	public static ImageIcon createImageIcon(String path, String description) {
		URL imgURL = ImageLoader.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public static ImageIcon createImageIcon(String path) {
		return createImageIcon(path, "");
	}
}
