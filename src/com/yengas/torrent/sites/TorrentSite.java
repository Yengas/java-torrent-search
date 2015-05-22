package com.yengas.torrent.sites;

import java.net.URL;
import java.util.List;

import com.yengas.torrent.Torrent;
import com.yengas.torrent.utilities.TorrentFeedParser;

public abstract class TorrentSite {
	public abstract TorrentFeedParser getParser();
	public abstract String getUrl(String term);
	public abstract String getItemPath();
	
	public List<Torrent> search(String term) throws Exception{
		return getParser().execute(new URL(getUrl(term)), getItemPath());
	}
}
