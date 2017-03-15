// Note: To give credit where it is due, some of the code below in
// buildSnippet(...) was guided by:
// https://hrycan.com/2009/10/25/lucene-highlighter-howto/.
// Though it is not an exact copy, the general layout of the function
// resembles some of the code found in the article.


package SearchEngine;

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

	public void shutdown() {
		try {
			indexer.getIndexer().close();
			dr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			QueryParser queryParser = new QueryParser("body", new StandardAnalyzer());
			QueryScorer queryScorer = new QueryScorer(queryParser.parse(query));
			Highlighter highlighter = new Highlighter(queryScorer);
			highlighter.setMaxDocCharsToAnalyze(1000000);
			highlighter.setTextFragmenter(new SimpleSpanFragmenter(queryScorer, 75));

			String[] snippets = highlighter.getBestFragments(new StandardAnalyzer(), "body", dr.document(result.doc).get("body"), 3);

			int currentPos = 0;

			for (int i = 0; i < snippets.length; ++i)
			{
				for (int pos = 0; pos < snippets[i].length(); ++pos, ++currentPos)
				{
					snippet.append(snippets[i].charAt(pos));

					if (Character.isWhitespace(snippets[i].charAt(pos)) && currentPos >= 100)
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
}
