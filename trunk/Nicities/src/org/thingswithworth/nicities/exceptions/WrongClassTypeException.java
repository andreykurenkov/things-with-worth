package org.thingswithworth.nicities.exceptions;

/**
 * A simple exception for cases where a class or class name is supplied but does not sattisfy a required superinteface or
 * superclass
 * 
 * @author Andrey Kurenkov
 * @version 1.0
 */
public class WrongClassTypeException extends Exception {

	private String neededClassTypeName;
	private Class<?> neededClassType;

	/**
	 * Basic constructor
	 * 
	 * @param message
	 *            the message for the exception
	 * @param neededType
	 *            the name of a required superinterface or superclass
	 */
	public WrongClassTypeException(String message, String neededType) {
		super(message);
		neededClassTypeName = neededType;
	}

	/**
	 * Basic constructor
	 * 
	 * @param message
	 *            the message for the exception
	 * @param neededType
	 *            the name of a required superinterface or superclass
	 */
	public WrongClassTypeException(String message, Class<?> neededType) {
		super(message);
		neededClassType = neededType;
		neededClassTypeName = neededType.getName();
	}

	/**
	 * getter for name of required superinterface or superclass
	 * 
	 * @return required class type name
	 */
	public String getRequiredTypeName() {
		return neededClassTypeName;
	}

	/**
	 * getter for required superinterface or superclass
	 * 
	 * @return required class type
	 */
	public Class<?> getRequiredType() {
		return neededClassType;
	}

}
