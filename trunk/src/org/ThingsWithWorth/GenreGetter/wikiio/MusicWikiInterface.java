package org.ThingsWithWorth.GenreGetter.wikiio;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicWikiInterface {

	public static String[] findAlbumGenres(String albumName) {
		return findAlbumGenres(albumName, null);
	}

	public static String[] findAlbumGenres(String albumName, String artistName) {
		MusicAlbumWikiParser[] parsers = MusicAlbumWikiParser.getParser(albumName, artistName);
		ArrayList<String> genres = new ArrayList<String>();
		if (parsers != null && parsers.length > 0) {
			for (MusicAlbumWikiParser parser : parsers) {
				String[] foundGenres = parser.getGenres();
				if (foundGenres != null && foundGenres.length > 0) {
					genres.addAll(Arrays.asList(foundGenres));
				}
			}
		}
		return genres.toArray(new String[genres.size()]);
	}

	public static String[] findArtistGenres(String artistName) {
		BandWikiParser[] parsers = BandWikiParser.getParser(artistName);
		ArrayList<String> genres = new ArrayList<String>();
		if (parsers != null && parsers.length > 0) {
			for (BandWikiParser parser : parsers) {
				String[] foundGenres = parser.getGenres();
				if (foundGenres != null && foundGenres.length > 0) {
					genres.addAll(Arrays.asList(foundGenres));
				}
			}
		}
		return genres.toArray(new String[genres.size()]);
	}
}
