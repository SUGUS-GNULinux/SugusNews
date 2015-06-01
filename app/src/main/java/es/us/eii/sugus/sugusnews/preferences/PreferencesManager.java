package es.us.eii.sugus.sugusnews.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by alejandrohall on 24/05/15.
 */
public class PreferencesManager {

    private final String PREFS_NAME = "SharedPreferences";

    private SharedPreferences settings;
    private SharedPreferences.Editor settings_editor;

    public PreferencesManager(Context context) {

        this.settings = context.getSharedPreferences(PREFS_NAME,0);
        this.settings_editor = settings.edit();

    }

    public boolean getIsFirstAccess(){
        return settings.getBoolean("isFirstAccess", true);
    }

    public void setIsFirstAccess(boolean isFirstAccess){
        settings_editor.putBoolean("isFirstAccess", isFirstAccess);
        settings_editor.commit();
    }
}
