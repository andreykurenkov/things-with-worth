import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;


public class PolygonAnimal extends GeneticAnimal{

	private SmartPolygon body;
	private Point[] points;
	private static double MAX_AREA = 2000.0;//TODO: arbitrary num, adjust
	private static final double MAX_SPEED = 1.0;//TODO:adjust
	private static final int NUM_POINTS=12;
	private static final double LIFE = 1.0;

	public PolygonAnimal(int x, int y, double startAngle, Point[] points) {
		super(LIFE,x,y);
		this.points=points;
		int[] xpoints = new int[NUM_POINTS];
		int[] ypoints = new int[NUM_POINTS];
		for(int point=0;point<NUM_POINTS;point++){
			xpoints[point] = points[point].x;
			ypoints[point] = points[point].y;
		}
		body = new SmartPolygon(xpoints, ypoints, NUM_POINTS);
		speed = (body.getArea()-1000)/MAX_AREA * MAX_SPEED;
		velX = speed * Math.cos(startAngle);
		velY = speed * Math.sin(-startAngle);//flip
		velAng = 0;
		body.translate(x, y);
		alive=true;
	}
	
	
	public static double getAngleOfPoint(Point point){
		return Math.atan2(point.y, point.x);
	}

	public static double getDistOfPoint(Point point){
		return Math.sqrt(point.x*point.x+point.y*point.y);
	}

	@Override
	public void draw(Graphics g) {
		if(!alive)
			return;
		//g.drawPolygon(body);
		g.setColor(Color.BLACK);
		g.drawPolyline(body.xpoints, body.ypoints, body.npoints);
		g.drawLine(body.xpoints[0], body.ypoints[0], body.xpoints[NUM_POINTS-1], body.ypoints[NUM_POINTS-1]);
	}

	@Override
	public void update(AnimalWilds wilds) {
		if(!alive)
			return;
		body.rotateAboutCenterOfMass(velAng);
		body.translate(velX,velY);
		for(Point obstacle: wilds.getObstacles()){
			if(body.contains(obstacle)){
				//TODO handle collision
			}
		}
		ArrayList<Point> removeFood = new ArrayList<Point>();
		for(Point food: wilds.getFood()){
			if(body.contains(food)){
				removeFood.add(food);
				life+=0.08;
			}
		}
		for(Point remove:removeFood)
			wilds.getFood().remove(remove);//TODO: HACKS
		life-=0.001;
		alive=life>0;
	}
	
	public SmartPolygon getBody(){
		return body;
	}
	
	public double getArea(){
		return body.getArea();
	}

	public static PolygonAnimal makeRandom(double maxDist, double minSize, double maxSize, int minX, int minY){
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
		int x = minX + (int)(Math.random()*(maxDist+minPointX))-minPointX;
		int y = minY + (int)(Math.random()*(maxDist+minPointY))-minPointY;
		double startAngle = Math.random() * Math.PI * 2;
		return new PolygonAnimal(x,y,startAngle, points);
	}

}
