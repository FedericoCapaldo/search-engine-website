package Service;

import spark.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 3/13/2017.
 */
public class Utils {
    public static Map<String, String> getParamMapFromRequest(Request request){
        Map<String, String> paramMap = new HashMap<>();
        for (String paramKey : request.queryParams()){
            String paramValue = request.queryParams(paramKey);
            paramMap.put(paramKey, paramValue);
        }
        return paramMap;
    }
}
