package org.thingswithworth.nicities.helpers;

import java.util.Collection;
import java.util.Iterator;

public class ArraysHelper {
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<T> from) {
		T[] array = (T[]) new Object[from.size()];
		Iterator<T> iterator = from.iterator();
		for (int i = 0; i < from.size(); i++)
			array[i] = iterator.next();
		return array;
	}
}
