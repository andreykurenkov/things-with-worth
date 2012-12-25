package org.thingswithworth.nicities.gui.components;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileSystemView;

import org.thingswithworth.nicities.Observable;
import org.thingswithworth.nicities.Observer;
import org.thingswithworth.nicities.fileio.FormattedFiles.ExtendedFile;

public class JFileList extends JList implements Observable<JFileList> {
	private FileFilter filter;
	private File currentDir;
	private ArrayList<File> files;
	private File lastOpened;
	private ArrayList<Observer<JFileList>> observers;

	public static final String CHANGED_DIR_CODE = "JFileList Changed Directory";
	public static final String CHANGE_OPEN_CODE = "JFileList new File Opened";
	public static final String CHANGE_ADD_FILE = "JFileList new File Added";
	public static final String CHANGE_REM_FILE = "JFileList new File Removed";
	private final static FileFilter ANY_FILE = new FileFilter() {
		public boolean accept(File file) {
			return file != null;
		}
	};

	public JFileList() {
		this(ANY_FILE);
	}

	public JFileList(File initDir) {
		this(ANY_FILE, initDir);
	}

	public JFileList(FileFilter filter) {
		this(filter, null);
	}

	public JFileList(FileFilter filter, File startDir) {
		observers = new ArrayList<Observer<JFileList>>();
		files = new ArrayList<File>();
		this.filter = filter;
		this.setCellRenderer(new FileIconRenderer());
		if (startDir != null) {
			this.changeDirectory(startDir);
		} else {
			currentDir = FileSystemView.getFileSystemView().getHomeDirectory();
		}
	}

	public JFileList(File[] files) {
		super(files);
		observers = new ArrayList<Observer<JFileList>>();
		this.files = new ArrayList<File>();
		this.filter = ANY_FILE;
		currentDir = FileSystemView.getFileSystemView().getHomeDirectory();
	}

	public void addFile(File add, boolean allowMultiple) {
		if (allowMultiple || !files.contains(add)) {
			files.add(add);
			this.setListData(files.toArray());
			this.notifyObservers(CHANGE_ADD_FILE, add);
		}
	}

	public void addFile(File add) {
		addFile(add, false);
	}

	public void removeFile(File add) {
		if (files.contains(add)) {
			files.remove(add);
			this.setListData(files.toArray());
			this.notifyObservers(CHANGE_REM_FILE, add);
		}
	}

	public void changeDirectory(File toDir, boolean overrwrite) {
		if (toDir != null) {
			this.currentDir = toDir;
			if (overrwrite)
				this.setDirFiles();
			this.notifyObservers(JFileList.CHANGED_DIR_CODE, toDir);
		}
	}

	public void changeDirectory(File toDir) {
		changeDirectory(toDir, true);
	}

	public File getCurrentDirectory() {
		return currentDir;
	}

	public File[] getFiles() {
		return files.toArray(new File[files.size()]);
	}

	public void setDirFiles() {
		files.clear();
		for (File file : currentDir.listFiles(filter))
			files.add(file);
		this.setListData(currentDir.listFiles(filter));
	}

	public File getLastOpened() {
		return lastOpened;
	}

	public void addDoubleClickListener(final ActionListener eventHandler) {
		if (eventHandler != null) {
			this.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					if (evt.getClickCount() == 2) {
						ActionEvent event = new ActionEvent(this, 22, "DoubleClicked");
						eventHandler.actionPerformed(event);
					}
				}
			});
		}
	}

	public void updateOpened(File updateTo) {
		if (updateTo == null || Arrays.asList(files).contains(updateTo)) {
			this.lastOpened = updateTo;
			this.notifyObservers(JFileList.CHANGE_OPEN_CODE, updateTo);
		}
	}

	private void notifyObservers(String code, Object... args) {
		for (Observer<JFileList> obv : observers)
			obv.acceptNotification(this, code, args);
	}

	@Override
	public void acceptObserver(Observer<JFileList> add) {
		this.observers.add(add);
	}

	@Override
	public void deleteObserver(Observer<JFileList> add) {
		this.observers.remove(add);

	}

	@Override
	public void deleteObservers() {
		observers = new ArrayList<Observer<JFileList>>();
	}

	@Override
	public int countObservers() {
		return observers.size();
	}

	public void saveToFile(File saveTo) {
		try {
			saveTo.delete();
			saveTo.createNewFile();
			FileWriter writer = new FileWriter(saveTo);
			for (File file : getFiles()) {
				writer.write(file.getCanonicalPath() + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class FileNameRenderer implements ListCellRenderer {
		private ListCellRenderer normalRenderer;

		public FileNameRenderer() {
			JList defaultList = new JList();
			normalRenderer = defaultList.getCellRenderer();
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object cell, int index, boolean selected, boolean focus) {
			JLabel defaultRender = (JLabel) normalRenderer.getListCellRendererComponent(list, cell, index, selected, focus);
			defaultRender.setText(((File) cell).getName());
			return defaultRender;
		}
	}

	public static class FileIconRenderer extends FileNameRenderer {
		private static final ImageIcon smallFolderBW = new ImageIcon("Res/smallFolderBW.png");
		private static final ImageIcon smallFolder = new ImageIcon("Res/smallFolder.png");
		private static final ImageIcon whiteFile = new ImageIcon("Res/whiteFile.png");
		private static final ImageIcon blackFile = new ImageIcon("Res/blackFile.png");

		@Override
		public Component getListCellRendererComponent(JList list, Object cell, int index, boolean selected, boolean focus) {
			JLabel defaultRender = (JLabel) super.getListCellRendererComponent(list, cell, index, selected, focus);
			File file = (File) cell;
			if (selected) {
				if (file.isDirectory())
					defaultRender.setIcon(smallFolder);
				else
					defaultRender.setIcon(whiteFile);
			} else {
				if (file.isDirectory())
					defaultRender.setIcon(smallFolderBW);
				else
					defaultRender.setIcon(blackFile);
			}
			return defaultRender;
		}
	}

	public static class SaveListener implements Observer<JFileList> {
		private ExtendedFile saveTo;

		public SaveListener(File saveTo) {
			this.saveTo = new ExtendedFile(saveTo);
		}

		@Override
		public void acceptNotification(JFileList source, String code, Object... arg) {
			if (code.equals(JFileList.CHANGE_ADD_FILE) || code.equals(JFileList.CHANGE_REM_FILE)) {
				try {
					saveTo.delete();
					saveTo.createNewFile();
					FileWriter writer = new FileWriter(saveTo);
					for (File file : source.getFiles()) {
						writer.write(file.getCanonicalPath() + "\n");
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
