package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;

public class DirectoryFile extends SmartFile {
	public static final String TYPE = "Directory";
	public static final String[] EXTENSIONS = { "" };

	protected DirectoryFile(File f) {
		super(f);
	}

	protected DirectoryFile(String s) {
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
		return false;
	}

	@Override
	public SmartFileFactory getFactory() {
		return new DirectoryFileFactory();
	}

	public static class DirectoryFileFactory extends SmartFileFactory {

		@Override
		protected SmartFile makeSmarter(File make) {
			return new DirectoryFile(make);
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
