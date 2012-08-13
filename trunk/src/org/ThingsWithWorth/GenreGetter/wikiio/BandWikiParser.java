package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.api.Page;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;

import org.ThingsWithWorth.Nicities.StringHelper;

public class BandWikiParser extends WikiParserNoImageSupport {
	private final static boolean DEBUG = false;

	private final String[] infobox = { "name,image,genre,website" };

	private BandWikiParser(Page toParse) {
		super(toParse);
	}

	public static BandWikiParser[] getParser(String bandName) {
		return getParser(bandName, false);
	}

	public static BandWikiParser[] getParser(String bandName, boolean calledAgain) {
		Page[] pages = attemptGetPage(bandName);
		if (pages == null || pages.length == 0) {
			if (calledAgain)
				return null;
			else {
				String titleCase = StringHelper.getInTitleCase(bandName);
				if (!bandName.equals(titleCase))
					return getParser(titleCase, true);
				else
					return null;
			}
		}
		ArrayList<BandWikiParser> parsers = new ArrayList<BandWikiParser>();
		for (int i = 0; i < pages.length; i++) {
			String type = getPageType(pages[i]);
			if (DEBUG)
				System.out.println("Artist infobox is " + WikiParser.getInfobox(pages[i]));
			if (type != null && type.toLowerCase().trim().contains("musical artist")) {
				parsers.add(new BandWikiParser(pages[i]));
			} else {
				if (calledAgain)
					return null;
				BandWikiParser[] precise = getParser(bandName + " (band)", true);
				if (precise != null)
					parsers.addAll(Arrays.asList(precise));
			}
		}
		return parsers.toArray(new BandWikiParser[parsers.size()]);
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
			System.out.println("Start BandWikiParser;getGenres() : ");
		try {
			String infobox = this.getInfobox();
			if (infobox != null) {
				int start = infobox.toLowerCase().indexOf("genre");
				if (start < 0) {
					if (DEBUG)
						System.err.println("BandWikiParser;getGenres() error: no genre information");
					return null;
				}

				int end = infobox.indexOf("\n", start + 1);
				String searchIn = infobox.substring(start, end);
				searchIn = this.removeReferences(searchIn);
				if (DEBUG)
					System.out.println("BandWikiParser;getGenres(): searcihing in string" + searchIn);
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
			} else {
				System.err.println("Infobox null for page " + this.parse.toString());
				return null;
			}
		} catch (Exception exc) {
			System.err.println("Error encountered  with page \n" + this.parse.toString());
			exc.printStackTrace();
			return null;
		}
	}
}
