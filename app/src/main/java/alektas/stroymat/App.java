package alektas.stroymat;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import alektas.stroymat.di.AppComponent;
import alektas.stroymat.di.AppModule;
import alektas.stroymat.di.DaggerAppComponent;
import alektas.stroymat.utils.ResourcesUtils;

public class App extends MultiDexApplication {
    private static AppComponent sAppComponent;
    private static final int FETCH_INTERVAL = 1800;
    private static final int FETCH_INTERVAL_DEBUG = 30;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sAppComponent = buildComponent();

        SharedPreferences prefs = getSharedPreferences(
                ResourcesUtils.getString(R.string.PREFS_NAME),
                Context.MODE_PRIVATE);

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        int fetchInterval = FETCH_INTERVAL;
        if (BuildConfig.DEBUG) fetchInterval = FETCH_INTERVAL_DEBUG;
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(fetchInterval)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaults(R.xml.remote_config_defaults);

        String galleryEnableKey = getString(R.string.GALLERY_ENABLE_KEY);
        remoteConfig.fetchAndActivate().addOnSuccessListener(fetched -> {
            if (fetched) {
                long galleryActualEnable = remoteConfig.getLong(galleryEnableKey);
                prefs.edit().putLong(galleryEnableKey, galleryActualEnable).apply();
            }
        });
    }

    public static AppComponent getComponent() {
        return sAppComponent;
    }

    private AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
