package com.yengas.torrent.sites;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.yengas.torrent.Category;
import com.yengas.torrent.Torrent;
import com.yengas.torrent.utilities.TorrentFeedParser;

public class KickassTO {

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
		
		parser.matchFiltered(Torrent.Field.PUBLISH, "pubDate", new TorrentFeedParser.Matcher() {
			@Override
			public Object[] match(String content) {
				Date date = null;
				try{ date = sdf.parse(content.trim()); }catch(Exception e){ e.printStackTrace(); }
				return new Object[]{ date };
			}
		});
		parser.matchFiltered(Torrent.Field.CATEGORY, "category", new TorrentFeedParser.Matcher(){
			@Override
			public Object[] match(String content) {
				return new Object[]{ new Category(content, "No description.", null, null) };
		}});
	}
	
	public static List<Torrent> search(String term){
		try{
			return parser.execute(new URL("http://kickass.to/usearch/"+ URLEncoder.encode(term) +"/?rss=1"), "/rss/channel/item");
		}catch(Exception e){ e.printStackTrace(); return null; }
	}
	
}
