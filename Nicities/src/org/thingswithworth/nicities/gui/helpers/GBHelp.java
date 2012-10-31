package org.thingswithworth.nicities.gui.helpers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JPanel;

public class GBHelp {
	public static final Insets defInsets = new Insets(0, 0, 0, 0);
	public static final double defWeight = 1;
	public static final int defPad = 0;
	public static final int HOR = GridBagConstraints.HORIZONTAL;
	public static final int VER = GridBagConstraints.VERTICAL;
	public static final int BOTH = GridBagConstraints.BOTH;
	public static final int REL = GridBagConstraints.RELATIVE;
	public static final int REM = GridBagConstraints.REMAINDER;
	public static final int NORTH = GridBagConstraints.NORTH;
	public static final int SOUTH = GridBagConstraints.SOUTH;
	public static final int EAST = GridBagConstraints.EAST;
	public static final int WEST = GridBagConstraints.WEST;
	public static final int CENT = GridBagConstraints.CENTER;
	public static final int NONE = GridBagConstraints.NONE;

	private GBHelp() {
	};

	public static GridBagConstraints constrain(int x, int y, int fill, double weightx, double weighty, int width,
			int height, int anchor) {
		GridBagConstraints c = GBHelp.constrain(x, y, fill, weightx, weighty, width, height);
		c.anchor = anchor;
		return c;
	}

	public static GridBagConstraints constrain(int x, int y, int fill, double weightx, double weighty, int width, int height) {
		GridBagConstraints c = GBHelp.constrain(x, y, fill, weightx, weighty);
		c.gridwidth = width;
		c.gridheight = height;
		return c;
	}

	public static GridBagConstraints constrain(int x, int y, int fill, double weightx, double weighty) {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = x;
		c.gridy = y;
		c.fill = fill;
		c.weightx = weightx;
		c.weighty = weighty;
		return c;
	}

	public static void addGroup(JPanel addTo, Component[] add, int[][] intVars, double[][] doubleVars) {
		for (int i = 0; i < add.length; i++) {
			addTo.add(add[i], constrain(intVars[i][0], intVars[i][1], intVars[i][2], doubleVars[i][0], doubleVars[i][1]));
		}
	}
}
