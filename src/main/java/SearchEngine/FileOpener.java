import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FileOpener
{
    // ensure this path is terminated with a forward slash
    private static String RELATIVE_WEBPAGES_DIRECTORY = "WEBPAGES_RAW/";
    private static String RELATIVE_BOOKKEEPING_DIRECTORY = "WEBPAGES_RAW/bookkeeping.json";

    private static ArrayList<String> ALL_FILES = new ArrayList<>();


    public static String getFileContent(String fileDirectory) throws IOException
    {
        BufferedReader buffer = new BufferedReader(new FileReader(fileDirectory));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = buffer.readLine()) != null)
        {
            content.append(line + "\n");
        }

        buffer.close();

        return content.toString();
    }


    // find all files in a directory, included its subdirectories
    public static ArrayList<String> listOfFilesInFolder(File folder) {
        for (File singleFile : folder.listFiles()) {
            if (singleFile.isDirectory()) {
                listOfFilesInFolder(singleFile);
            }
            else if (singleFile.isFile()) {
                ALL_FILES.add(singleFile.getPath());
            }
        }

        return ALL_FILES;
    }

    // temporary to get reading to work without null pointer exception
    // returns a mapping from file directory
    public static HashMap<String, String> getDirectories()
    {
        HashMap<String, String> directories = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

        try
        {
            Object object = jsonParser.parse(new FileReader(RELATIVE_BOOKKEEPING_DIRECTORY));
            JSONObject jsonObject = (JSONObject) object;

            for (Object key : jsonObject.keySet())
            {
                directories.put(RELATIVE_WEBPAGES_DIRECTORY + key, (String) jsonObject.get(key));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return directories;
    }
}
