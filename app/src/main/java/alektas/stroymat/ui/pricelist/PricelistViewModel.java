package alektas.stroymat.ui.pricelist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import alektas.stroymat.data.ItemsRepository;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.PricelistItem;

public class PricelistViewModel extends AndroidViewModel {
    private Repository mRepository;
    private MutableLiveData<PricelistItem> mSelectedItem = new MutableLiveData<>();

    public PricelistViewModel(@NonNull Application application) {
        super(application);
        mRepository = ItemsRepository.getInstance(application);
    }

    public LiveData<List<PricelistItem>> getItems() {
        return mRepository.getItems();
    }

    public LiveData<Boolean> getItemsLoading() {
        return mRepository.getItemsLoading();
    }

    public LiveData<PricelistItem> getSelectedItem() {
        return mSelectedItem;
    }

    public void setCategory(int category) {
        mRepository.setCategory(category);
    }

    public void onItemSelected(int article) {
        mSelectedItem.setValue(mRepository.getItem(article));
    }

    public void onSearchEnter(String query) {
        mRepository.setFoundItems("%"+query+"%");
    }

    public String getCategoryName(int categ) {
        return mRepository.getCategoryName(categ);
    }
}
