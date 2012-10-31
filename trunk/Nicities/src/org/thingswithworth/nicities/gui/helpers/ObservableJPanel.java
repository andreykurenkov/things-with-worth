package org.thingswithworth.nicities.gui.helpers;

import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JPanel;

import org.thingswithworth.nicities.Observable;
import org.thingswithworth.nicities.Observer;

public class ObservableJPanel extends JPanel implements Observable<ObservableJPanel> {

	protected ArrayList<Observer<ObservableJPanel>> observers;

	public ObservableJPanel(LayoutManager layout) {
		super(layout);
	}

	protected void notifyObservers(String code, Object... args) {
		for (Observer<ObservableJPanel> obv : observers)
			obv.acceptNotification(this, code, args);
	}

	@Override
	public void acceptObserver(Observer<ObservableJPanel> add) {
		this.observers.add(add);
	}

	@Override
	public void deleteObserver(Observer<ObservableJPanel> add) {
		this.observers.remove(add);

	}

	@Override
	public void deleteObservers() {
		observers = new ArrayList<Observer<ObservableJPanel>>();
	}

	@Override
	public int countObservers() {
		return observers.size();
	}

}
