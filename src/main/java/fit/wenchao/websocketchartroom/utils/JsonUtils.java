package fit.wenchao.websocketchartroom.utils;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
    public static JSONObject ofJson(String key, Object value ) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(key, value);
        return jsonObject;
    }

    public static JSONObject ofJson(String k1, Object v1, String k2, Object v2 ) {
        JSONObject json = ofJson(k1, v1);
        json.put(k2,v2);
        return json;
    }


    public static JSONObject ofJson(String k1, Object v1, String k2, Object v2 ,
                                    String k3, Object v3) {
        JSONObject json = ofJson(k1,v1,
        k2,v2);
        json.put(k3,v3);
        return json;
    }

    public static JSONObject ofJson(String k1, Object v1, String k2, Object v2 ,
                                    String k3, Object v3, String k4, Object v4) {
        JSONObject json = ofJson(k1,v1,
                k2,v2,
                k3,v3);
        json.put(k4,v4);
        return json;
    }

    public static JSONObject ofJson(String k1, Object v1, String k2, Object v2 ,
                                    String k3, Object v3, String k4, Object v4,
                                    String k5, Object v5) {
        JSONObject json = ofJson(k1,v1,
                k2,v2,
                k3,v3,
                k4,v4);
        json.put(k5,v5);
        return json;
    }

}
