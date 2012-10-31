package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;

import javax.swing.filechooser.FileSystemView;

public abstract class SmartFile extends ExtendedFile {

	protected SmartFile(String path) {
		super(path);
	}

	protected SmartFile(File f) {
		this(f.getAbsolutePath());
	}

	protected SmartFile() {
		super(FileSystemView.getFileSystemView().getHomeDirectory());
	}

	public abstract String[] getValidExtensionRegex();

	public abstract String getType();

	public abstract boolean isReadable();

	public abstract SmartFileFactory getFactory();

	public String toString() {
		return this.getName();
	}

}
