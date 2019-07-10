package alektas.stroymat;

import androidx.multidex.MultiDexApplication;

import alektas.stroymat.di.AppComponent;
import alektas.stroymat.di.AppModule;
import alektas.stroymat.di.DaggerAppComponent;

public class App extends MultiDexApplication {
    private static AppComponent sAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppComponent = buildComponent();
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
