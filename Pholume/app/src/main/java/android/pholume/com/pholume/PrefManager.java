package android.pholume.com.pholume;

import android.content.Context;
import android.content.SharedPreferences;
import android.pholume.com.pholume.Model.User;

import com.google.gson.Gson;

import java.util.HashSet;

public class PrefManager {
    private static final String PREF_COOKIES = "PREF_COOKIES";
    private static SharedPreferences mSharedPreferences;
    private static PrefManager sPrefManager;

    private Context mApplicationContext;

    private PrefManager(Context context) {
        mApplicationContext = context;
        mSharedPreferences = mApplicationContext
                .getSharedPreferences(mApplicationContext.getString(R.string.pref_file_key), Context.MODE_PRIVATE);
    }

    /**
     * Used only in the PholumeApplication to initialize this class
     * @param context Application context passed in from PholumeApplication
     * @return PrefManager instance
     */
    public static PrefManager getInstance(Context context) {
        if (sPrefManager == null) {
            sPrefManager = new PrefManager(context);
        }
        return sPrefManager;
    }

    /**
     * Should be used throughout the app, should never return null
     * @return PrefManager instance
     */
    public static PrefManager getInstance() {
        return sPrefManager;
    }

    public void saveCurrentUser(User user) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        Gson gson = new Gson();
        String userString = gson.toJson(user);
        editor.putString(mApplicationContext.getString(R.string.pref_current_user), userString);
        editor.apply();
    }

    public User getCurrentUser() {
        String userString = mSharedPreferences.getString(
                mApplicationContext.getString(R.string.pref_current_user), "");
        Gson gson = new Gson();
        return gson.fromJson(userString, User.class);
    }

    public void saveCookies(HashSet<String> cookies) {
        mSharedPreferences.edit()
                .putStringSet(PREF_COOKIES, cookies)
                .apply();
    }

    public HashSet<String> getCookies() {
        return (HashSet<String>) mSharedPreferences.getStringSet(PREF_COOKIES, new HashSet<String>());
    }

    public void logoutAndClearUserCookies() {
        saveCookies(null);
        saveCurrentUser(null);
    }
}
