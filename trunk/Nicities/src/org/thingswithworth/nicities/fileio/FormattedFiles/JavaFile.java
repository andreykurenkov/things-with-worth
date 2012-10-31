package org.thingswithworth.nicities.fileio.FormattedFiles;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

public class JavaFile extends SmartFile {
	public static final String TYPE = "Java";
	public static final String[] EXTENSIONS = { "java" };

	protected JavaFile(File f) {
		super(f);
	}

	protected JavaFile(String s) {
		super(s);
	}

	public boolean compile(Writer writer) {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, null);
		List<File> dir = new ArrayList<File>();
		dir.add(new File(this.getAbsolutePath().substring(0, this.getAbsolutePath().lastIndexOf('/'))));
		try {
			fm.setLocation(StandardLocation.CLASS_PATH, dir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		ArrayList<File> file = new ArrayList<File>(1);
		file.add(this);
		JavaCompiler.CompilationTask task = null;
		task = compiler.getTask(writer, fm, diagnostics, null, null, fm.getJavaFileObjectsFromFiles(file));

		boolean success = task.call();
		if (success) {
			return true;
		} else {
			for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics())
				try {
					if (writer != null)
						writer.append(d.toString());
					System.err.println(d.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return false;
		}
	}

	public static boolean compile(ArrayList<JavaFile> files, Writer writer) {
		List<File> dir = new ArrayList<File>();
		for (JavaFile jf : files) {
			dir.add(new File(jf.getDirectory().getAbsolutePath()));
		}
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
		StandardJavaFileManager fm = compiler.getStandardFileManager(diagnostics, null, null);

		try {
			fm.setLocation(StandardLocation.CLASS_PATH, dir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		JavaCompiler.CompilationTask task = null;
		task = compiler.getTask(writer, fm, diagnostics, null, null, fm.getJavaFileObjectsFromFiles(files));

		boolean success = task.call();
		if (success) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "Compilation not succesful", "Compilation error", JOptionPane.ERROR_MESSAGE);
			for (Diagnostic<? extends JavaFileObject> d : diagnostics.getDiagnostics())
				try {
					writer.append(d.toString());
					System.err.println(d.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			return false;
		}
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
		return new JavaFileFactory();
	}

	public static class JavaFileFactory extends SmartFileFactory {

		@Override
		protected SmartFile makeSmarter(File make) {
			return new JavaFile(make);
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