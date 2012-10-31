package org.thingswithworth.nicities;

public interface Observer<Observed extends Observable<Observed>> {
	public void acceptNotification(Observed source, String code, Object... arg);
}
