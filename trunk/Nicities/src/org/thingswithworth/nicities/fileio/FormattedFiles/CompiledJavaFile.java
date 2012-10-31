package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class CompiledJavaFile extends SmartFile {
	public static final String TYPE = "CompiledJava";
	public static final String[] EXTENSIONS = { "class" };

	protected CompiledJavaFile(File f) {
		super(f);
	}

	protected CompiledJavaFile(String s) {
		super(s);
	}

	public boolean isRunnable() {
		String name = this.getName();
		int index = 0;
		if ((index = name.indexOf('.')) <= 0)
			return false;
		if (!name.substring(index).equals(".class"))
			return false;
		try {
			URL[] load = { new File(this.getAbsolutePath().substring(0, this.getAbsolutePath().indexOf(this.getName())))
					.toURI().toURL() };
			URLClassLoader loader = new URLClassLoader(load);
			Class<?> myClass = loader.loadClass(this.getNonExtensionName());
			myClass.getMethod("main", String[].class);
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}

	public Process run() {
		if (this.isRunnable()) {
			ProcessBuilder builder = new ProcessBuilder("java", this.getNonExtensionName());
			builder.directory(this.getDirectory().getAbsoluteFile());
			try {
				Process runningProcess = builder.start();
				return runningProcess;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			System.err.println("Cannot run this file!");
		return null;
	}

	public ProcessBuilder getProcessRunner() {
		if (this.isRunnable()) {
			ProcessBuilder builder = new ProcessBuilder("java", this.getNonExtensionName());
			builder.directory(this.getDirectory().getAbsoluteFile());
			return builder;
		} else
			return null;

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
		return new CompiledJavaFileFactory();
	}

	public static class CompiledJavaFileFactory extends SmartFileFactory {

		protected SmartFile makeSmarter(File make) {
			return new CompiledJavaFile(make);
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
