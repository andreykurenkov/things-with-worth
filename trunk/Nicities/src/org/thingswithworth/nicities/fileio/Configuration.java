package org.thingswithworth.nicities.fileio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.thingswithworth.nicities.exceptions.WrongClassTypeException;

/**
 * This is a simple representation of a generic configuration of a module or the entire simulator. It extends Java's
 * Properties class and so can store in XML as well as plaintext files. A default configuration file is specified, though
 * this supports other files as well. This class can also be used for temporary runtime configuration that is never saved to
 * disk.
 * 
 * @author Andrey Kurenkov (akurenkov3@gatech.edu)
 * @version 1.0
 */
public class Configuration extends Properties {
	private File propertyFile;
	private boolean isXML;
	private static final String PATH_TO_DEFAULT = "conf" + System.getProperties().getProperty("file.seperator")
			+ "default_conf.xml";
	private static Configuration globalConfig;
	private static final File DEFAULT_FILE = new File(PATH_TO_DEFAULT);

	/**
	 * Plain constructor that creates a configuration not attached to any file on the filesystem and containing no values.
	 */
	public Configuration() {
		super();
	}

	/**
	 * Simple constructor that creates an empty set of configuration not associated with any file. If any keys are retrieved
	 * that have not been entered into this config, it will search defaultVals and return either a value inside that or null.
	 * 
	 * @param defaultVals
	 *            the default values to use for retrieval
	 */
	public Configuration(Configuration defaultVals) {
		super(defaultVals);
	}

	/**
	 * More general constructor for existing configuration file. The code that supplies this file is expected to handle
	 * exceptions with it not existing or not actually being a configuration file (as in, saved from Configuration itself
	 * previously).
	 * 
	 * @param loadFrom
	 *            the file to load the Configuration from. Must be the result of calling the write method of Configuration
	 *            previously or already formatted correctly for Java's Properties class.
	 * @throws InvalidPropertiesFormatException
	 *             See the JDK Properties class
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Configuration(File loadFrom) throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		propertyFile = loadFrom;
		isXML = loadFrom.getName().endsWith(".xml");

		if (isXML)
			loadFromXML(new FileInputStream(loadFrom));
		else
			load(new FileInputStream(loadFrom));
	}

	/**
	 * Static getter/intializer for a configuration based on a hardcoded default file. Since the default file is specified
	 * and constant, it is expected to exist and programmers using it should generally not handle the case where there are
	 * exceptions(unlike the public constructor). Problems with the default file are thus logged but not generally expected
	 * and so not thrown.
	 * 
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public static Configuration loadDefault() {
		if (DEFAULT_FILE.exists()) {
			try {
				Configuration config = new Configuration(DEFAULT_FILE);
				return config;
			} catch (InvalidPropertiesFormatException e) {
				// TODO handle logging in proper format
				e.printStackTrace();
			} catch (IOException e) {
				// TODO handle logging in proper format
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Sets a new static global configuration, as long as the configuration is not null
	 * 
	 * @param newGlobal
	 *            the new global Configuration
	 * @throws NullPointerException
	 *             is the given Configuration is null
	 */
	public static void setGlobalConfig(Configuration newGlobal) throws NullPointerException {
		if (newGlobal != null)
			globalConfig = newGlobal;
		else
			throw new NullPointerException("Trying to set a null global configuration is not allowed");
	}

	/**
	 * Retrieves the global configuration. If it has not been set yet, it loads the default configuration file and sets that
	 * to be global before returning it.
	 * 
	 * @return the static global configuration
	 */
	public static Configuration getGlobalConfig() {
		if (globalConfig == null)
			globalConfig = Configuration.loadDefault();
		return globalConfig;
	}

	/**
	 * Retrieves a boolean value associated with the given key. Boolean values are stored with the toString of the boolean
	 * class and parsed by that class. If the key is not found this returns null (thus the Boolean return type).
	 * 
	 * @param key
	 *            the key to retrieve the value for
	 * @param defaultValue
	 *            the default value to return
	 * @return
	 */
	public Boolean getBoolean(String key) {
		return (containsKey(key) ? Boolean.parseBoolean(getProperty(key)) : null);
	}

	/**
	 * Attempts to retrieve a class from the classpath given the name of the key where its name is stored.
	 * 
	 * @param key
	 *            the key to which the classname is associated
	 * @return the class retrieved through reflection on the classpath or null if the key is not stored
	 * @throws ClassNotFoundException
	 */
	public Class<?> getClassByName(String key) throws ClassNotFoundException {
		if (!containsKey(key))
			return null;
		return Class.forName(getProperty(key));
	}

	/**
	 * Attempts to retrieve a class given the name of the key where its name is stored.
	 * 
	 * @param key
	 *            the key to which the classname is associated
	 * @return the class retrieved through reflection on the classpath or null if the key is not stored
	 * @throws ClassNotFoundException
	 */
	public Class<?> getClassByName(String key, URLClassLoader loader) throws ClassNotFoundException {
		if (!containsKey(key))
			return null;
		return loader.loadClass(getProperty(key));
	}

	/**
	 * Utility method to get a property as a File.
	 * 
	 * @param key
	 *            the key of the property
	 * @return null if key not contained, or file with the path as the valueif it is contained
	 */
	public File getFile(String key) {
		if (!containsKey(key))
			return null;
		else
			return new File(getProperty(key));
	}

	/**
	 * Utility method to get property parsed to a double.
	 * 
	 * @param key
	 *            key to get value for
	 * @return the double value if right format and key contained, false otherwise
	 */
	public Double getDouble(String key) {
		if (!containsKey(key))
			return null;
		else {
			try {
				return Double.parseDouble(getProperty(key));

			} catch (Exception e) {
				e.printStackTrace();// TODO: proper logging
				return null;
			}
		}
	}

	/**
	 * Utility method to get property parsed to an int.
	 * 
	 * @param key
	 *            key to get value for
	 * @return the int value if right format and key contained, false otherwise
	 */
	public Integer getInt(String key) {
		if (!containsKey(key))
			return null;
		else {
			try {
				return Integer.parseInt(getProperty(key));
			} catch (Exception e) {
				e.printStackTrace();// TODO: proper logging
				return null;
			}
		}
	}

	/**
	 * Utility method to get property parsed to a long.
	 * 
	 * @param key
	 *            key to get value for
	 * @return the long value if right format and key contained, false otherwise
	 */
	public Long getLong(String key) {
		if (!containsKey(key))
			return null;
		else {
			try {
				return Long.parseLong(getProperty(key));
			} catch (Exception e) {
				e.printStackTrace();// TODO: proper logging
				return null;
			}
		}
	}

	/**
	 * If key is associated with a series of comma seperated Strings, this returns those Strings in an array without commas
	 * 
	 * @param key
	 *            the key to get strings for
	 * @return an array of Strings, or null if the key is invalid
	 */
	public String[] getStrings(String key) {
		if (!containsKey(key))
			return null;
		return getProperty(key).split(",");
	}

	/**
	 * Reloads the configuration from the currently associated propertyFile.
	 * 
	 * @return true if sucessful, false of file not set or that is an error
	 */
	public boolean reloadConfiguration() {
		if (propertyFile != null && this.propertyFile.exists()) {
			try (FileInputStream reader = new FileInputStream(propertyFile)) {
				if (isXML)
					loadFromXML(reader);
				else
					load(reader);
				return true;
			} catch (InvalidPropertiesFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Associates the given key with a string representation of a boolean
	 * 
	 * @param key
	 *            the key to stoare the boolean with
	 * @param value
	 *            the boolean value to convert to a string
	 * @return The string value the key was associated to, or null if key is null.
	 */
	public String setBoolean(String key, boolean value) {
		if (key != null) {
			setProperty(key, Boolean.toString(value));
			return getProperty(key);
		}
		return null;
	}

	/**
	 * Set the value of the name property to the name of a theClass implementing the given interface interface. An exception
	 * is thrown if theClass does not implement the interface.
	 * 
	 * @param key
	 *            the key of the class type
	 * @param theClass
	 *            The class to save
	 * @param superType
	 *            the superinterface or superclass type that needs to be enforced
	 * @throws WrongClassTypeException
	 * @return the String value of what the key was associated to, or null if key is null.
	 */
	public String setClass(String key, Class<?> theClass, Class<?> superType) throws WrongClassTypeException {
		if (key != null)
			if (superType.isAssignableFrom(theClass)) {
				setProperty(key, theClass.toString());
				return getProperty(key);
			} else {
				throw new WrongClassTypeException("Tried saving " + theClass + " as a class of type of " + superType + ".",
						superType);
			}
		return null;
	}

	/**
	 * Set the value of the name property to the name of a theClas.
	 * 
	 * @param key
	 *            the key of the class type
	 * @param theClass
	 *            The class to save
	 * @return the String value of what the key was associated to, or null if key is null.
	 */
	public String setClass(String key, Class<?> theClass) {
		if (key != null) {
			setProperty(key, theClass.toString());
			return getProperty(key);
		}
		return null;
	}

	/**
	 * Sets the value associated with the key to the value if it is not already set.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return true if the value was set, false otherwise
	 */
	public boolean setIfUnset(String key, String value) {
		if (key != null && !containsKey(key)) {
			setProperty(key, value);
			return true;
		}
		return false;
	}

	/**
	 * Sets the key to the String value of the int.
	 * 
	 * @param key
	 * @param value
	 * @return the String value of the int stored, or null if key is null.
	 */
	public String setInt(String key, int value) {
		if (key != null) {
			setProperty(key, Integer.toString(value));
			return getProperty(key);
		}
		return null;
	}

	/**
	 * Sets the key to the String value of the long.
	 * 
	 * @param key
	 * @param value
	 * @return the String value of the long stored, or null if key is null.
	 */
	public String setLong(String key, long value) {
		if (key != null) {
			setProperty(key, Long.toString(value));
			return getProperty(key);
		}
		return null;
	}

	/**
	 * Sets the key to the String value of the double.
	 * 
	 * @param key
	 * @param value
	 * @return the String value of the double stored, or null if key is null.
	 */
	public String setDouble(String key, long value) {
		if (key != null) {
			setProperty(key, Double.toString(value));
			return getProperty(key);
		}
		return null;
	}

	/**
	 * Associates the given strings with the key. The strings are stored simply as a comma-seperated string combination of
	 * all the individual strings. The getStrings method can be used to retrieve an array of these strings.
	 * 
	 * @param key
	 *            the key
	 * @param values
	 *            the String to associate the key
	 * @return the String that the key is now associated with, or null if key is null.
	 */
	public String setStrings(String key, String... values) {
		if (key != null) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < values.length; i++) {
				builder.append(values[i]);
				if ((i + 1) < values.length) {
					builder.append(",");
				}
			}
			setProperty(key, builder.toString());
			return getProperty(key);
		}
		return null;
	}

}
