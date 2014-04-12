package org.thingswithworth.circuitscan.model;

import java.io.File;

public abstract class Component {
	public final File SCHEMATIC_FOLDER = new File("res/omponents");
	
	protected double x,y;
	protected int net;
	protected String name;
	
	public Component(double x, double y, String name) {
		super();
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public int getNet() {
		return net;
	}

	public void setNet(int net) {
		this.net = net;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
}
