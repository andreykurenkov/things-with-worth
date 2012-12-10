package org.ThingsWithWorth.MetaMusic.Model;

import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.thingswithworth.nicities.datastructure.ArrayListSet;

public class MusicCollection extends DefaultTreeModel implements TreeNode {
	private ArrayListSet<Artist> artists;
	private ArrayListSet<Song> songs;

	public MusicCollection() {
		super(null, true);
		super.setRoot(this);
		artists = new ArrayListSet<Artist>();
	}

	public ArrayListSet<Artist> getArtists() {
		return artists;
	}

	public void addArtists(Artist... add) {
		for (Artist artist : add)
			if (artist != null)
				artists.add(artist);
	}

	@Override
	public Object getRoot() {
		return this;
	}

	@Override
	public boolean isLeaf(Object arg0) {
		return false;
	}

	@Override
	public Enumeration<Artist> children() {
		return new Enumeration<Artist>() {
			Iterator<Artist> iterator = artists.iterator();

			@Override
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			@Override
			public Artist nextElement() {
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
		return artists.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return artists.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return artists.indexOf(node);
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	public ArrayListSet<Song> getSongSet() {
		if (songs == null)
			songs = new ArrayListSet<Song>();
		for (Artist artist : artists) {
			for (Album album : artist.getAlbums()) {
				songs.addAll(album.getSongs());
			}
		}
		return songs;
	}
}
