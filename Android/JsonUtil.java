
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okhttp3.ResponseBody;

/**
    * util for Android Json Object 
*/

public class JsonUtil {

    public static int getJsonObjectOrJsonArraySize(Object jsonObject) {
        if (jsonObject == null) {
            return 0;
        }
        if (jsonObject instanceof JSONObject) {
            return getJsonObjectSize(jsonObject);
        }
        else if (jsonObject instanceof JSONArray) {
            return getJsonArraySize(jsonObject);
        }
        return 0;
    }

    public static int getJsonObjectSize(Object jsonObject) {
        if (jsonObject == null) {
            return 0;
        }
        int size = ((JSONObject) jsonObject).length();
        return size;
    }

    public static int getJsonArraySize(Object jsonArray) {
        if (jsonArray == null || jsonArray instanceof JSONArray == false) {
            return 0;
        }
        int size = ((JSONArray) jsonArray).length();
        return size;
    }

    public static Boolean checkJsonAndKeyEmpty(Object json, String key) {
        if (key == null || key.length() == 0 || json == null) {
            return true;
        }
        if (json instanceof JSONObject && ((JSONObject) json).length() == 0) {
            return true;
        }
        return false;
    }

    public static Boolean checkJsonContainsKey(Object json, String key) {
        if (key == null || key.length() == 0 || json == null) {
            return false;
        }
        if (json instanceof JSONObject) {
            return ((JSONObject) json).has(key);
        }
        return false;
    }

    private static boolean checkJsonArrayEmptyWithIndex(Object jsonArray, int index) {
        if (index < 0 || jsonArray == null) {
            return true;
        }

        int size = getJsonArraySize(jsonArray);
        if (size == 0 || index >= size) {
            return true;
        }

        return false;
    }

    public static String getStringFromJSON(Object json, String key) {
        if (checkJsonAndKeyEmpty(json, key)) {
            return "";
        }
        try {
            String s = ((JSONObject) json).getString(key);

            if (s.equals("null")) {
                return "";
            } else {
                return s;
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static Date getDateFromJSON(Object json, String key) {
        String value = getStringFromJSON(json, key);
        return DateFunctionality.getDateFromStringFormat(value);
    }

    public static int getIntFromJSON(Object json, String key) {
        String intString = getStringFromJSON(json, key);
        if (DataHelper.isStringEmpty(intString)) { return -1; }
        try { return Integer.parseInt(intString); } catch (Exception e) { }
        return 0;
    }

    public static double getDoubleFromJSON(Object json, String key) {
        String intString = getStringFromJSON(json, key);
        if (DataHelper.isStringEmpty(intString)) { return -1; }
        try { return Double.parseDouble(intString); } catch (Exception e) { }
        return 0;
    }

    public static long getLongFromJSON(Object json, String key) {
        String intString = getStringFromJSON(json, key);
        if (DataHelper.isStringEmpty(intString)) {
            return -1;
        }
        return Long.parseLong(intString);
    }

    public static boolean getBooleanFromJSON(Object json, String key) {
        return getBooleanFromJSON(json, key, false);
    }

    public static boolean getBooleanFromJSON(Object json, String key, boolean defaultIfBlank) {
        String value = getStringFromJSON(json, key);

        if (DataHelper.isStringEmpty(value)) {
            return defaultIfBlank;
        }

        return Boolean.parseBoolean(value);
    }

    public static Object getJsonObjectOrJsonArray(JSONObject json, String key) {
        Object object = null;
        try {
            object = json.get(key);
        } catch (JSONException e) {
            object = new JSONObject();
        }
        return object;
    }

    public static Object getJsonArray(Object json, String key) {
        Object array = new JSONArray();
        if (checkJsonAndKeyEmpty(json, key)) {
            return array;
        }
        try {
            array = ((JSONObject) json).getJSONArray(key);
            if (array == null) {
                array = new JSONArray();
            }
        } catch (Exception e) {

        }
        return array;
    }

    public static Object getJsonObjectFromJson(Object json, String key) {
        Object object = null;
        if (checkJsonAndKeyEmpty(json, key)) {
            return new JSONObject();
        }
        try {
            object = ((JSONObject) json).getJSONObject(key);
        } catch (Exception e) {
        }
        if (object == null) {
            object = new JSONObject();
        }
        return object;
    }

    public static String getStringFromJsonArray(Object jsonArray, int index) {
        String s = "";
        if (checkJsonArrayEmptyWithIndex(jsonArray, index)) {
            return s;
        }
        try {
            s = ((JSONArray) jsonArray).getString(index);
        } catch (Exception e) {
        }

        if (DataHelper.isStringEmpty(s) || s.equals("null")) {
            return "";
        }
        return s;
    }

    public static Object getJsonObjectFromJsonArray(Object jsonArray, int index) {
        Object object = new JSONObject();
        if (checkJsonArrayEmptyWithIndex(jsonArray, index)) {
            return object;
        }
        try {
            object = ((JSONArray) jsonArray).getJSONObject(index);
            if (object == null) {
                object = new JSONObject();
            }
        } catch (Exception e) {
        }
        return object;
    }

    public static Object mergeJsonObject(Object jsonOrigin, Object jsonReplace) {
        if (jsonOrigin == null) {
            jsonOrigin = new JSONObject();
        }

        if (jsonOrigin.getClass().equals(jsonReplace.getClass()) == false) {
            return jsonOrigin;
        }

        if (checkJsonAndKeyEmpty(jsonOrigin, "#") || checkJsonAndKeyEmpty(jsonReplace, "#")) {
            return jsonOrigin;
        }

        Iterator<?> it = ((JSONObject) jsonReplace).keys();

        try {
            while (it.hasNext()) {
                String keyString = (String) it.next();
                ((JSONObject) jsonOrigin).put(keyString, ((JSONObject) jsonReplace).get(keyString));
            }
        } catch (Exception e) {

        }

        return jsonOrigin;
    }


// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// region JSON CONVERTER

    public static Object convetBundleToJsonForNotification(Bundle bundle) {
        JSONObject object = new JSONObject();
        Set<String> keys = bundle.keySet();
        for (String key : keys) {
            try {
                Object value = bundle.get(key);
                if (value instanceof String) {
                    Object jsonObject = createJsonObjectOrJsonArrayFromResponseBody((String) value);
                    if (getJsonObjectOrJsonArraySize(jsonObject) > 0) {
                        value = jsonObject;
                    }
                }
                object.put(key, value);
            }
            catch(JSONException e) { }
        }
        return object;
    }

    public static String getBodyStringFromResponseBody(ResponseBody body) {
        String bodyString = "";
        if (body != null) {
            try { bodyString = body.string(); }
            catch (IOException e) { bodyString = ""; }
        }
        return bodyString;
    }

    public static JSONArray createJSONArrayFromResponseBody(ResponseBody body) {
        String bodyString = getBodyStringFromResponseBody(body);
        return createJSONArrayFromBodyString(bodyString);
    }

    public static JSONArray createJSONArrayFromBodyString(String bodyString) {
        JSONArray jsonArray;

        try {
            jsonArray = new JSONArray(bodyString);
        } catch (JSONException e) {
            jsonArray = new JSONArray();
        }

        return jsonArray;
    }

    public static Object createJsonObjectOrJsonArrayFromResponseBody(ResponseBody body) {
        String bodyString = getBodyStringFromResponseBody(body);
        return createJsonObjectOrJsonArrayFromResponseBody(bodyString);
    }

    public static Object createJsonObjectOrJsonArrayFromResponseBody(String bodyString) {
        Object jsonObject = JsonHelper.createJsonObjectFromString(bodyString);
        if (JsonHelper.getJsonObjectSize(jsonObject) == 0) {
            JSONArray jsonArray = JsonHelper.createJSONArrayFromBodyString(bodyString);
            jsonObject = jsonArray;
        }
        return jsonObject;
    }

    public static JSONObject createJsonObjectFromResponseBody(ResponseBody body) {
        String bodyString = "";
        if (body != null) {
            try {
                bodyString = body.string();
            } catch (IOException e) {
                bodyString = "";
            }
        }
        return JsonHelper.createJsonObjectFromString(bodyString);
    }

    public static JSONObject createJsonObjectFromString(String string) {
        if (DataHelper.isStringEmpty(string)) {
            return new JSONObject();
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(string);
        } catch (JSONException e) {
        }
        if (jsonObject == null) {
            jsonObject = new JSONObject();
        }
        return jsonObject;
    }

    public static HashMap<String, String> getMapOfObjectIdFromJsonArray(Object jsonArray) {
        HashMap<String, String> map = new HashMap<>();

        int size = JsonHelper.getJsonArraySize(jsonArray);
        for (int i = 0; i < size; i++) {
            String readByUserId = JsonHelper.getStringFromJsonArray(jsonArray, i);
            if (DataHelper.isStringEmpty(readByUserId)) {
                Object jsonObject = JsonHelper.getJsonObjectFromJsonArray(jsonArray, i);
                readByUserId = JsonHelper.getStringFromJSON(jsonObject, "id");
            }
            map.put(readByUserId, readByUserId);
        }
        return map;
    }

    public static void putValueForKeyInJsonObject(JSONObject object, String key, Object value) {
        if (object == null || DataHelper.isStringEmpty(key)) {
            return;
        }
        try {
            object.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

// endregion

    /**
     *  get array value from JSONObject safely
     * *
     * @param json json object
     * @param key json key
     * @return Array or [] when json value is null
     * @exception JSONException throw when get json value exception occurs
     */

    public static <T> ArrayList<T> getObjectArrayFromJSON(JSONObject json, String key, Class<T> clazz) {
        try {
            if (!json.has(key)){
                return new ArrayList<>(0);
            }

            JSONArray jsonArray = json.getJSONArray(key);

            return getObjectArrayFromJSON(jsonArray, clazz);
        }catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static Object getFirstObjectFromJson(Object object) {
        try {
        JSONObject jsonObject = (JSONObject) object;
        String key = jsonObject.keys().next();
        return jsonObject.get(key);
        } catch (Exception e) {

        }
        return null;
    }

    public static String getFirstStringFromJson(Object object) {
        Object firstResult = getFirstObjectFromJson(object);
        if (firstResult == null || firstResult instanceof String == false) { return ""; }
        return (String) firstResult;
    }
}