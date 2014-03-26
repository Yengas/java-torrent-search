package com.yengas.torrent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Torrent {
	
	public enum Field{ 
		TITLE, DESCRIPTION, CATEGORY, DOWNLOAD_URL, PAGE_URL, PUBLISH, SEED, LEECH, DOWNLOAD_COUNT, SIZE 
	}
	
	private String title, description;
	private Category category;
	private URL download, page;
	private Date publish;
	private int seed, leech;
	private long downloadCount, size;

	public Torrent(){
		this.title = "";
		this.description = "";
		this.download = null;
		this.page = null;
		this.publish = null;
		this.seed = -1;
		this.leech = -1;
		this.downloadCount = -1;
		this.size = -1;
	}
	
	public Torrent(String title, URL download, URL page) {
		this(title, download, page, -1, -1, -1);
	}
	
	public Torrent(String title, URL download, URL page, int seed, int leech, long size){
		this(title, download, page, seed, leech, -1, size, Category.UNKNOWN, null);
	}

	public Torrent(String title, URL download, URL page, int seed, int leech,
			int downloadCount, long size, Category category, Date publish) {
		this(title, download, page, seed, leech, downloadCount, size, category, publish, "");
	}

	public Torrent(String title, URL download, URL page, int seed, int leech,
			int downloadCount, long size, Category category, Date publish, String description) {
		this.title = title;
		this.download = download;
		this.category = category;
		this.page = page;
		this.publish = publish;
		this.seed = seed;
		this.leech = leech;
		this.size = size;
		this.downloadCount = downloadCount;
		this.description = description;
	}

	/**
	 * Returns title of the Torrent.
	 * @return {@link java.lang.String}
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns description of the Torrent. Empty if not known.
	 * @return {@link java.lang.String}
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns a Category class which gives information about the torrent's category on the site.
	 * @return {@link com.yengas.torrent.Category}
	 * @see {@link com.yengas.torrent.Category}
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Returns an url to the download link of the torrent.
	 * @return {@link java.net.URL}
	 */
	public URL getDownload() {
		return download;
	}

	/**
	 * Returns an url to the gui page of the torrent.
	 * @return {@link java.net.URL}
	 */
	public URL getPage() {
		return page;
	}

	/**
	 * Returns publish date of the Torrent.
	 * @returns {@link java.util.Date}
	 */
	public Date getPublish() {
		return publish;
	}

	/**
	 * Returns int, representing the seed number of the torrent. Returns -1 if not known.
	 * @return int
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * Returns int, representing the leech number of the torrent. Returns -1 if not known.
	 * @return int
	 */
	public int getLeech() {
		return leech;
	}

	/**
	 * Returns int, representing the download number of the torrent. Returns -1 if not known.
	 * @return int
	 */
	public long getDownloadCount() {
		return downloadCount;
	}

	/**
	 * Returns a long, representing the size of the torrent in bytes. Returns -1 if not known.
	 * @return long
	 */
	public long getSize() {
		return size;
	}
	
	public void setInteger(Torrent.Field field, Object value){
		int set = -1;
		if(value instanceof Integer){
			set = (int) value;
		}else{
			set = Integer.parseInt("" + value);
		}
		
		if(field == Torrent.Field.LEECH){
			this.leech = set;
		}else if(field == Torrent.Field.SEED){
			this.seed = set;
		}
	}
	
	public void setLong(Torrent.Field field, Object value){
		long set = -1;
		if(value instanceof Long){
			set = (long) value;
		}else{
			set = Long.parseLong("" + value);
		}
		
		if(field == Torrent.Field.DOWNLOAD_COUNT){
			this.downloadCount = set;
		}else if(field == Torrent.Field.SIZE){
			this.size = set;
		}
	}
	
	public void setCategory(Object value){
		Category set = Category.UNKNOWN;
		if(value instanceof Category){
			set = (Category) value;
		}else{
			set = new Category("" + value, "No description.", null, null);
		}
		this.category = set;
	}
	
	/**
	 * Sets the given field of the Torrent. 
	 */
	public void setField(Torrent.Field field, Object value) throws MalformedURLException{
		switch(field){
		case CATEGORY: setCategory(value); break;
		
		case PUBLISH: if(value != null && value instanceof Date){ this.publish = (Date) value; } break;
		
		case LEECH: this.setInteger(Torrent.Field.LEECH, value); break;
		case SEED: this.setInteger(Torrent.Field.SEED, value); break;
		
		case DOWNLOAD_COUNT: this.setLong(Torrent.Field.DOWNLOAD_COUNT, value); break;
		case SIZE: this.setLong(Torrent.Field.SIZE, value); break;
		
		case DOWNLOAD_URL: this.download = new URL("" + value);; break;
		case PAGE_URL: this.page = new URL("" + value); break;
		case DESCRIPTION: this.description = "" + value; break;
		case TITLE: this.title = "" + value; break;
		}
	}

}