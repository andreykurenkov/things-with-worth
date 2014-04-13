import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.PrintWriter;
public class toBW{
	
	public static void main(String[] args) throws IOException{
		PrintWriter writer = new PrintWriter("positives.txt");
		File here = new File(".");
		for(File imageFile: here.listFiles()){
			if(!imageFile.getName().contains(".png") || imageFile.getName().contains("BW"))
				continue;
			BufferedImage image = ImageIO.read(imageFile);
			if(image!=null){
				if(image.getHeight()>image.getWidth()){			
					AffineTransform transform = new AffineTransform();
					transform.rotate(Math.PI/2, image.getWidth()/2, image.getHeight()/2);
					AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
					int width = image.getWidth();
					image = op.filter(image, null);
					image = image.getSubimage(0,image.getHeight()-width,image.getWidth(),width);
				}
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
						if(mag<average*0.75 && dif<(averageDif+5)*0.8)
							image.setRGB(x,y,Color.black.getRGB());
						else
							image.setRGB(x,y,Color.white.getRGB());
					}
				}
				String name = imageFile.getName().replace(".png","")+"BW.png";
				writer.write("positive\\"+name+" 1 0 0 "+image.getWidth()+" "+image.getHeight()+"\n");
				//ImageIO.write(image, "png", new File(here,name));
				}
			}
		writer.close();
	}
}