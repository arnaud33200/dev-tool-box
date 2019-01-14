import android.content.SharedPreferences;

public abstract class AbstractPreferenceItem<T> {

    protected T value;
    SharedPreferences sharedPreferences;
    private final String key;

    PreferenceStringItemValidator<T> validator;

    public AbstractPreferenceItem(SharedPreferences sharedPreferences, String key) {
        this.value = null;
        this.sharedPreferences = sharedPreferences;
        this.key = key;
    }

    public void setValidator(PreferenceStringItemValidator validator) {
        this.validator = validator;
    }

    protected abstract T getInitialEmptyValue();
    protected abstract boolean isValueEmpty(T value);
    protected abstract T getValueFromSharedPreference(SharedPreferences sharedPreferences, String key);
    protected abstract void setValueFromSharedPreference(SharedPreferences sharedPreferences, T value, String key);

    public T getValue() {
        if (value == null) {
            value = getValueFromSharedPreference(sharedPreferences, key);
            if (isValueEmpty(value)) {
                setValue(getInitialEmptyValue());
            }
        }

        if (validator != null) {
            value = validator.validateDeserializedValue(value);
        }

        return value;
    }

    public void setValue(T s) {
        value = s;
        setValueFromSharedPreference(sharedPreferences, value, key);

        if (validator != null) {
            validator.onValueSerialized(value);
        }
    }

    public void resetValue() {
        setValue(null);
    }

    public interface PreferenceStringItemValidator<T> {
        T validateDeserializedValue(T value);
        void onValueSerialized(T value);
    }

}
