package Service;

import spark.Spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServiceMain {
    public static void main(String args[]) throws IOException {
        Spark.externalStaticFileLocation("static");
        HTML renderer = new HTML();

        Spark.get("/search", (request, response) -> {
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
