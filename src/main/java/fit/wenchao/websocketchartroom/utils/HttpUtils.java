package fit.wenchao.websocketchartroom.utils;

import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    /***
     * convert request query string to map
     *
     * @param queryString
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        if (queryString == null || queryString.equals("")) {
            return null;
        }
        int index = queryString.indexOf("?");

        if (index >=0 ) {
            queryString = queryString.substring(index + 1);
        }

        Map<String, String> argMap = new HashMap<>();
        String[] queryArr = queryString.split("&");
        for (int i = 0; i < queryArr.length; i++) {
            String string = queryArr[i];
            String keyAndValue[] = string.split("=", 2);
            if (keyAndValue.length != 2) {
                argMap.put(keyAndValue[0], "");
            } else {
                argMap.put(keyAndValue[0], keyAndValue[1]);
            }
        }
        return argMap;
    }

    public static void main(String[] args) {
        String url="?orderId=INTE2016031800025&orgId=90001001119";

        Map<String, String> argMap =parseQueryString(url);
        System.out.println(argMap);
    }

}
