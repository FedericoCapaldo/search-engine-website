package Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HTML {
    // return content of a html file a string
    public String renderContent(String htmlFile) {
        try {
            Path path = Paths.get(getHtmlFolder() + htmlFile);
            return new String(Files.readAllBytes(path), Charset.defaultCharset());
        } catch (IOException e) {
            // Add your own exception handlers here.
        }
        return null;
    }

    // get the path to the project folder HTML
    public static String getHtmlFolder() throws UnsupportedEncodingException {
        String filePath = getBaseFolder() + File.separator + "html" + File.separator;
        return filePath;
    }

    // get project folder
    public static String getBaseFolder() throws UnsupportedEncodingException {
        String filePath = URLDecoder.decode(ServiceMain.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "UTF-8");
        String base = "search-engine-website";
        filePath = filePath.substring(0, filePath.indexOf(base)+base.length());
        return filePath;
    }
}
