package org.ThingsWithWorth.MetaMusic.Model;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import org.ThingsWithWorth.GenreGetter.io.fileio.MusicFile;
import org.thingswithworth.nicities.datastructure.ArrayListSet;

public class Song implements Comparable<Song>, TreeNode {
	private String name;
	private String description;
	private Album album;
	private ArrayListSet<Artist> additionalArtists;
	private ArrayListSet<Album> additionalAlbums;
	private MusicFile locInFileSystem;

	public Song(String name) {
		this(name, null);
	}

	public Song(String name, MusicFile inFile) {
		this.name = name;
		locInFileSystem = inFile;
		additionalArtists = new ArrayListSet<Artist>();
	}

	public void setAlbum(Album setTo) {
		this.album = setTo;
		if (setTo != null)
			setTo.addSongs(this);
	}

	@Override
	public Enumeration children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return album;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Song) {
			Song other = (Song) obj;
			if (other != null && other.name.equals(name)) {
				if (album != null && other.album != null) {
					return album.equals(other.album);
				} else {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(Song o) {
		return name.compareTo(o.name);
	}
}
