package org.thingswithworth.nicities.gui.components;

import java.io.File;
import java.io.FileFilter;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.thingswithworth.nicities.Observer;

public class FileBrowserPanel extends JSplitPane {
	private DirectoryBrowserPanel browser;
	private JFileList fileList;
	private JScrollPane fileScroll;
	private final double weightDominant = 10;
	private final double weightDemanded = 0.1;
	private final double weightNormal = 1;

	public FileBrowserPanel(FileFilter fileFilter, final boolean autoResize) {
		this(fileFilter, false, null);
	}

	public FileBrowserPanel(FileFilter fileFilter) {
		this(fileFilter, false);
	}

	public FileBrowserPanel(FileFilter fileFilter, final boolean autoResize, File initDir) {
		this(fileFilter, autoResize, false, initDir);
	}

	public FileBrowserPanel(FileFilter fileFilter, final boolean autoResize, final boolean horizontal, File initDir) {
		super(horizontal ? JSplitPane.VERTICAL_SPLIT : JSplitPane.HORIZONTAL_SPLIT);
		if (initDir != null)
			browser = new DirectoryBrowserPanel(initDir, "File Browser");
		else
			browser = new DirectoryBrowserPanel("File Browser");
		fileList = new JFileList(fileFilter, browser.getCurrentDirectory());
		fileList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		fileList.setLayoutOrientation(JList.VERTICAL);
		fileScroll = new JScrollPane(fileList);

		browser.getList().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent event) {
				JList list = (JList) event.getSource();
				File newDir = (File) list.getSelectedValue();
				fileList.changeDirectory(newDir);
				if (autoResize)
					resizePane();
			}
		});
		if (autoResize) {
			browser.getList().acceptObserver(new Observer<JFileList>() {

				@Override
				public void acceptNotification(JFileList source, String code, Object... args) {
					if (code.equals(JFileList.CHANGED_DIR_CODE)) {
						resizePane();
					}
				}

			});
		}
		fileList.setCellRenderer(new JFileList.FileNameRenderer());
		this.setOneTouchExpandable(true);
		this.setContinuousLayout(true);
		this.setResizeWeight(0.6);
		this.setTopComponent(browser);
		this.setBottomComponent(fileScroll);
	}

	private void resizePane() {
		JFileList list = browser.getList();
		FileBrowserPanel.this.setDividerLocation(((double) list.getModel().getSize()
				/ (list.getModel().getSize() + fileList.getModel().getSize()) + 0.4) * 0.8);
		FileBrowserPanel.this.repaint();
	}

	public JFileList getFileList() {
		return fileList;
	}

	public DirectoryBrowserPanel getBrowser() {
		return browser;
	}
}
