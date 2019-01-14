import android.content.SharedPreferences;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Collection;

public class PreferenceArrayListItem extends AbstractPreferenceItem<ArrayList<?>> {

    public PreferenceArrayListItem(SharedPreferences sharedPreferences, String key) {
        super(sharedPreferences, key);
    }

    @Override
    protected ArrayList<?> getInitialEmptyValue() {
        return new ArrayList<>();
    }

    @Override
    protected boolean isValueEmpty(ArrayList<?> value) {
        return value == null || value.size() == 0;
    }

    @Override
    protected ArrayList<?> getValueFromSharedPreference(SharedPreferences sharedPreferences, String key) {
        String storedHashMapString = sharedPreferences.getString(key, "");
        java.lang.reflect.Type type = new TypeToken<Collection<?>>(){}.getType();
        ArrayList<?> collection = new Gson().fromJson(storedHashMapString, type);
        return collection;
    }

    @Override
    protected void setValueFromSharedPreference(SharedPreferences sharedPreferences, ArrayList<?> value, String key) {
        Gson gson = new Gson();
        String gsonString = gson.toJson(value);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gsonString);
        boolean success = editor.commit();
    }
}
