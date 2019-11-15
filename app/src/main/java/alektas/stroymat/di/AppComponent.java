package alektas.stroymat.di;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Singleton;

import alektas.stroymat.data.Repository;
import alektas.stroymat.ui.MainActivity;
import alektas.stroymat.ui.calculators.profnastil.ProfnastilViewModel;
import alektas.stroymat.ui.calculators.stove.StoveViewModel;
import alektas.stroymat.ui.calculators.trotuar.TrotuarViewModel;
import alektas.stroymat.ui.cart.CartViewModel;
import alektas.stroymat.ui.gallery.GalleryViewModel;
import alektas.stroymat.ui.pricelist.PricelistViewModel;
import alektas.stroymat.ui.server.AdminViewModel;
import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
    Resources getResources();
    Repository getRepository();
    void inject(MainActivity activity);
    void inject(CartViewModel vm);
    void inject(AdminViewModel vm);
    void inject(GalleryViewModel vm);
    void inject(PricelistViewModel vm);
    void inject(ProfnastilViewModel vm);
    void inject(StoveViewModel vm);
    void inject(TrotuarViewModel vm);
}
