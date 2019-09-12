package alektas.stroymat.di;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import alektas.stroymat.ui.MainActivity;
import alektas.stroymat.ui.cart.CartViewModel;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
    Resources getResources();
    void inject(CartViewModel vm);
    void inject(MainActivity activity);
}
