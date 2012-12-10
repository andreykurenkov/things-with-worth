package org.ThingsWithWorth.MetaMusic.GUI;

import javax.swing.JButton;
import javax.swing.JToolBar;

public class MusicToolbar extends JToolBar {
	private JButton launchGenreSetter;

	public MusicToolbar() {
		launchGenreSetter = new JButton("Correct Genres");
		launchGenreSetter.setToolTipText("Click to launch a tool that will help you correct wrong genre classifications");
		add(launchGenreSetter);
	}
}
