package alektas.stroymat;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import alektas.stroymat.di.AppComponent;
import alektas.stroymat.di.AppModule;
import alektas.stroymat.di.DaggerAppComponent;

public class App extends MultiDexApplication {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        sAppComponent = buildComponent();

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        int fetchInterval = 1800;
        if (BuildConfig.DEBUG) fetchInterval = 30;
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(fetchInterval)
                .build();
        remoteConfig.setConfigSettingsAsync(configSettings);
        remoteConfig.setDefaults(R.xml.remote_config_defaults);
        remoteConfig.fetchAndActivate();
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
