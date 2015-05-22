import java.util.ArrayList;
import java.util.List;

import com.yengas.torrent.Torrent;
import com.yengas.torrent.sites.KickassTO;
import com.yengas.torrent.sites.NyaaEU;
import com.yengas.torrent.sites.TorrentSite;

public class Test {

	public static void main(String[] args) {
		TorrentSite[] sites = new TorrentSite[]{ new KickassTO(), new NyaaEU() };
		List<Torrent> torrents = new ArrayList<>();
		String term = "asd";
		
		for(TorrentSite site : sites){
			try {
				torrents.addAll( site.search(term) );
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		for(Torrent t : torrents){
			System.out.println("Title: " + t.getTitle());
			System.out.println("Description: " + t.getDescription());
			System.out.println("Category: " + t.getCategory().getName());
			System.out.println("Download URL: " + t.getDownload());
			System.out.println("GUI URL: " + t.getPage());
			System.out.println("Date: " + t.getPublish().toString());
			System.out.println("--------STATS--------");
			System.out.println("Seed: " + t.getSeed() + " Leech: " + t.getLeech() + " Download: " + t.getDownloadCount() + " Size(B): " + t.getSize());
			System.out.println("---------------------");
		}
		System.out.println("Total torrent found: " + torrents.size());
	}
	
}
