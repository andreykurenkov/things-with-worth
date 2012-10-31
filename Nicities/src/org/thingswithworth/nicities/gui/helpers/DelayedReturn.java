package org.thingswithworth.nicities.gui.helpers;

import org.thingswithworth.nicities.ObservableClass;
import org.thingswithworth.nicities.Observer;

public class DelayedReturn<Type> extends ObservableClass {
	private Type toReturn;

	public DelayedReturn() {
	}

	public DelayedReturn(Observer<ObservableClass> toNotify) {
		this.acceptObserver(toNotify);
	}

	public void setReturn(Type set) {
		toReturn = set;
	}

	public Type getReturn() {
		return toReturn;
	}

	public void doReturn() {
		this.notifyObservers("Returned", toReturn);
	}

}
