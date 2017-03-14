package Service;

import SearchEngine.Indexer;
import SearchEngine.Searcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.ScoreDoc;
import spark.Spark;

import java.io.File;
import java.io.IOException;

public class ServiceMain {



    public static void main(String args[]) throws IOException {
        HTML renderer = new HTML();

        String indexDirectory = HTML.getBaseFolder() + File.separator + "index" + File.separator;
        Indexer indexer = new Indexer(indexDirectory);
//      indexer.buildIndex();
        Searcher searcher = new Searcher(indexer.getIndexer());
        DirectoryReader dr = searcher.getIndexReader();



        Spark.get("/search", (request, response) -> {
            String query = request.queryParams("search");
            ScoreDoc[] results = searcher.search(query);
            String docs = "";

            System.out.println(query);

            for (int hit = 0; hit < results.length; ++hit) {

                System.out.println(hit + " loop");
                docs += dr.document(results[hit].doc).get("title") + "<br>";
                docs += dr.document(results[hit].doc).get("url") + "<br>";
                docs += "<br><br>";

            }

            return docs;
        });


        // this is just what you can do with the request
        Spark.get("/searchExample", (request, response) -> {
            String resultsToPrint = "the request you passed has parameter map:" + Utils.getParamMapFromRequest(request) + "<br>";
            resultsToPrint += "value of query param 'search': " + request.queryParams("search") + "<br>";
            resultsToPrint += "<a href=\"SomeOtherWebPage.html\">go to SomeOtherWebPage.html<//href>";
            return resultsToPrint;
        });

        Spark.get("/", (request, response) -> {
            return renderer.renderContent("index.html");
        });
    }



}
