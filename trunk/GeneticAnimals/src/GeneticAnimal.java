import java.awt.Graphics;
import java.util.List;


public abstract class GeneticAnimal {
	
	protected double life;
	protected boolean alive;
	protected double speed;
	protected double velX, velY, velAng;
	protected int size, x, y;
	
	public GeneticAnimal(int life, int x, int y){
		this.life=life;
		this.x=x;
		this.y=y;
	}
	
	public GeneticAnimal(int life){
		this(life,0,0);
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public int getSize(){
		return size;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
		
	public abstract void draw(Graphics g);
		
	public abstract void update(AnimalWilds wilds);

}
