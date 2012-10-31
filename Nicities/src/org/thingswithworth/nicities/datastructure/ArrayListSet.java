package org.thingswithworth.nicities.datastructure;

import java.util.ArrayList;
import java.util.Collection;

public class ArrayListSet<E> extends ArrayList<E> {

	@Override
	public boolean add(E add) {
		if (!contains(add)) {
			return super.add(add);
		}
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> allAdd) {
		boolean added = false;
		for (E add : allAdd)
			if (!contains(add)) {
				added = true;
				super.add(add);
			}
		return added;
	}

}
