package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;

//TODO
public class CSVFile extends SmartFile {
	public static final String TYPE = "Comma Seperated";
	public static final String[] EXTENSIONS = { "csv" };

	protected CSVFile(File f) {
		super(f);
	}

	protected CSVFile(String s) {
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
		return new CSVFileFactory();
	}

	public static class CSVFileFactory extends SmartFileFactory {

		@Override
		protected SmartFile makeSmarter(File make) {
			return new CSVFile(make);
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
