package purrinha;

import javafx.beans.property.SimpleObjectProperty;
import java.util.ResourceBundle;
import java.util.Locale;

public class LanguageManager {
    private static final LanguageManager instance = new LanguageManager();
    private final SimpleObjectProperty<ResourceBundle> bundle;
    private final SimpleObjectProperty<Locale> currentLocale;

    private LanguageManager() {
        this.bundle = new SimpleObjectProperty<>();
        this.currentLocale = new SimpleObjectProperty<>();

        currentLocale.addListener((observable, oldLocale, newLocale) ->
            bundle.set(ResourceBundle.getBundle("messages", newLocale))
        );

        currentLocale.set(Locale.ENGLISH);
    }

    public static LanguageManager getInstance() {
        return instance;
    }

    public String getString(String key, Object... args) {
        String pattern = bundle.get().getString(key);
        return args.length == 0 ? pattern : String.format(pattern, args);
    }

    public void changeLanguage(String language) {
        currentLocale.set(Locale.forLanguageTag(language));
    }

    public SimpleObjectProperty<Locale> getCurrentLocale() {
        return currentLocale;
    }
}
