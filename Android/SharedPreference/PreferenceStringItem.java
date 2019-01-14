import android.content.SharedPreferences;
import com.google.common.base.Strings;

public class PreferenceStringItem extends AbstractPreferenceItem<String> {

    public PreferenceStringItem(SharedPreferences sharedPreferences, String key) {
        super(sharedPreferences, key);
    }

    @Override
    protected String getInitialEmptyValue() {
        return "";
    }

    @Override
    protected boolean isValueEmpty(String value) {
        return Strings.isNullOrEmpty(value);
    }

    @Override
    protected String getValueFromSharedPreference(SharedPreferences sharedPreferences, String key) {
        String string = "";
        string = sharedPreferences.getString(key, "");
        return string;
    }

    @Override
    protected void setValueFromSharedPreference(SharedPreferences sharedPreferences, String value, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        boolean success = editor.commit();
    }
}