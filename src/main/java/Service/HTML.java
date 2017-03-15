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

    private static String getHead(){
        return "<html><head><meta charset=\"UTF-8\"><title>121 Search Engine | Eric, Danilo, Federico</title><style>" + getBootstrap() + "</style></head>";
    }

    private static String getBootstrap(){
        return "body{font-size:14pt; font-family:'Noto Sans',sans-serif; padding: 0; margin: 0;}form{width: auto; padding: 5px; background-color: auto; border-radius: 1em;}.button{background-color:#cccccc; border:2px; color:grey; padding:15px 15px; text-align:center; text-decoration:none; display:inline-block; font-size:15px; cursor:pointer; position: relative; top: 50%; width: 100px; border-radius: 1em;}.button:hover{background-color:#F5F5F5; color:#00303f}.overall_wrapper{margin-top:0px;}.wrapper{text-align: center; margin-top: 40px;}.form-group{margin-bottom: 1rem;}.form-control{display: block; width: 100%; padding: 0.5rem 0.75rem; font-size: 1rem; line-height: 1.25; color: #464a4c; background-color: #fff; background-image: none; -webkit-background-clip: padding-box; background-clip: padding-box; border: 1px solid rgba(0, 0, 0, 0.15); border-radius: 0.25rem; -webkit-transition: border-color ease-in-out 0.15s, -webkit-box-shadow ease-in-out 0.15s; transition: border-color ease-in-out 0.15s, -webkit-box-shadow ease-in-out 0.15s; -o-transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s; transition: border-color ease-in-out 0.15s, box-shadow ease-in-out 0.15s, -webkit-box-shadow ease-in-out 0.15s;}.form-inline{display: -webkit-box; display: -webkit-flex; display: -ms-flexbox; display: flex; -webkit-flex-flow: row wrap; -ms-flex-flow: row wrap; flex-flow: row wrap; -webkit-box-align: center; -webkit-align-items: center; -ms-flex-align: center; align-items: center;}.border_left{margin-left: 15px;}p{margin-left: 20px; padding: 5px;}a{font-size: 17px; text-decoration: none;}.no_margin{margin-top: -28px;}.green_text{font-size: 15px; color: green;}.sub_text{font-size: 15px; margin-top: -27px;}.larger_text{width: 500px;margin-left: 20px; padding: 5px;}";
    }

    private static String getBody(){
        return "<body><form class=\"form-inline overall_wrapper\" action=\"/search\"> <div class=\"form-group larger_text\"> <input type=\"text\" class=\"form-control\" name=\"search\"> </div><div class=\"form-group border_left\"> <button type=\"submit\" value=\"Search\" class=\"button\">Search</button> </div></form>";
    }

    public static String getHTML(){
        return getHead() + getBody();
    }

}
