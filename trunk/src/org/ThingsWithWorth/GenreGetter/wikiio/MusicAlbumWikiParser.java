package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.api.Page;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import org.ThingsWithWorth.Nicities.StringHelper;

public class MusicAlbumWikiParser extends WikiParserNoImageSupport {

	private final static boolean DEBUG = false;

	private final String[] infobox = { "name,genre" };

	private MusicAlbumWikiParser(Page toParse) {
		super(toParse);
	}

	public static MusicAlbumWikiParser[] getParser(String albumName, String artist, boolean calledAgain) {
		Page[] pages = attemptGetPage(albumName);
		if (pages == null || pages.length == 0) {
			if (calledAgain)
				return null;
			else {
				String titleCase = StringHelper.getInTitleCase(albumName);
				if (!albumName.equals(titleCase))
					return getParser(titleCase, artist, true);
				else
					return null;
			}
		}
		ArrayList<MusicAlbumWikiParser> parsers = new ArrayList<MusicAlbumWikiParser>();
		for (int i = 0; i < pages.length; i++) {
			String type = getPageType(pages[i]);
			if (DEBUG)
				System.out.println("Album infobox is " + WikiParser.getInfobox(pages[i]));
			if (type != null && type.toLowerCase().contains("album")) {
				parsers.add(new MusicAlbumWikiParser(pages[i]));
			} else {
				if (calledAgain)
					return null;
				MusicAlbumWikiParser[] precise = getParser(albumName + " (album)", null, true);
				if (precise != null)
					parsers.addAll(Arrays.asList(precise));
				if (artist != null) {
					precise = getParser(albumName + " (" + artist + "+album)", null, true);
					if (precise != null)
						parsers.addAll(Arrays.asList(precise));
				}

			}
		}
		return parsers.toArray(new MusicAlbumWikiParser[parsers.size()]);
	}

	public static MusicAlbumWikiParser[] getParser(String albumName, String artistName) {
		return getParser(albumName, artistName, false);
	}

	public static MusicAlbumWikiParser[] getParser(String albumName) {
		return getParser(albumName, null, false);
	}

	@Override
	public String[] infoboxSupport() {
		return infobox;
	}

	@Override
	public Object[] getInfoboxInfo(String forType) {
		if (Arrays.asList(infobox).contains(forType)) {
			if (forType.equals("genre"))
				return getGenres();
		}
		return null;
	}

	public Image getInfoboxImage() {
		return null;
	}

	public String[] getGenres() {
		if (DEBUG)
			System.out.println("Start MusicAlbumWikiParser;getGenres() : ");
		try {
			String infobox = this.getInfobox();
			if (infobox == null) {
				return null;
			}
			int start = infobox.toLowerCase().indexOf("genre");
			if (start < 0) {
				if (DEBUG)
					System.err.println("MusicAlbumParser;getGenres() error: no genre information");
				return null;
			}
			int end = infobox.indexOf("\n", start + 1);
			String searchIn = infobox.substring(start, end);
			searchIn = this.removeReferences(searchIn);
			if (DEBUG)
				System.out.println("MusicAlbumParser;getGenres(): searching in string" + searchIn);
			start = searchIn.indexOf("[[", 0);
			ArrayList<String> genres = new ArrayList<String>();
			while (start < searchIn.length() && start > 0) {
				end = searchIn.indexOf("]]", start + 1);
				String possibleGenre = searchIn.substring(start + 2, end);
				if (possibleGenre.contains("|"))
					genres.add(possibleGenre.substring(possibleGenre.indexOf("|") + 1));
				else
					genres.add(possibleGenre);
				start = searchIn.indexOf("[[", start + 1);
			}
			return genres.toArray(new String[genres.size()]);
		} catch (Exception exc) {
			System.err.println("Error encountered  with page \n" + this.parse.toString());
			exc.printStackTrace();
			return null;
		}
	}

}
