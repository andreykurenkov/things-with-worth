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
