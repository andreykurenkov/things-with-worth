package org.thingswithworth.nicities.gui.components;

import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.thingswithworth.nicities.gui.helpers.GBHelp;

public class ProgressButtonPanel extends JPanel {
	private JProgressBar bar;
	private JButton button;

	public ProgressButtonPanel() {
		this(new JProgressBar(), new JButton());
	}

	public ProgressButtonPanel(JProgressBar bar, JButton button) {
		super(new GridBagLayout());
		add(bar, GBHelp.constrain(0, 0, GBHelp.BOTH, 0.8, 1, GBHelp.REL, GBHelp.REM, GBHelp.WEST));
		add(button, GBHelp.constrain(1, 0, GBHelp.BOTH, 0.2, 1, GBHelp.REM, GBHelp.REM, GBHelp.EAST));
		this.bar = bar;
		this.button = button;
	}

	public JProgressBar getBar() {
		return bar;
	}

	public JButton getButton() {
		return button;
	}

	public void insertNewButton(JButton insert) {
		this.remove(button);
		this.button = insert;
		add(button, GBHelp.constrain(1, 0, GBHelp.BOTH, 0.2, 1, GBHelp.REM, GBHelp.REM, GBHelp.EAST));
		this.revalidate();
	}

	public void insertNewBar(JProgressBar newBar) {
		this.remove(bar);
		this.bar = newBar;
		add(bar, GBHelp.constrain(0, 0, GBHelp.BOTH, 0.8, 1, GBHelp.REL, GBHelp.REM, GBHelp.WEST));
		this.revalidate();
	}
}
