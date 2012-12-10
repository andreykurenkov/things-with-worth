package org.ThingsWithWorth.MetaMusic.GUI;

import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.jfree.chart.plot.PiePlot;

public class MusicGrapher extends JFrame {
	private PiePlot plot;
	private JProgressBar bar;
	private SwingWorker<Void, Void> load;
	private boolean started;

}
