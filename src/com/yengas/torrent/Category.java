package com.yengas.torrent;

import java.net.URL;

public class Category {
	public static final Category UNKNOWN = new Category("Unknown", "No category found.", null, null);
	private String name, description;
	private URL page, feed;

	public Category(String name, String description, URL page, URL feed) {
		this.name = name;
		this.description = description;
		this.page = page;
		this.feed = feed;
	}

	/**
	 * Returns a string, representing the name of the category.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns a string, representing the description of a category.  
	 * @return String
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns a url to the gui page of the category. Returns null reference if the gui page of the category is not known.
	 * @return {@link java.net.URL} or null reference
	 */
	public URL getPage() {
		return page;
	}
	
	/**
	 * Returns a url to the rss feed of the category. Returns null reference if the rss feed of the category is not known.
	 * @return {@link java.net.URL} or null reference
	 */
	public URL getFeed(){
		return feed;
	}	
	
}
