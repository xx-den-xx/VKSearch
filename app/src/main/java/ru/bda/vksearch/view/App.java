package ru.bda.vksearch.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

import ru.bda.vksearch.model.constant.Const;

/**
 * email: denbelobaba@gmail.com
 *
 * @author Belobaba Denis
 */

public class App extends MultiDexApplication {

    private static SharedPreferences preferences;

    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);
        preferences = getSharedPreferences(Const.APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void saveToken(String token) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Const.TOKEN, token);
        editor.apply();
    }

    public static String getToken() {
        return preferences.getString(Const.TOKEN, "");
    }

    public static Context getContext() {
        return getContext();
    }
}
