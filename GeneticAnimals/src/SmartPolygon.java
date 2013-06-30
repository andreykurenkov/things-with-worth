import java.awt.Point;
import java.awt.Polygon;


public class SmartPolygon extends Polygon {
    private double x, y;
    private int xI, yI;
    
	public SmartPolygon(int[] xpoints, int[] ypoints, int numPoints) {
		super(xpoints, ypoints, numPoints);
	}

	public double getArea(){
		double  area = 0;         // Accumulates area in the loop
		int numPoints = npoints;
		int j = numPoints-1;  // The last vertex is the 'previous' one to the first
		for (int i=0; i<numPoints; i++) { 
			area = area +  (xpoints[j]+xpoints[i]) * (ypoints[i]-ypoints[j]); 
			j = i;  //j is previous vertex to i
		}
		return area/2;
	}
	
    public void rotateAboutCenterOfMass(double angle){
		//TODO
		
	}
	
	private void rotateAbout(Point center, double angle){
		
		
	}
	
	public void translate(double dX, double dY){
		x+= dX;
		y+= dY;		
		int newXI = xI+(int)Math.round(x-xI);
		int newYI = yI+(int)Math.round(y-yI);
		this.translate(newXI-xI, newYI-yI);
		xI = newXI;
		yI = newYI;
	}
	
	private Point rotatePoint(Point pt, Point center, double angleDeg)
	{
	    double angleRad = (angleDeg/180)*Math.PI;
	    double cosAngle = Math.cos(angleRad );
	    double sinAngle = Math.sin(angleRad );
	    double dx = (pt.x-center.x);
	    double dy = (pt.y-center.y);

	    pt.x = center.x + (int) (dx*cosAngle-dy*sinAngle);
	    pt.y = center.y + (int) (dx*sinAngle+dy*cosAngle);
	    return pt;
	}
	
}
