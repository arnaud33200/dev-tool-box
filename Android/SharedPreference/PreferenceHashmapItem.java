import android.content.SharedPreferences;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.HashMap;

public class PreferenceHashmapItem<K, V> extends AbstractPreferenceItem<HashMap<K, V>> {

    public PreferenceHashmapItem(SharedPreferences sharedPreferences, String key) {
        super(sharedPreferences, key);
    }

    @Override
    protected HashMap<K, V> getInitialEmptyValue() {
        return new HashMap<>();
    }

    @Override
    protected boolean isValueEmpty(HashMap<K, V> value) {
        return value == null || value.size() == 0;
    }

    @Override
    protected HashMap<K, V> getValueFromSharedPreference(SharedPreferences sharedPreferences, String key) {
        String storedHashMapString = sharedPreferences.getString(key, "");
        return convertFromGsonString(storedHashMapString);
    }

    public HashMap<K, V> convertFromGsonString(String gsonString) {
        java.lang.reflect.Type type = new TypeToken<HashMap<K, V>>(){}.getType();
        HashMap<K, V> hashMap = new Gson().fromJson(gsonString, type);
        return hashMap;
    }

    @Override
    protected void setValueFromSharedPreference(SharedPreferences sharedPreferences, HashMap<K, V> value, String key) {
        Gson gson = new Gson();
        String gsonString = gson.toJson(value);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gsonString);
        boolean success = editor.commit();
    }
}
