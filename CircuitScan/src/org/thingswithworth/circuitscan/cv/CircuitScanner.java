package org.thingswithworth.circuitscan.cv;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import net.sourceforge.tess4j.TessAPI;
import net.sourceforge.tess4j.TessAPI.TessBaseAPI;
import net.sourceforge.tess4j.TessAPI1;

import com.recognition.software.jdeskew.ImageDeskew;
import com.recognition.software.jdeskew.ImageUtil;
public class CircuitScanner {
	private BufferedImage circuitImage;
	private ImageDeskew deskew;
	private TessAPI ocr;
	private TessBaseAPI ocrAPI; 
	private TessAPI1 ocr1;
	public CircuitScanner(BufferedImage image){
		this.circuitImage = image;
		this.deskew = new ImageDeskew(circuitImage);
		ocr = TessAPI.INSTANCE;
		ocrAPI = ocr.TessBaseAPICreate();
	}
	
	
	

}
