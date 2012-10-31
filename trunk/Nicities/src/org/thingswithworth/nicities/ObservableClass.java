package org.thingswithworth.nicities;

import java.util.ArrayList;

public class ObservableClass implements Observable<ObservableClass> {

	protected ArrayList<Observer<ObservableClass>> observers = new ArrayList<Observer<ObservableClass>>();

	protected void notifyObservers(String code, Object... args) {

		for (Observer<ObservableClass> obv : observers)
			obv.acceptNotification(this, code, args);
	}

	@Override
	public void acceptObserver(Observer<ObservableClass> add) {
		this.observers.add(add);
	}

	@Override
	public void deleteObserver(Observer<ObservableClass> add) {
		this.observers.remove(add);

	}

	@Override
	public void deleteObservers() {
		observers = new ArrayList<Observer<ObservableClass>>();
	}

	@Override
	public int countObservers() {
		return observers.size();
	}

}
