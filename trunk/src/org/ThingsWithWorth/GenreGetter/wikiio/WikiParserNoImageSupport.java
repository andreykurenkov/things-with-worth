package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.api.Page;

import java.awt.Image;

public abstract class WikiParserNoImageSupport extends WikiParser {

	public WikiParserNoImageSupport(Page toParse) {
		super(toParse);
	}

	@Override
	public boolean imageSupport() {
		return false;
	}

	@Override
	public String[] retrieveImageNames() {
		return null;
	}

	@Override
	public Image[] retrieveImages() {
		return null;
	}

}
