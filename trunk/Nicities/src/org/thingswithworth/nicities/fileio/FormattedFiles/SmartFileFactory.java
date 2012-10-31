package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

public abstract class SmartFileFactory implements FileFilter {

	private final static boolean DEBUG = false;
	private final static ArrayList<SmartFileFactory> supported;
	static {
		SmartFileFactory[] defSupported = { new JavaFile.JavaFileFactory(), new CompiledJavaFile.CompiledJavaFileFactory(),
				new HtmlFile.HtmlFileFactory(), new DirectoryFile.DirectoryFileFactory(), new TxtFile.TxtFileFactory(),
				new CSVFile.CSVFileFactory(), };
		supported = new ArrayList<SmartFileFactory>();
		for (SmartFileFactory sff : defSupported)
			supported.add(sff);
	}

	public SmartFile tryToMakeSmart(File file) {
		if (isValid(file)) {
			return makeSmarter(file);
		}
		return null;
	}

	public boolean accept(File file) {
		return isValid(file);
	}

	protected abstract SmartFile makeSmarter(File file);

	public abstract boolean isValid(File file);

	protected static void addSupport(SmartFileFactory newSupported) {
		supported.add(newSupported);
	}

	@SuppressWarnings("unused")
	public static SmartFile smartify(File stupid) {
		ExtendedFile file = new ExtendedFile(stupid);
		for (SmartFileFactory entry : supported) {
			if (entry.isValid(stupid)) {
				return entry.makeSmarter(stupid);
			}
		}

		return null;
	}

	public static SmartFile[] smartify(File[] stupid) {
		SmartFile[] smart = new SmartFile[stupid.length];
		for (int i = 0; i < stupid.length; i++)
			smart[i] = SmartFileFactory.smartify(stupid[i]);
		return smart;
	}

}
