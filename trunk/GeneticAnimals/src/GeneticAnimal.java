import java.awt.Graphics;
import java.util.List;


public abstract class GeneticAnimal {
	
	protected double life;
	protected boolean alive;
	protected double speed;
	protected double velX, velY, velAng;
	protected double size, x, y;
	
	public GeneticAnimal(double life, int x, int y){
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
	
	public double getSize(){
		return size;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
		
	public abstract void draw(Graphics g);
		
	public abstract void update(AnimalWilds wilds);
	
	public abstract double getArea();

}
