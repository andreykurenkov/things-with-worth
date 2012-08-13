package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.api.Page;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class WikiParser {
	public static final String DISAMBIG_TYPE = "disambig";
	protected Page parse;
	protected static final WikiInterface standardInterface = new WikiInterface(WikiInterface.engWikipedia);

	public WikiParser(Page toParse) {
		parse = toParse;
	}

	protected String removeReferences(String from) {
		return from.replaceAll("<ref>.*</ref>", "");
	}

	public abstract String[] infoboxSupport();

	public abstract Object[] getInfoboxInfo(String forType);

	public abstract boolean imageSupport();

	public abstract String[] retrieveImageNames();

	public abstract Image[] retrieveImages();

	public String getInfobox() {
		return getInfobox(parse);
	}

	public static String getInfobox(Page parse) {
		String content = parse.toString();
		int start = content.indexOf("{{Infobox");
		if (start < 0) {
			return null;
		}
		int nextStart = content.indexOf("{{", start + 1);
		int end = content.indexOf("}}", start + 1);
		while (nextStart < end && nextStart != -1) {
			nextStart = content.indexOf("{{", nextStart + 1);
			end = content.indexOf("}}", end + 1);
		}
		try {
			return content.substring(start, end);
		} catch (Exception e) {
			System.err.println("Cant get infobox" + content);
			e.printStackTrace();
			return null;
		}
	}

	public String[] getHeadings(Page toRender) {
		String pageContent = toRender.toString();
		ArrayList<String> headings = new ArrayList<String>();
		String currentMatch = "==";
		headings.addAll(getInsideHeadings("", currentMatch, pageContent, 0));
		return headings.toArray(new String[headings.size()]);
	}

	public Page getPage() {
		return parse;
	}

	public static Page[] attemptGetPage(String name) {
		List<Page> pages = WikiParser.standardInterface.getPages(name);
		if (pages == null)
			return null;
		ArrayList<Page> validPages = new ArrayList<Page>();
		if (pages.size() == 0)
			return null;
		Page possibleNull = pages.get(0);
		String pageContent = possibleNull.toString();
		String pageId = pageContent.substring(pageContent.indexOf(":") + 1, pageContent.indexOf(";")).trim();
		if (pageId.equals("null"))
			return null;
		for (int i = 0; i < pages.size(); i++) {
			pageContent = pages.get(i).toString();
			if (pageContent.contains("#REDIRECT")) {
				String redirectLine = pageContent.substring(pageContent.indexOf("#REDIRECT"));
				String redirectName = redirectLine.substring(redirectLine.indexOf("[[") + 2, redirectLine.indexOf("]]"));
				Page[] redirectPages = attemptGetPage(redirectName);
				if (redirectPages != null)
					validPages.addAll(Arrays.asList(redirectPages));
			} else
				validPages.add(pages.get(i));
		}
		return validPages.toArray(new Page[validPages.size()]);
	}

	public static String getPageType(Page of) {
		String infobox = getInfobox(of);
		if (infobox == null) {
			if (of.toString().contains("{{disambing}}"))
				return "disambig";
			return null;
		}
		String firstLine = infobox.substring(0, infobox.indexOf('\n'));
		int firstSpace = firstLine.indexOf(' ');
		if (firstLine.contains("<!")) {
			if (!firstLine.contains("|"))
				return firstLine.substring(firstSpace + 1, firstLine.indexOf("<!")).trim();
			else
				return firstLine.substring(firstSpace + 1, firstLine.indexOf("|")).trim();

		} else {
			if (!firstLine.contains("|"))
				return firstLine.substring(firstSpace + 1).trim();
			else
				return firstLine.substring(firstSpace + 1, firstLine.indexOf("|")).trim();
		}
	}

	public String getPageType() {
		return getPageType(parse);
	}

	private ArrayList<String> getInsideHeadings(String nestingHeading, String currentMatch, String pageContent,
			int headingOpen) {
		ArrayList<String> headings = new ArrayList<String>();
		headingOpen = pageContent.indexOf(currentMatch, headingOpen);
		while (headingOpen < pageContent.length() && headingOpen > 0) {
			int headingClose = pageContent.indexOf(currentMatch, headingOpen + 1);
			String currentHeading = pageContent.substring(headingOpen + 2, headingClose);
			if (nestingHeading.length() > 0)
				currentHeading = nestingHeading + ";" + currentHeading;
			headings.add(currentHeading);
			headings.addAll(getInsideHeadings(currentHeading, currentMatch + "=", pageContent, headingClose + 2));
		}
		return headings;
	}
}
