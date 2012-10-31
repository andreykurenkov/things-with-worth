package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;

public class HtmlFile extends SmartFile {
	public static final String TYPE = "HTML";
	public static final String[] EXTENSIONS = { "html" };

	protected HtmlFile(File f) {
		super(f);
	}

	protected HtmlFile(String s) {
		super(s);
	}

	@Override
	public String[] getValidExtensionRegex() {
		return EXTENSIONS;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public SmartFileFactory getFactory() {
		return new HtmlFileFactory();
	}

	public static class HtmlFileFactory extends SmartFileFactory {

		@Override
		public SmartFile makeSmarter(File make) {
			return new HtmlFile(make);
		}

		@Override
		public boolean isValid(File file) {
			if (file != null)
				for (String exten : EXTENSIONS)
					if ((new ExtendedFile(file)).getExtension().equals(exten))
						return true;
			return false;
		}
	}

}