package alektas.stroymat.ui.server;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import alektas.stroymat.App;
import alektas.stroymat.data.Repository;

public class AdminViewModel extends ViewModel {
    @Inject
    public Repository repository;

    public AdminViewModel() {
        App.getComponent().inject(this);
    }

    public void uploadPricelist() {
        repository.uploadPricelist();
    }

    public void uploadCategories() {
        repository.uploadCategories();
    }

    public void resetPricelist() {
        repository.resetServerPricelist();
    }

    public void resetCategories() {
        repository.resetServerCategories();
    }
}
