package org.ThingsWithWorth.MetaMusic.GUI;

import javax.swing.JTabbedPane;

import org.ThingsWithWorth.MetaMusic.Model.MusicCollection;

public class GraphPanes extends JTabbedPane {
	private PiePanel pie;
	private FilesPanel files;
	private MusicCollection collection;

	public GraphPanes() {
		collection = new MusicCollection();
		files = new FilesPanel();
		this.add(files);
		pie = new PiePanel(collection);
	}
}
