package org.thingswithworth.nicities.test.manual;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.thingswithworth.nicities.GUI.Helpers.Dialogs;

public class DialogsTests {
	private JPanel testPanel;
	private JButton getFoldersButton;

	public static void main(String[] args) {
		JFrame testFrame = new JFrame("Dialogs-getFolders test");
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		DialogsTests test = new DialogsTests();
		testFrame.add(test.getTestPanel());
		testFrame.pack();
		testFrame.setVisible(true);
	}

	public DialogsTests() {
		// TODO:add rows as more tests put here
		testPanel = new JPanel(new GridLayout(1, 1));
		getFoldersButton = new JButton("Test getFolders");
		getFoldersButton.addActionListener(new DialogListener());
		testPanel.add(getFoldersButton);

	}

	public JPanel getTestPanel() {
		return testPanel;
	}

	private class DialogListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == getFoldersButton) {
				File[] files = Dialogs.getFolders(null, "Get Folders Test",
						"This should function intuitively - you select a collection of directories", null, null);
				System.out.println("Got the following folders:");
				for (File f : files)
					System.out.println(f);
			}

		}
	}
}
