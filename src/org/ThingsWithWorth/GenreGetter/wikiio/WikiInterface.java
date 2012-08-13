package org.ThingsWithWorth.GenreGetter.wikiio;

import info.bliki.api.Page;
import info.bliki.api.User;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WikiInterface {
	private User wikiUser;
	private String wiki;
	private long lastRelogin;
	public static final String engWikipedia = "http://en.wikipedia.org/";

	public WikiInterface(String wiki) {
		this.wiki = wiki;
		lastRelogin = (Calendar.getInstance().getTimeInMillis());
		wikiUser = new User("", "", wiki + "w/api.php");
		wikiUser.login();
	}

	@SuppressWarnings("unchecked")
	public List<Page> getPages(final String query) {
		long queryTime = (Calendar.getInstance().getTimeInMillis());
		if (queryTime - lastRelogin > 60000) {
			wikiUser.login();
			lastRelogin = queryTime;
		}
		ExecutorService executor = Executors.newCachedThreadPool();
		Callable<Object> task = new Callable<Object>() {
			public Object call() {
				return wikiUser.queryContent(new String[] { query });
			}
		};
		Future<Object> future = executor.submit(task);
		List<Page> toReturn = null;
		try {
			toReturn = (List<Page>) future.get(10, TimeUnit.SECONDS);
		} catch (Exception ex) {
			System.out.println("CANCEL");
			ex.printStackTrace();
		} finally {
			future.cancel(true); // may or may not desire this
			return toReturn;
		}
	}

	public List<Page> getPages(String[] queries) {
		long queryTime = (Calendar.getInstance().getTimeInMillis());
		if (queryTime - lastRelogin > 1000) {
			wikiUser = new User("", "", wiki + "w/api.php");
			wikiUser.login();
			lastRelogin = queryTime;
		}
		List<Page> listOfPages = wikiUser.queryContent(queries);
		return listOfPages;
	}
}
