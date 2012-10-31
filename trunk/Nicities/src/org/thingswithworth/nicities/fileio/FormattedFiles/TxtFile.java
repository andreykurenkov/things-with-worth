package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;

public class TxtFile extends SmartFile {
	public static final String TYPE = "Txt";
	public static final String[] EXTENSIONS = { "txt" };

	protected TxtFile(File f) {
		super(f);
	}

	protected TxtFile(String s) {
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
		return new TxtFileFactory();
	}

	public static class TxtFileFactory extends SmartFileFactory {

		@Override
		protected SmartFile makeSmarter(File make) {
			return new TxtFile(make);
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