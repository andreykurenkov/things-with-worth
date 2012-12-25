package org.thingswithworth.nicities.gui.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.thingswithworth.nicities.gui.helpers.Dialogs;

public class ThreadedProgressDialog extends JDialog {
	private JLabel text;
	JScrollPane textPane;
	private JProgressBar bar;
	private BoundedRangeModel barModel;
	private JButton button;

	public ThreadedProgressDialog(Window in, String title, String initialText) {
		this(in, title, initialText, 0, 100, true);
	}

	public ThreadedProgressDialog(Window in, String title, String initialText, int min, int max, boolean haveButton) {
		super(in, title);
		text = new JLabel(initialText);
		textPane = new JScrollPane(text);
		textPane.getViewport().setPreferredSize(new Dimension(text.getWidth(), text.getHeight()));
		textPane.setMaximumSize(new Dimension(400, 600));
		this.add(textPane, BorderLayout.CENTER);
		bar = new JProgressBar();
		barModel = new DefaultBoundedRangeModel(min, 0, min, max);
		bar.setModel(barModel);
		if (haveButton) {
			button = new JButton("Cancel");
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					setVisible(false);
				}

			});

			ProgressButtonPanel progressPanel = new ProgressButtonPanel(bar, button);
			this.add(progressPanel, BorderLayout.SOUTH);
		} else {
			this.add(bar, BorderLayout.SOUTH);
		}
		this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		Dialogs.centerDialog(this, in);
	}

	public void setProgress(int progress) {
		bar.setValue(progress);
		if (button != null) {
			if (bar.getPercentComplete() >= 1)
				button.setText("Close");
		}
		this.repaint();
	}

	public void setText(String setTo) {
		text.setText(setTo);
		text.revalidate();
		int preferredWidth = text.getWidth() < 400 && text.getWidth() > textPane.getWidth() ? text.getWidth() + 35
				: textPane.getWidth();
		int preferredHeight = text.getHeight() < 600 ? text.getHeight() + 35 : textPane.getHeight();
		textPane.getViewport().setPreferredSize(new Dimension(preferredWidth, preferredHeight));
		textPane.revalidate();
		Dialogs.centerDialog(this, this.getOwner());
		this.pack();
		this.repaint();
	}

	public double getPercent() {
		return bar.getPercentComplete();
	}

	public SwingWorker<Void, Void> startDisplay(boolean modal, final boolean dispose) {
		this.setModal(modal);
		SwingWorker<Void, Void> displayWorker = new SwingWorker<Void, Void>() {

			@Override
			protected Void doInBackground() throws Exception {
				setVisible(true);
				return null;
			}

			protected void done() {
				if (dispose)
					dispose();
			}

		};
		displayWorker.execute();
		return displayWorker;
	}
}
