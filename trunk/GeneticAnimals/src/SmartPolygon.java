import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


public class SmartPolygon extends Polygon {
    private double x, y;
    private double dx, dy;
    private int xI, yI;
    private Point centroid;
    private double area;
    private Point[] originalPoints;
    private double angle;
    
	public SmartPolygon(int[] xpoints, int[] ypoints, int numPoints) {
		super(xpoints, ypoints, numPoints);
	
		for (int i=0, j = numPoints-1; i<numPoints; i++, j=(j+1)%numPoints)
			area +=  (xpoints[j]+xpoints[i]) * (ypoints[i]-ypoints[j]);
		
		originalPoints = new Point[numPoints];
		
		for(int i =0; i< numPoints; i++)
			originalPoints[i] = new Point(this.xpoints[i], this.xpoints[i]);
		
		int cX = 0;
		int cY = 0;
		
		for(int i =0; i < numPoints; i++){
			cX += xpoints[i];
			cY += ypoints[i];
		}
		
		centroid = new Point(cX/numPoints, cY/numPoints);
		
	}

	public double getArea(){
		return area;
	}
	
	
    public void rotate(Point center, Pair<Double, Double> velocity){
    	double dist = Math.sqrt(  Math.pow(center.x - centroid.x,2) + Math.pow(center.y-centroid.y,2));
    
    	double angle = Math.acos(Math.abs(center.x-centroid.x)/dist);
    	
    	
    	
    	Point[] temp = new Point[originalPoints.length];
    	
    	rotatePointMatrix(center, originalPoints, angle, temp );
    	
    	
    }
    
    private void rotatePointMatrix(Point center, Point[] origPoints, double angle, Point[] storeTo){

        /* We ge the original points of the polygon we wish to rotate
         *  and rotate them with affine transform to the given angle. 
         *  After the opeariont is complete the points are stored to the 
         *  array given to the method.
        */
        AffineTransform.getRotateInstance
        (Math.toRadians(angle), center.x, center.y)
                .transform(origPoints,0,new Point2D[origPoints.length],0,5);

        //TODO: Repeated affine transforms deform the polygons. This has been a problem so I've temporarily disabled rotation while I deal with this.

    }
    
    
    
	public void translate(double dX, double dY){
		x+= dX;
		y+= dY;		
		int newXI = xI+(int)Math.round(x-xI);
		int newYI = yI+(int)Math.round(y-yI);
		this.translate(newXI-xI, newYI-yI);
		xI = newXI;
		yI = newYI;
		centroid.translate(newXI -xI, newYI - yI);
	}
	
	public void draw(Graphics g){
        g.setColor(Color.BLACK);
        g.drawPolyline(xpoints, ypoints, npoints);
        g.drawLine(xpoints[0], ypoints[0], xpoints[xpoints.length - 1], ypoints[xpoints.length - 1]);

	}
}
