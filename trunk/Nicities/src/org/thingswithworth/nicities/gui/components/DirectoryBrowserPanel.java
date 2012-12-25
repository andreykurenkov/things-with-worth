package org.thingswithworth.nicities.gui.components;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileSystemView;

import org.thingswithworth.nicities.Observer;
import org.thingswithworth.nicities.gui.helpers.Dialogs;

public class DirectoryBrowserPanel extends JPanel {
	private JFileList currentFolders;
	private JScrollPane dirScroll;
	private Stack<File> previous;
	private Stack<File> next;
	private BackActionNextButtons previousNext;
	private JButton setDir;
	private String keyboardInput;

	public DirectoryBrowserPanel() {
		this(FileSystemView.getFileSystemView().getHomeDirectory());
	}

	public DirectoryBrowserPanel(String title) {
		this(FileSystemView.getFileSystemView().getHomeDirectory(), title);
	}

	public DirectoryBrowserPanel(File startDir) {
		this(startDir, null);
	}

	public DirectoryBrowserPanel(File startDir, String title) {
		super(new BorderLayout());
		previous = new Stack<File>();
		next = new Stack<File>();
		FileFilter dirFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.isDirectory() && !file.isHidden();
			}

		};
		currentFolders = new JFileList(dirFilter, startDir);
		// TODO: make a non-hardcoded icon management thing somehow
		setDir = new JButton("Set Directory", new ImageIcon("Res/openFolder.png"));
		previousNext = new BackActionNextButtons("", false);
		previousNext.getAction().setIcon(new ImageIcon("Res/UpArrow.png"));
		previousNext.getBack().setEnabled(false);
		previousNext.getNext().setEnabled(false);
		dirScroll = new JScrollPane(currentFolders);
		previousNext.addBackListener(new StackListener(previous));
		previousNext.addNextListener(new StackListener(next));
		previousNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File newDir = currentFolders.getCurrentDirectory().getParentFile();
				if (newDir != null) {
					addBackStack();
					currentFolders.changeDirectory(newDir);
					if (newDir.getParentFile() == null) {
						previousNext.getAction().setEnabled(false);
					}
				}
			}
		});
		setDir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File newDir = Dialogs.getFile(null, "Select new directory",
						"Browse to new directory and click open with the desired folder selected.",
						currentFolders.getCurrentDirectory(), JFileChooser.DIRECTORIES_ONLY);
				if (newDir != null && !newDir.equals(currentFolders.getCurrentDirectory())) {
					addBackStack();
					currentFolders.changeDirectory(newDir);
				}
			}
		});
		currentFolders.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				keyboardInput = "";
				if (evt.getClickCount() == 2) {
					addBackStack();
					currentFolders.changeDirectory((File) currentFolders.getSelectedValue());
				}
			}
		});
		currentFolders.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent event) {
				keyboardInput += event.getKeyChar();
				File found = null;
				for (File in : currentFolders.getFiles()) {
					if (in.getName().toLowerCase().startsWith(keyboardInput.toLowerCase())) {
						found = in;
						break;
					}
				}
				if (found != null)
					currentFolders.setSelectedValue(found, true);
			}

		});
		currentFolders.acceptObserver(new Observer<JFileList>() {

			@Override
			public void acceptNotification(JFileList source, String code, Object... files) {
				if (code.equals(JFileList.CHANGED_DIR_CODE)) {
					previousNext.getAction().setEnabled(((File) files[0]).getParentFile() != null);
				}

			}

		});
		currentFolders.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		currentFolders.setCellRenderer(new JFileList.FileIconRenderer());
		JPanel topPanel = new JPanel(new BorderLayout());
		if (title != null) {
			JLabel titleLabel = new JLabel(title);
			titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 14));
			titleLabel.setHorizontalAlignment(JLabel.CENTER);
			topPanel.add(titleLabel, BorderLayout.NORTH);
		}
		topPanel.add(previousNext, BorderLayout.CENTER);
		topPanel.add(setDir, BorderLayout.SOUTH);
		topPanel.setBorder(BorderFactory.createEmptyBorder());
		currentFolders.setBorder(BorderFactory.createEmptyBorder());
		this.add(topPanel, BorderLayout.NORTH);
		this.add(dirScroll, BorderLayout.CENTER);
	}

	public File getCurrentDirectory() {
		return currentFolders.getCurrentDirectory();
	}

	public void changeToDirectory(File newDirectory) {
		if (newDirectory != null)
			currentFolders.changeDirectory(newDirectory);
	}

	public JFileList getList() {
		return currentFolders;
	}

	private void addBackStack() {
		previous.add(currentFolders.getCurrentDirectory());
		previousNext.getBack().setEnabled(true);
		next.clear();
		previousNext.getNext().setEnabled(false);

	}

	public class StackListener implements ActionListener {
		private Stack<File> theStack;

		public StackListener(Stack<File> forStack) {
			this.theStack = forStack;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				File newDir = theStack.pop();
				if (theStack == previous) {
					next.add(currentFolders.getCurrentDirectory());
					previousNext.getNext().setEnabled(true);
					if (theStack.empty())
						previousNext.getBack().setEnabled(false);
				} else {
					previous.add(currentFolders.getCurrentDirectory());
					previousNext.getBack().setEnabled(true);
					if (theStack.empty())
						previousNext.getNext().setEnabled(false);
				}
				currentFolders.changeDirectory(newDir);
			} catch (EmptyStackException ex) {
				System.err.println("DirectoryBrowser;StackListener: back or next clicked when empty - should be disabled");
			}

		}
	}

}
