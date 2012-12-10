package org.ThingsWithWorth.MetaMusic.FileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile;
import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile.MusicFileFactory;
import org.ThingsWithWorth.MetaMusic.Model.Album;
import org.ThingsWithWorth.MetaMusic.Model.Artist;
import org.ThingsWithWorth.MetaMusic.Model.MusicCollection;
import org.ThingsWithWorth.MetaMusic.Model.Song;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.thingswithworth.nicities.ObservableClass;
import org.thingswithworth.nicities.datastructure.ArrayListSet;
import org.thingswithworth.nicities.fileio.DirScanner;

public class MusicDataGatherer extends ObservableClass {
	private MusicCollection data;
	private ArrayListSet<File> folders;

	public MusicDataGatherer() {
		folders = new ArrayListSet<File>();
	}

	public void addFile(File folder) {
		folders.add(folder);
	}

	public MusicCollection doThreadedFindMusicFiles() {
		getMusicFinderWorker().execute();
		return data;
	}

	public SwingWorker<ArrayList<MusicFile>, File> getMusicFinderWorker() {
		SwingWorker<ArrayList<MusicFile>, File> finder = new SwingWorker<ArrayList<MusicFile>, File>() {

			@Override
			public void process(List<File> toProcess) {
				folders.addAll(toProcess);
			}

			@Override
			protected ArrayList<MusicFile> doInBackground() throws Exception {
				return findMusicFiles();
			}

		};
		return finder;
	}

	public ArrayList<MusicFile> findMusicFiles() {
		ArrayListSet<Song> alreadyGotten = data.getSongSet();
		ArrayList<MusicFile> newFound = new ArrayList<MusicFile>();
		for (int i = 0; i < folders.size(); i++) {
			File dir = folders.get(i);
			boolean skip = false;
			for (File potentialContainer : folders) {
				if (potentialContainer != dir && dir.getAbsolutePath().startsWith(potentialContainer.getAbsolutePath())) {
					skip = true;
					break;
				}
			}
			if (skip)
				continue;
			File[] musicFiles = DirScanner.recursiveFileFind(dir, MusicFileFactory.instance, true);
			for (File file : musicFiles) {
				if (!alreadyGotten.contains(file)) {
					MusicFile musicFile = (MusicFile) MusicFileFactory.instance.tryToMakeSmart(file);
					newFound.add(musicFile);
					this.addToCollection(musicFile);
				}
			}
		}
		return newFound;
	}

	public MusicCollection getCollection() {
		return data;
	}

	public void addToCollection(MusicFile file) {
		MusicDataGatherer.addToCollection(file, data);
	}

	public static void addToCollection(MusicFile file, MusicCollection collection) {
		Tag musicInfo = file.getTagData();
		String artistName = musicInfo.getFirst(FieldKey.ARTIST);
		String albumName = musicInfo.getFirst(FieldKey.ALBUM);
		String songName = musicInfo.getFirst(FieldKey.TITLE);
		Artist artist = new Artist(artistName);
		Album album = new Album(albumName);
		Song song = new Song(songName, file);
		album.addSongs(song);
		artist.addAlbums(album);
		collection.addArtists(artist);
	}
}
