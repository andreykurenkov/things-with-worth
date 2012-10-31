package org.thingswithworth.nicities;

public interface Observable<ObservableType extends Observable<ObservableType>> {
	public void acceptObserver(Observer<ObservableType> add);

	public void deleteObserver(Observer<ObservableType> add);

	public void deleteObservers();

	public int countObservers();

}
