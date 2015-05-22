package com.yengas.torrent.sites;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.yengas.torrent.Category;
import com.yengas.torrent.Torrent;
import com.yengas.torrent.utilities.TorrentFeedParser;

public class KickassTO extends TorrentSite{

	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	
	/**
	 * Create a torrent parser and match the TorrentFields to RSS fields.
	 */
	private static final TorrentFeedParser parser = new TorrentFeedParser();
	static{
		parser.matchField(Torrent.Field.TITLE, "title");
		parser.matchField(Torrent.Field.DESCRIPTION, "description");
		parser.matchField(Torrent.Field.PAGE_URL, "guid");
		parser.matchField(Torrent.Field.DOWNLOAD_URL, "enclosure/attribute::url");
		parser.matchField(Torrent.Field.LEECH, "torrent|peers");
		parser.matchField(Torrent.Field.SEED, "torrent|seeds");
		parser.matchField(Torrent.Field.SIZE, "torrent|contentLength");
		parser.matchFiltered(Torrent.Field.PUBLISH, "pubDate", (content) -> {
				Date date = null;
				try{ date = sdf.parse(content.trim()); }catch(Exception e){ e.printStackTrace(); }
				return new Object[]{ date };
		});
		parser.matchFiltered(Torrent.Field.CATEGORY, "category", (content) -> {
				return new Object[]{ new Category(content, "No description.", null, null) };
		});
	}
	
	@Override
	public TorrentFeedParser getParser() {
		return parser;
	}
	
	@Override
	public String getUrl(String term) {
		try {
			return "http://kickass.to/usearch/"+ URLEncoder.encode(term, "UTF-8") +"/?rss=1";
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	@Override
	public String getItemPath() {
		return "/rss/channel/item";
	}
}
