package org.thingswithworth.nicities.gui.components;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import org.thingswithworth.nicities.fileio.ImageLoader;

public class BackActionNextButtons extends JPanel {
	private final JButton back;
	private final JButton action;
	private final JButton next;

	public BackActionNextButtons(String back, String actionText, boolean vertical, String next) {
		this.setLayout(new GridLayout(vertical ? 3 : 1, vertical ? 1 : 3));
		this.back = new JButton(ImageLoader.getImageIcon(back));
		this.add(this.back);
		if (actionText != null) {
			this.action = new JButton(actionText);
			this.add(this.action);
		} else {
			this.action = new JButton("");
		}
		this.next = new JButton(ImageLoader.getImageIcon(next));
		this.add(this.next);

	}

	public BackActionNextButtons(String back, boolean vertical, String next) {
		this(back, null, vertical, next);
	}

	public BackActionNextButtons(String actionText, boolean vertical) {
		this((vertical ? "UpArrow.png" : "LeftArrow.png"), actionText, vertical, (vertical ? "DownArrow.png"
				: "RightArrow.png"));
	}

	public BackActionNextButtons() {
		this(null, false);
	}

	public void addBackListener(ActionListener add) {
		back.addActionListener(add);
	}

	public void addNextListener(ActionListener add) {
		next.addActionListener(add);
	}

	public void addActionListener(ActionListener add) {
		if (action != null)
			action.addActionListener(add);
	}

	public JButton getBack() {
		return back;
	}

	public JButton getNext() {
		return next;
	}

	public JButton getAction() {
		return action;
	}

	public void setActionText(String setTo) {
		this.action.setText(setTo);
	}

	public void setBorders(Border setTo) {
		back.setBorder(setTo);
		if (action != null)
			action.setBorder(setTo);
		next.setBorder(setTo);
	}
}
