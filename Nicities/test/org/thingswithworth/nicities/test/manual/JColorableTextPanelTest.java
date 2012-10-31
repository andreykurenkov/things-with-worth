package org.thingswithworth.nicities.test.manual;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.thingswithworth.nicities.GUI.Components.JColorableTextPane;

public class JColorableTextPanelTest {
	private JPanel testPanel;

	public static void main(String[] args) {
		JFrame testFrame = new JFrame("JColorableTextPane test");
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JColorableTextPanelTest test = new JColorableTextPanelTest();
		testFrame.add(test.getTestPanel());
		testFrame.pack();
		testFrame.setVisible(true);
	}

	public JColorableTextPanelTest() {
		testPanel = new JPanel();
		JColorableTextPane test = new JColorableTextPane();
		test.addColoring("public", Color.blue);
		test.addColoring("void", Color.red);
		test.addColoring("static", Color.pink);
		test.addColoring("class", Color.green);
		test.addRegionColoring("/*", "*/", Color.GRAY);
		test.addRegionColoring("\\\\", "\n", Color.LIGHT_GRAY);
		test.append(
				Color.yellow,
				"This text should be yellow. You can inpu public, which should be blue, or test, which should be red.\nCommens should also work\n");
		test.append(Color.black, "");
		testPanel.add(test);
		testPanel.setPreferredSize(new Dimension(800, 600));
	}

	public JPanel getTestPanel() {
		return testPanel;
	}

}
