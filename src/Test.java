import java.util.List;

import com.yengas.torrent.Torrent;
import com.yengas.torrent.sites.KickassTO;
import com.yengas.torrent.sites.NyaaEU;

public class Test {

	public static void main(String[] args) {
		List<Torrent> torrents = KickassTO.search("asd");
		torrents.addAll(NyaaEU.search("asd"));
		
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
