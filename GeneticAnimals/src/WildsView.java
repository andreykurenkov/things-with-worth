import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;


public class WildsView extends JPanel {
	private AnimalWilds wilds;
    private Dimension dim;
    
	public WildsView(AnimalWilds wilds) {
		this.wilds = wilds;
	}
	
	public void paint(Graphics g) {
		for(GeneticAnimal animal:wilds.getAnimals()){
		    animal.draw(g);	
		}
	}

}
