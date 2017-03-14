package SearchEngine;

import Service.HTML;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.ScoreDoc;

import java.io.File;
import java.util.HashMap;


public class SearchEngine
{

	Indexer indexer;
	Searcher searcher;
	DirectoryReader dr;

	public SearchEngine()
	{
		try
		{
			String indexDirectory = HTML.getBaseFolder() + File.separator + "index" + File.separator;
			indexer = new Indexer(indexDirectory);
//			indexer.buildIndex();
			searcher = new Searcher(indexer.getIndexer());
			dr = searcher.getIndexReader();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public HashMap<Integer, String[]> search(String query) {

		try {
			ScoreDoc[] results = searcher.search(query);
			return formatResult(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	private HashMap<Integer, String[]> formatResult(ScoreDoc[] results) {
		HashMap<Integer, String[]> formatted = new HashMap<>();
		for (int hit = 0; hit < results.length; ++hit) {
			try {
				String[] resultInfo = {Float.toString(results[hit].score), dr.document(results[hit].doc).get("title"), dr.document(results[hit].doc).get("url")};
				formatted.put(hit + 1, resultInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return formatted;
	};



	public void shutdown() {
		try {
			indexer.getIndexer().close();
			dr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
