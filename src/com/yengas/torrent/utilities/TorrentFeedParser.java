package com.yengas.torrent.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.yengas.torrent.Torrent;

public class TorrentFeedParser {

	/**
	 * An interface to convert rss content to needed format. For example regex
	 * matching if multiple Torrent Fields are in the same rss field. Also used
	 * for creating Date and Category instances from rss string.
	 */
	public interface Matcher {
		public Object[] match(String content);
	}

	/**
	 * A simple class to hold Matcher, rssField and torrent fields together.
	 */
	private class Match {
		public Torrent.Field[] fields;
		public String rssField;
		public Matcher match;

		public Match(Torrent.Field[] fields, String field, Matcher match) {
			this.fields = fields;
			this.rssField = field;
			this.match = match;
		}
	}

	/**
	 * List to hold matches together. This matches will be used for extracting
	 * xml content.
	 */
	private ArrayList<Match> matchList = new ArrayList<Match>();
	/**
	 * Default matcher which returns the rss field's content.
	 */
	private static final Matcher defaultMatcher = (content) -> {
			return new Object[] { content };
	};

	/**
	 * Match the coressponding rss field to torrent field.
	 * 
	 * @param torrentField
	 * @param rssField
	 */
	public void matchField(Torrent.Field torrentField, String rssField) {
		matchList.add(new Match(new Torrent.Field[] { torrentField }, rssField, defaultMatcher));
	}

	/**
	 * Match multiple torrent fields to a rss field. Use matcher to extract
	 * torrent fields from the rss field content then return an object array
	 * which is in the same order with the fields array you gave.
	 * 
	 * @param rssField
	 * @param fields
	 * @param matcher
	 */
	public void matchAll(String rssField, Torrent.Field[] fields, Matcher matcher) {
		matchList.add(new Match(fields, rssField, matcher));
	}

	/**
	 * Match a rss field and Torrent Field together and use the matcher
	 * interface to convert string to field's class.
	 * 
	 * @param field
	 * @param rssField
	 * @param matcher
	 */
	public void matchFiltered(Torrent.Field field, String rssField, Matcher matcher) {
		matchList.add(new Match(new Torrent.Field[] { field }, rssField, matcher));
	}

	/**
	 * A simple html content fetcher.
	 * 
	 * @param uri
	 * @return String
	 */
	private static String getContent(URL url) {
		StringBuilder content = new StringBuilder("");
		try {
			URLConnection urlConnection = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			String line = null;
			
			while ((line = br.readLine()) != null) {
				content.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	/**
	 * Returns a torrent list by using the Torrent.Field - Rss Field parity and
	 * the simple html fetcher defined by the TorrentFeedParser class
	 * 
	 * @param uri
	 * @param pathToItems
	 * @return List
	 * @see {@link TorrentFeedParser#getContent(URL)}
	 */
	public List<Torrent> execute(URL uri, String pathToItems) throws Exception {
		return execute(getContent(uri), pathToItems);
	}

	/**
	 * Parses the given xml content and returns a torrent list by using the
	 * Torrent.Field - Rss Field parity.
	 * 
	 * @param content
	 * @param pathToItems
	 * @return
	 * @throws Exception 
	 */
	public List<Torrent> execute(String content, String pathToItems) throws Exception {
		ArrayList<Torrent> torrents = new ArrayList<Torrent>();
		XPath xPath = XPathFactory.newInstance().newXPath();
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(content)));
		NodeList torrentList = (NodeList) xPath.compile(pathToItems).evaluate(document, XPathConstants.NODESET);
		int index, length = torrentList.getLength();
		Node torrentNode;

		for (index = 0; index < length; index++) {
			torrentNode = torrentList.item(index);
			Torrent torrent = new Torrent();
			
			for (Match m : matchList) {
				String fieldContent = xPath.compile(m.rssField).evaluate(torrentNode);
				Object[] fieldValues = m.match.match(fieldContent);
				int i;
				
				for (i = 0; i < m.fields.length; i++) {
					torrent.setField(m.fields[i], fieldValues[i]);
				}
			}
			
			torrents.add(torrent);
		}

		return torrents;
	}

}
