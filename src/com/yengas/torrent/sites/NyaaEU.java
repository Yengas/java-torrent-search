package com.yengas.torrent.sites;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yengas.torrent.Category;
import com.yengas.torrent.Torrent;
import com.yengas.torrent.utilities.TorrentFeedParser;

public class NyaaEU extends TorrentSite{
	
	/**
	 * For converting from String(e.g. '1.5 GiB') to bytes
	 */
	private static final HashMap<String, Long> bytes = new HashMap<String, Long>();
	static{
		bytes.put("B", new Long(1));
		bytes.put("KiB", new Long(1024));
		bytes.put("MiB", new Long(1048576));
		bytes.put("GiB", new Long(1073741824));
		bytes.put("TiB", new Long(1099511627776L));
	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
	private static final Pattern pattern = Pattern.compile("([\\d]+) seeder\\(s\\), ([\\d]+) leecher\\(s\\), ([\\d]+).*?-([^-]+)");
	
	/**
	 * Create a torrent parser and match the TorrentFields to RSS fields.
	 */
	private static final TorrentFeedParser parser = new TorrentFeedParser();
	static {
		parser.matchField(Torrent.Field.TITLE, "title");
		parser.matchField(Torrent.Field.PAGE_URL, "guid");
		parser.matchField(Torrent.Field.DOWNLOAD_URL, "link");
		parser.matchAll("description", new Torrent.Field[] {
				Torrent.Field.SEED, Torrent.Field.LEECH,
				Torrent.Field.DOWNLOAD_COUNT, Torrent.Field.SIZE },
				(content) -> {
					Matcher m = pattern.matcher(content);
					
					if(m.find()){
						return new Object[]{ m.group(1), m.group(2), m.group(3), stringToBytes(m.group(4)) };
					}
					return new Object[]{ -1, -1, -1, -1 };
				});
		parser.matchFiltered(Torrent.Field.PUBLISH, "pubDate", (content) -> {
			Date date = null;
			
			try {
				date = sdf.parse(content.trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Object[] { date };
		});
		parser.matchFiltered(Torrent.Field.CATEGORY, "category", (content) -> {
				return new Object[] { new Category(content, "No description.", null, null) };
		});
	}
	
	public static long stringToBytes(String sizeString){
		sizeString = sizeString.trim();
		String[] splitted = sizeString.split(" ");
		float size = Float.parseFloat(splitted[0]);
		return (long)(bytes.get(splitted[1]).longValue() * size);
	}
	
	@Override
	public TorrentFeedParser getParser() {
		return parser;
	}

	@Override
	public String getUrl(String term) {
		try {
			return "http://www.nyaa.se/?page=rss&cats=0_0&filter=0&term="+ URLEncoder.encode(term, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public String getItemPath() {
		return "/rss/channel/item";
	}

}
