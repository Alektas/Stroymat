package alektas.stroymat.di;

import android.content.Context;
import android.content.res.Resources;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
    Resources getResources();
}
