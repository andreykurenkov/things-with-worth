package org.ThingsWithWorth.MetaMusic.Model;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.TreeNode;

import org.thingswithworth.nicities.datastructure.ArrayListSet;

public class Album implements Comparable<Album>, TreeNode {
	private int year;
	private String description;
	private String mainGenre;
	private ArrayListSet<String> genres;
	private String name;
	private Artist artist;
	private ArrayListSet<Artist> additionalArtists;
	private ArrayListSet<Song> songs;
	private boolean isSingle;

	public Album(String name) {
		this.name = name;
		genres = new ArrayListSet<String>();
		additionalArtists = new ArrayListSet<Artist>();
		songs = new ArrayListSet<Song>();
	}

	public ArrayListSet<Song> getSongs() {
		return songs;
	}

	public void setArtist(Artist setTo) {
		this.artist = setTo;
		if (setTo != null)
			setTo.addAlbums(this);
	}

	public void addSongs(Song... add) {
		for (Song song : add)
			if (song != null)
				songs.add(song);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Album) {
			Album other = (Album) obj;
			if (other != null && other.name.equals(name)) {
				if (artist != null && other.artist != null) {
					return artist.equals(other.artist);
				} else {
					for (Song otherSong : other.songs)
						if (!songs.contains(otherSong))
							return false;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(Album o) {
		return name.compareTo(o.name);
	}

	@Override
	public Enumeration<Song> children() {
		return new Enumeration<Song>() {
			Iterator<Song> iterator = songs.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Song nextElement() {
				return iterator.next();
			}

		};

	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return songs.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return songs.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return songs.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return artist;
	}

	@Override
	public boolean isLeaf() {
		return songs.size() == 0;
	}

}
