package org.ThingsWithWorth.MetaMusic.Model;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.TreeNode;

import org.thingswithworth.nicities.datastructure.ArrayListSet;

public class Artist implements Comparable<Artist>, TreeNode {
	private String name;
	private String description;
	private int startYear, endYear;
	private MusicCollection collection;
	private ArrayListSet<String> genres;
	private ArrayListSet<Album> albums;
	private ArrayListSet<Album> guestAlbums;
	private ArrayListSet<Song> guestSongs;

	public Artist(String name) {
		this.name = name;
		genres = new ArrayListSet<String>();
		albums = new ArrayListSet<Album>();
		guestAlbums = new ArrayListSet<Album>();
		guestSongs = new ArrayListSet<Song>();
	}

	public void setCollection(MusicCollection setTo) {
		this.collection = setTo;
		if (setTo != null)
			setTo.addArtists(this);
	}

	public void addToCollection(MusicCollection... collections) {
		for (MusicCollection collection : collections)
			if (collection != null) {
				collection.addArtists(this);
			}
	}

	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void addAlbums(Album... toAdd) {
		for (Album album : toAdd) {
			if (album != null)
				albums.add(album);
		}
	}

	public void addGenres(String... toAdd) {
		for (String genre : toAdd) {
			if (genre != null)
				genres.add(genre);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ArrayListSet<Album> getAlbums() {
		return albums;
	}

	public ArrayListSet<Album> getGuestAlbums() {
		return guestAlbums;
	}

	public ArrayListSet<String> getGenres() {
		return genres;
	}

	public ArrayListSet<Song> getGuestSongs() {
		return guestSongs;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Artist))
			return false;
		Artist o = (Artist) obj;
		if (!o.name.equals(name))
			return false;
		for (Album otherAlbum : o.albums) {
			if (!albums.contains(otherAlbum))
				return false;
		}
		return true;
	}

	@Override
	public int compareTo(Artist o) {
		return name.compareTo(o.name);
	}

	@Override
	public Enumeration<Album> children() {
		return new Enumeration<Album>() {
			Iterator<Album> iterator = albums.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Album nextElement() {
				return iterator.next();
			}

		};

	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int index) {
		return albums.get(index);
	}

	@Override
	public int getChildCount() {
		return albums.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return albums.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return collection;
	}

	@Override
	public boolean isLeaf() {
		return albums.size() == 0;
	}

}
