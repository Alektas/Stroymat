package alektas.stroymat.di;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import alektas.stroymat.data.ItemsRepository;
import alektas.stroymat.data.Repository;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context mContext;
    private Resources mResources;

    public AppModule(Application app) {
        mContext = app.getApplicationContext();
        mResources = app.getResources();
    }

    @Provides
    Context getContext() {
        return mContext;
    }

    @Provides
    Resources getResources() {
        return mResources;
    }

    @Provides
    @Singleton
    Repository provideRepository() {
        return ItemsRepository.getInstance(mContext);
    }
}
