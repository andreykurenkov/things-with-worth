import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;


public class WildsView extends JPanel {
	private AnimalWilds wilds;
    private Dimension dim;
    
	public WildsView(AnimalWilds wilds) {
		setBackground(Color.WHITE);
		this.wilds = wilds;
		this.setPreferredSize(new Dimension(wilds.getBorder(),wilds.getBorder()));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(GeneticAnimal animal:wilds.getAnimals()){
		    animal.draw(g);	
		}
		for(Point obstacle:wilds.getObstacles()){
			g.setColor(Color.RED);
		    g.drawOval(obstacle.x, obstacle.y, 3, 3);
		}
		for(Point food:wilds.getFood()){
			g.setColor(Color.GREEN);
		    g.drawOval(food.x, food.y, 3, 3);
		}
	}

}
