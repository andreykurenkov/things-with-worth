



import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Main class with frame and not much else.
 * 
 * @author andrey
 * 
 */
public class AnimalEvolution {

	public static void main(String[] args) {

		JFrame theFrame = new JFrame("This is a metaphor for your life");
		int border = getIntSafely("What border length would you like?");

		AnimalWilds life = new AnimalWilds(border);
		WildsView lifePanel = new WildsView(life);
		ControlPanel control = new ControlPanel(lifePanel, life);
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.add(control, BorderLayout.NORTH);
		theFrame.add(lifePanel, BorderLayout.CENTER);
		theFrame.pack();
		theFrame.setVisible(true);

	}

	private static int getIntSafely(String checkMessage) {
		boolean validInput = false;
		int input = -1;
		while (!validInput) {
			try {
				String response = JOptionPane.showInputDialog(checkMessage);
				if(response == null)
					System.exit(0);
				input = Integer.parseInt(response);
				validInput = true;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, ":(. Thats not a valid int input. Try again.");
				validInput = false;
			}
		}
		return input;
	}
}