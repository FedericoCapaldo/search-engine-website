package Service;

import SearchEngine.SearchEngine;
import spark.Spark;

import java.io.IOException;
import java.util.HashMap;

public class ServiceMain {



    public static void main(String args[]) throws IOException {
        HTML renderer = new HTML();

        SearchEngine searchEngine = new SearchEngine();

        Spark.get("/search", (request, response) -> {
            String query = request.queryParams("search");
            HashMap<Integer, String[]> results = searchEngine.search(query);
            String finalResponse = "";

            for (int i : results.keySet()) {
                finalResponse += "<div class=\"result\">";
                finalResponse += i + ". ";
                finalResponse += "<span> (" +results.get(i)[0] + ") </span>" + " ";
                finalResponse += "<span>" + results.get(i)[1] + "</span>";
                finalResponse += "<br>";
                finalResponse += "<a href=\"" + "http://" + results.get(i)[2] + "\">" + results.get(i)[2] + "</a>";
                finalResponse += "<br>";
                finalResponse += "<br><br>";
                finalResponse += "</div>";
            }

            return finalResponse;
        });

        // this is just what you can do with the request but we don't need it
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
