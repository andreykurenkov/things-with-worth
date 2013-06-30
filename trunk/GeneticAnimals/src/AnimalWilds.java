import java.awt.Point;
import java.util.ArrayList;


public class AnimalWilds {
	
	private ArrayList<GeneticAnimal> animals;
	private int generations;
	private ArrayList<Point> obstacles;
	private ArrayList<Point> food;
	private int border;
	private int divisions;
	private int numFood;
	private double seed;
	
	//TODO: configurable?
	private final static int NUM_ANIMALS =10;
	
	public enum State {
		NEW, RUNNING, PAUSED, STABLE
	};
	
	private State state;
	
	public AnimalWilds(){
		this(600);
		animals = new ArrayList<GeneticAnimal>();
		obstacles = new ArrayList<Point>();
		food = new ArrayList<Point>();
		reset(0.5);
	}
	
	public AnimalWilds(int border) {
        this.border=border;
		animals = new ArrayList<GeneticAnimal>();
		obstacles = new ArrayList<Point>();
		food = new ArrayList<Point>();
	}

	/**
	 * Randomize the animals in the current world.
	 * @param seed
	 */
	public void randomise(){
		animals.clear();
		for(int i=0;i<NUM_ANIMALS;i++){
			animals.add(PolygonAnimal.makeRandom(divLength, divLength/5.0, divLength*4/5.0));
		}
	}
	
	/**
	 * Recreate the entire world
	 * @param seed
	 */
	public void reset(double seed){
		animals.clear();
		obstacles.clear();
		food.clear();
		
		this.seed = seed % 1.0;
		divisions = border / (10 + (int)(10*seed));
		numFood = divisions * divisions / 2;
		int divLength = border/divisions;
		for(int x=0;x<divisions;x++){
			for(int y=0;y<divisions;y++){
				if(!(x==0 && y==0)){
					int newObstacleX = divLength*x+(int)(Math.random()*divLength);
					int newObstacleY = divLength*y+(int)(Math.random()*divLength);
					obstacles.add(new Point(newObstacleX,newObstacleY));
				}
			}
		}
		int count=0;
		while(count < numFood){
			int x=(int)(Math.random()*divisions);
			int y=(int)(Math.random()*divisions);
			if(!(x==0 && y==0)){
				count++;
				int newObstacleX = divLength*x+(int)(Math.random()*divLength);
				int newObstacleY = divLength*y+(int)(Math.random()*divLength);
				food.add(new Point(newObstacleX,newObstacleY));
			}
		}
		
		for(int i=0;i<NUM_ANIMALS;i++){
			animals.add(PolygonAnimal.makeRandom(divLength, divLength/5.0, divLength*4/5.0));
		}
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
