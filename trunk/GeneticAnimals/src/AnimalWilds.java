import java.awt.Point;
import java.util.ArrayList;


public class AnimalWilds {
	
	private ArrayList<GeneticAnimal> animals;
	private int generations;
	private ArrayList<Point> obstacles;
	private ArrayList<Point> food;
	private int DIVISIONS=12;
	
	public enum State {
		NEW, RUNNING, PAUSED, STABLE
	};
	
	private State state;
	
	public AnimalWilds(){
		this(600);
	}
	
	public AnimalWilds(int border) {

		animals = new ArrayList<GeneticAnimal>();
		obstacles = new ArrayList<Point>();
		food = new ArrayList<Point>();
	}

	public void randomise(double seed){
		
		
	}
	
	public void reset(){
		
	}
	
	public void run(){
		state = State.RUNNING;
	}
	
	public void pause(){
		state = State.PAUSED;
	}
	
	public void update(){
		
		//check for collisions
		
		//update momentum
		
		//update positions (rotate as needed)
		
	}

	private void rotateAnimal(GeneticAnimal animal, Point center ){
		
		
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
	
	/**
	 * Indicatse whether the game has reached a steady state.
	 * @return
	 */
	public boolean isStable(){
		return false;
	}
	
	public int getGenerations(){
		return generations;
	}
	
	public ArrayList<GeneticAnimal> getAnimals(){
		return animals;//TODO: ideally this would be cloned, but eh
	}

	public ArrayList<Point> getObstacles() {
		return obstacles;
	}

	public ArrayList<Point> getFood() {
		return food;
	}

}
