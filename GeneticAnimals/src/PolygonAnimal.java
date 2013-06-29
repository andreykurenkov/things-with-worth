import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;


public class PolygonAnimal extends GeneticAnimal{

	private Polygon body;
	private Point[] points;
	
	public PolygonAnimal(int life, int x, int y, Point[] points) {
		super(life, x, y);
		this.points=points;
	}
	
	public PolygonAnimal(int life, int x, int y) {
		super(life, x, y);
	}

	@Override
	public void draw(Graphics g) {

	}

	@Override
	public void update(AnimalWilds wilds) {
		
	}

}
