package Service;

import SearchEngine.SearchEngine;
import spark.Spark;

import java.io.IOException;
import java.util.HashMap;

public class ServiceMain {



    public static void main(String args[]) throws IOException {
        HTML renderer = new HTML();
        SearchEngine searchEngine = new SearchEngine();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.print("shutting down...");
			searchEngine.shutdown();
			Spark.stop();
            System.out.println("done");
		}));

        Spark.get("/", (request, response) -> renderer.renderContent("index.html"));

        Spark.get("/search", (request, response) -> {
            String query = request.queryParams("search");
            HashMap<Integer, String[]> results = searchEngine.search(query);
            StringBuilder finalResponse = new StringBuilder();
            StringBuilder res = new StringBuilder();

            for (int i : results.keySet()) {
                finalResponse.append("<p class=\"result no_margin\">");
                finalResponse.append("<a href=\"" + "http://" + results.get(i)[2] + "\">" + results.get(i)[1] + "</a>");
                finalResponse.append("<p class = \"no_margin green_text\">" + results.get(i)[2] + "</p>");
                finalResponse.append("<p class = \"sub_text\">" + i + ", " + results.get(i)[0] + "</p></p>");
            }

            res.append(HTML.getHTML() + finalResponse.toString() + "</body></html>");
            // searchEngine.shutdown();
            return res.toString();
        });

        // this is just what you can do with the request but we don't need it
        Spark.get("/searchExample", (request, response) -> {
            String resultsToPrint = "the request you passed has parameter map:" + Utils.getParamMapFromRequest(request) + "<br>";
            resultsToPrint += "value of query param 'search': " + request.queryParams("search") + "<br>";
            resultsToPrint += "<a href=\"SomeOtherWebPage.html\">go to SomeOtherWebPage.html<//href>";
            return resultsToPrint;
        });
    }
}
