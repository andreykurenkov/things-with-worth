package org.ThingsWithWorth.MetaMusic.GUI;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import org.ThingsWithWorth.MetaMusic.Model.MusicCollection;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

public class PiePanel extends GraphPanel<PiePlot, DefaultPieDataset> {
	private PiePlot plot;
	private DefaultPieDataset data;

	public PiePanel(MusicCollection model) {
		super(model);
		plot = new PiePlot();
		data = new DefaultPieDataset();
		plot.setDataset(data);
	}

	public void paint(Graphics g) {
		plot.draw((Graphics2D) g, new Rectangle(0, 0, getWidth(), getHeight()), new Point(0, 0), null, null);
	}

	@Override
	public void drawPlot() {
		repaint();
	}

	@Override
	public void setData(DefaultPieDataset data) {
		this.data = data;
		plot.setDataset(data);
	}

	@Override
	public DefaultPieDataset getData() {
		return data;
	}

	@Override
	public PiePlot getPlot() {
		return plot;
	}

	@Override
	public void constructDataset() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean handlingEvents() {
		return true;
	}

}
