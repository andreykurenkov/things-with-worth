package org.thingswithworth.nicities.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileSystemView;

public class FileAdderPanel extends JPanel {
	private JButton add, done, remove;
	private JFileList selectedList;

	public FileAdderPanel(String inquisition, File startDir, File[] initialFiles, boolean doneButton) {
		this(inquisition, (Font) null, startDir, initialFiles, doneButton);

	}

	public FileAdderPanel(String inquisition, Font inquisitionFont, File startDir, File[] initialFiles, boolean doneButton) {
		super(new BorderLayout());
		if (startDir == null)
			startDir = FileSystemView.getFileSystemView().getHomeDirectory();
		final DirectoryBrowserPanel browser = new DirectoryBrowserPanel(startDir, "Directory Browser");
		browser.getList().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		doSetUp(inquisition, inquisitionFont, initialFiles, doneButton, browser, browser.getList());
	}

	public FileAdderPanel(String inquisition, File startDir, File[] initialFiles, FileFilter filter, boolean doneButton) {
		this(inquisition, null, startDir, initialFiles, filter, doneButton);
	}

	public FileAdderPanel(String inquisition, Font inquisitionFont, File startDir, File[] initialFiles, FileFilter filter,
			boolean doneButton) {
		super(new BorderLayout());
		if (startDir == null)
			startDir = FileSystemView.getFileSystemView().getHomeDirectory();
		final FileBrowserPanel browser = new FileBrowserPanel(filter, false, startDir);
		browser.getFileList().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		doSetUp(inquisition, inquisitionFont, initialFiles, doneButton, browser, browser.getFileList());
	}

	private void doSetUp(String inquisition, Font inqFont, File[] initialFiles, boolean doneButton, JComponent browser,
			final JList fileList) {
		JLabel instr = new JLabel(inquisition);
		instr.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		if (inqFont != null)
			instr.setFont(inqFont);
		else
			instr.setFont(new Font(instr.getFont().getName(), Font.PLAIN, 14));
		add(instr, BorderLayout.NORTH);
		JPanel majorComponents = new JPanel(new GridLayout(1, 2));
		majorComponents.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		majorComponents.add(browser);
		browser.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		if (initialFiles == null) {
			selectedList = new JFileList();
		} else {
			selectedList = new JFileList(initialFiles);
		}
		JPanel selectedPanel = new JPanel(new BorderLayout());
		selectedPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		JLabel titleLabel = null;
		if (browser instanceof DirectoryBrowserPanel)
			titleLabel = new JLabel("Added Folders");
		else
			titleLabel = new JLabel("Added Files");
		titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 14));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		selectedPanel.add(titleLabel, BorderLayout.NORTH);
		selectedPanel.add(new JScrollPane(selectedList), BorderLayout.CENTER);
		selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		majorComponents.add(selectedPanel);
		add(majorComponents, BorderLayout.CENTER);
		done = new JButton("Done");
		add = new JButton("Add Selected");
		remove = new JButton("Remove Selected");
		JPanel buttonsPanel = new JPanel(new GridLayout(1, doneButton ? 3 : 2));
		buttonsPanel.add(add);
		if (doneButton)
			buttonsPanel.add(done);
		buttonsPanel.add(remove);
		add(buttonsPanel, BorderLayout.SOUTH);
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Object o : fileList.getSelectedValues()) {
					selectedList.addFile((File) o);
				}
			}

		});
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Object o : selectedList.getSelectedValues())
					selectedList.removeFile((File) o);
			}

		});
	}

	public JButton getDoneButton() {
		return done;
	}

	public void addAddListener(ActionListener toAdd) {
		add.addActionListener(toAdd);
	}

	public void addRemoveListener(ActionListener toAdd) {
		remove.addActionListener(toAdd);
	}

	public JButton getAdd() {
		return add;
	}

	public JButton getRemove() {
		return remove;
	}

	public JFileList getFileList() {
		return selectedList;
	}
}
