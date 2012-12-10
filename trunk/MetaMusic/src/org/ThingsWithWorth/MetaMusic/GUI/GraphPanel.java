package org.ThingsWithWorth.MetaMusic.GUI;

import javax.swing.JPanel;

import org.ThingsWithWorth.MetaMusic.Model.MusicCollection;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.AbstractDataset;

public abstract class GraphPanel<P extends Plot, T extends AbstractDataset> extends JPanel {
	protected MusicCollection modelCollection;

	public GraphPanel(MusicCollection collection) {
		this.modelCollection = collection;
	}

	public abstract void constructDataset();

	public abstract boolean handlingEvents();

	public abstract void drawPlot();

	public abstract void setData(T data);

	public abstract T getData();

	public abstract P getPlot();
}