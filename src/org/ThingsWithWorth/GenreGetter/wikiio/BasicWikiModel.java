package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.namespaces.INamespace;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class BasicWikiModel extends WikiModel {

	public BasicWikiModel(Configuration configuration, Locale locale, String imageBaseURL, String linkBaseURL) {
		super(configuration, locale, imageBaseURL, linkBaseURL);
	}

	public BasicWikiModel(Configuration configuration, ResourceBundle resourceBundle, INamespace namespace,
			String imageBaseURL, String linkBaseURL) {
		super(configuration, resourceBundle, namespace, imageBaseURL, linkBaseURL);
	}

	public BasicWikiModel(Configuration configuration, String imageBaseURL, String linkBaseURL) {
		super(configuration, imageBaseURL, linkBaseURL);
	}

	public BasicWikiModel(String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
	}

	@Override
	public String getRawWikiContent(String namespace, String articleName, Map<String, String> templateParameters) {
		String rawContent = super.getRawWikiContent(namespace, articleName, templateParameters);

		if (rawContent == null) {
			return "";
		} else {
			return rawContent;
		}
	}
}