package org.ThingsWithWorth.MetaMusic.GUI;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.SwingWorker;

import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile;
import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile.MusicFileFactory;
import org.thingswithworth.nicities.datastructure.ArrayListSet;
import org.thingswithworth.nicities.fileio.DirScanner;
import org.thingswithworth.nicities.gui.components.FileAdderPanel;
import org.thingswithworth.nicities.gui.helpers.ObservableJPanel;

public class FilesPanel extends ObservableJPanel {
	private FileAdderPanel adder;
	private TreeSet<String> foundFiles;
	private ArrayListSet<MusicFile> foundMusicFiles;
	private ArrayListSet<File> searchFiles;
	public static String PROGRESS_STRING = "Music File found";

	public FilesPanel() {
		super(new BorderLayout());
		foundFiles = new TreeSet<String>();
		adder = new FileAdderPanel(
				"Add the folders containing pertinent music here - all contained folders will be searched.", null, null,
				true);
		searchFiles = new ArrayListSet<File>();
		this.add(adder, BorderLayout.CENTER);
	}

	public FileAdderPanel getAdder() {
		return adder;
	}

	public SortedSet<String> getFoundMusicFiles() {
		return foundFiles;
	}

	public ArrayList<MusicFile> doThreadedFindMusicFiles() {
		getMusicFinderWorker().execute();
		return foundMusicFiles;
	}

	public SwingWorker<ArrayList<MusicFile>, File> getMusicFinderWorker() {
		SwingWorker<ArrayList<MusicFile>, File> finder = new SwingWorker<ArrayList<MusicFile>, File>() {

			@Override
			public void process(List<File> toProcess) {
				searchFiles.addAll(toProcess);
			}

			@Override
			protected ArrayList<MusicFile> doInBackground() throws Exception {
				return findMusicFiles();
			}

		};
		return finder;
	}

	public ArrayList<MusicFile> findMusicFiles() {
		ArrayListSet<MusicFile> newFound = new ArrayListSet<MusicFile>();
		searchFiles.addAll(Arrays.asList(adder.getFileList().getFiles()));
		for (int i = 0; i < searchFiles.size(); i++) {
			File dir = searchFiles.get(i);
			boolean skip = false;
			for (File potentialContainer : adder.getFileList().getFiles()) {
				if (potentialContainer != dir && dir.getAbsolutePath().startsWith(potentialContainer.getAbsolutePath())) {
					skip = true;
					break;
				}
			}
			if (skip)
				continue;
			File[] musicFiles = DirScanner.recursiveFileFind(dir, MusicFile.MusicFileFactory.instance, true);
			for (File file : musicFiles) {
				if (!foundFiles.contains(file.getName())) {
					MusicFile musicFile = (MusicFile) MusicFileFactory.instance.tryToMakeSmart(file);
					newFound.add(musicFile);
					this.notifyObservers(PROGRESS_STRING, musicFile);
				}
			}
			foundMusicFiles.addAll(newFound);
			for (MusicFile mFile : newFound) {
				foundFiles.add(mFile.getName());
			}
		}
		return newFound;
	}
}
