import java.util.ArrayList;


public class AnimalWilds {
	
	private ArrayList<GeneticAnimal> animals;
	private int generations;//Do it traditional?
	
	public void run(){
		
	}
	
	public void randomise(double seed){
		
	}
	
	public void reset(){
		
	}
	
	public void pause(){
		
	}
	
	public void update(int width, int height){
		
	}
	
	/**
	 * Indicatse whether the game has reached a steady state.
	 * @return
	 */
	public boolean isStable(){
		return false;
	}
	
	public int getGenerations(){
		return 1;
	}
	
	public ArrayList<GeneticAnimal> getAnimals(){
		return animals;//TODO: ideally this would be cloned, but eh
	}

}
