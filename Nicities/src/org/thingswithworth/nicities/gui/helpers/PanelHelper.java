package org.thingswithworth.nicities.gui.helpers;

import java.awt.Component;

import javax.swing.JPanel;

public class PanelHelper {

	public void addAll(JPanel to, Component... add) {
		for (Component c : add)
			to.add(c);
	}
}
