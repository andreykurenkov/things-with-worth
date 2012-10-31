package org.thingswithworth.nicities.fileio;

/**
 * Simple interface to specify classes that can save/load configration with the Configuration class.
 * 
 * @author Andrey Kurenkov
 * 
 */
public interface Configurable {

	/**
	 * Getter for the classe's current configuration
	 * 
	 * @return the current configuration
	 */
	public Configuration getConfiguration();

	/**
	 * Setter for a new configuration
	 * 
	 * @param setTo
	 *            the new configuration of this class
	 */
	public void setConfiguration(Configuration setTo);

}
