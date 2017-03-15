package SearchEngine;

import Service.HTML;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class SearchEngine
{

	private Indexer indexer;
	private Searcher searcher;
	private DirectoryReader dr;

	public SearchEngine()
	{
		try
		{
			String indexDirectory = FileOpener.getBaseFolder() + File.separator + "index" + File.separator;
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
			return formatResult(results, query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	private HashMap<Integer, String[]> formatResult(ScoreDoc[] results, String query) {
		HashMap<Integer, String[]> formatted = new HashMap<>();
		for (int hit = 0; hit < results.length; ++hit) {
			try
			{
				String[] resultInfo = {
						Float.toString(results[hit].score),
						dr.document(results[hit].doc).get("title"),
						dr.document(results[hit].doc).get("url"),
						buildSnippet(results[hit], query)};
				formatted.put(hit + 1, resultInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return formatted;
	}


	private String buildSnippet(ScoreDoc result, String query)
	{
		StringBuilder snippet = new StringBuilder();
		try
		{
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			QueryScorer scorer = new QueryScorer(parser.parse(query));
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer, 80));
			highlighter.setMaxDocCharsToAnalyze(1000000);

			String[] snippets = highlighter.getBestFragments(new StandardAnalyzer(), "", dr.document(result.doc).get("body"), 3);

			int currentPos = 0;

			for (int i = 0; i < snippets.length; ++i)
			{
				for (int pos = 0; pos < snippets[i].length(); ++pos, ++currentPos)
				{
					snippet.append(snippets[i].charAt(pos));

					if (snippets[i].charAt(pos) == ' ' && currentPos >= 100)
					{
						snippet.append("<br>");
						currentPos = 0;
					}
				}

				snippet.append("...");
			}
		}
		catch (ParseException | IOException | InvalidTokenOffsetsException e)
		{
			e.printStackTrace();
		}

		return snippet.toString();
	}


	public void shutdown() {
		try {
			indexer.getIndexer().close();
			dr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
