package org.thingswithworth.nicities.helpers;

public class StringHelper {
	public static String getInTitleCase(String string) {
		String[] parts = string.split(" |-");
		for (int i = 0; i < parts.length; i++) {
			if (parts[i].length() > 0) {
				String newPart = Character.toUpperCase(parts[i].charAt(0))
						+ (parts[i].length() > 1 ? parts[i].substring(1).toLowerCase() : "");
				string = string.replace(parts[i], newPart);
			}
		}
		return string;
	}
}
