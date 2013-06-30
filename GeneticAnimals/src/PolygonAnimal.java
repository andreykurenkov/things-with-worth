import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;


public class PolygonAnimal extends GeneticAnimal{

	private Polygon body;
	private Point[] points;
	private static final double MAX_AREA = 50.0;//TODO: arbitrary num, adjust
	private static final double MAX_SPEED = 1.0;//TODO:adjust
	private static final int NUM_POINTS=12;
	private static final int LIFE = 50;


	public PolygonAnimal(int x, int y, Point[] points) {
		this(x, y);
		this.points=points;
		int[] xpoints = new int[NUM_POINTS];
		int[] ypoints = new int[NUM_POINTS];
		for(int point=0;point<NUM_POINTS;point++){
			xpoints[point] = points[point].x;
			ypoints[point] = points[point].y;
		}
		body = new Polygon(xpoints, ypoints, NUM_POINTS);
		speed = getArea(body)/MAX_AREA * MAX_SPEED;
		body.translate(x, y);
	}

	public static double getArea(Polygon polygon){
		double  area = 0;         // Accumulates area in the loop
		int numPoints = polygon.npoints;
		int j = numPoints-1;  // The last vertex is the 'previous' one to the first
		for (int i=0; i<numPoints; i++) { 
			area = area +  (polygon.xpoints[j]+polygon.xpoints[i]) * (polygon.ypoints[j]-polygon.ypoints[i]); 
			j = i;  //j is previous vertex to i
		}
		return area/2;
	}

	public static double getAngleOfPoint(Point point){
		return Math.atan2(point.y, point.x);
	}

	public static double getDistOfPoint(Point point){
		return Math.sqrt(point.x*point.x+point.y*point.y);
	}

	public PolygonAnimal(int x, int y) {
		super(LIFE, x, y);
	}

	@Override
	public void draw(Graphics g) {

	}

	@Override
	public void update(AnimalWilds wilds) {

	}

	public static PolygonAnimal makeRandom(double maxDist, double minSize, double maxSize){
		Point[] points = new Point[NUM_POINTS];
		double radialDiv = 2*Math.PI/NUM_POINTS;
		int minPointX = 0;
		int minPointY = 0;
		for(int point=0;point<NUM_POINTS;point++){
			double region = radialDiv*point;
			double angle = region + Math.random()*radialDiv;
			double dist = minSize + Math.random()*(maxSize-minSize);
			int x = (int) (Math.cos(angle)*dist);
			int y = (int) (Math.sin(angle)*dist);
			minPointX = Math.min(minPointX, x);
			minPointY = Math.min(minPointY, y);
			points[point]=new Point(x,y);
		}
		int x = (int)(Math.random()*(maxDist+minPointX))-minPointX;
		int y = (int)(Math.random()*(maxDist+minPointY))-minPointY;        
		return new PolygonAnimal(x,y,points);
	}

}
