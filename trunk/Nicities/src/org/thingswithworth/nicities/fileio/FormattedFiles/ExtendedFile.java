package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ExtendedFile extends File {

	public ExtendedFile(String pathname) {
		super(pathname);
	}

	public ExtendedFile(File f) {
		this(f.getAbsolutePath());
	}

	public File getDirectory() {
		if (this.isDirectory())
			return this;
		String path = this.getAbsolutePath();
		return new File(path.substring(0, path.indexOf(this.getNonExtensionName())));
	}

	public String getNonExtensionName() {
		String name = this.getName();
		int index = 0;
		if ((index = name.indexOf('.')) > 0)
			return name.substring(0, index);
		return name;
	}

	public String getExtension() {
		String name = this.getName();
		int index = 0;
		if ((index = name.lastIndexOf('.')) > 0)
			return name.substring(index + 1);
		return "";
	}

	public String read() {
		if (this.canRead()) {
			Scanner scan = null;
			try {
				scan = new Scanner(this);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String res = "";
			String next = "";
			while (scan.hasNextLine())
				if ((next = scan.nextLine()) != null && next.length() > 0)
					res += "\n" + next;
			return res;
		} else
			return null;
	}

	public void copyTo(File to) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(this));
		BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(to));
		int c;

		while ((c = in.read()) != -1)
			out.write(c);

		in.close();
		out.close();
	}

	public String toString() {
		return this.getName();
	}

}
