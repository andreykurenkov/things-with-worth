import java.util.List;


public abstract class GeneticAnimal {
	
	protected double life;
	protected boolean alive;
	protected double speed;
	protected double size;
	
	public GeneticAnimal(int life){
		this.life=life;
		alive=true;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public abstract List<GeneticAnimal> update();
	
	public abstract boolean isPrey();//We have two species

}
