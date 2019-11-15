package alektas.stroymat.ui.pricelist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import alektas.stroymat.App;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.PricelistItem;

public class PricelistViewModel extends ViewModel {
    @Inject
    public Repository mRepository;
    private MutableLiveData<PricelistItem> mSelectedItem = new MutableLiveData<>();
    private MutableLiveData<Integer> mSelectedCategory = new MutableLiveData<>();
    private LiveData<List<Category>> mCategories;
    private LiveData<List<PricelistItem>> mItems;

    public PricelistViewModel() {
        App.getComponent().inject(this);
        mCategories = mRepository.getCategories();
        mItems = mRepository.getItems(); // Установка всех товаров в списке по-умолчанию
        mSelectedCategory.setValue(0); // Отметить категорию "Все" (== 0) в меню
    }

    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    public LiveData<List<PricelistItem>> getItems() {
        return mItems;
    }

    public LiveData<PricelistItem> getSelectedItem() {
        return mSelectedItem;
    }

    public void setCategory(int category) {
        mRepository.setCategory(category);
        mSelectedCategory.setValue(category);
    }

    public void onItemSelected(int article) {
        mSelectedItem.setValue(mRepository.getItem(article));
    }

    public void onSearchEnter(String query) {
        mRepository.setFoundItems("%"+query+"%");
    }

    public Category getCategory(int categ) {
        return mRepository.getCategory(categ);
    }

    public LiveData<Integer> getSelectedCategory() {
        return mSelectedCategory;
    }

    public void addToCart(PricelistItem item, float quantity) {
        mRepository.addCartItem(new CartItem(item, quantity));
    }

    public int getCartQuantity(int article) {
        return mRepository.getCartQuantity(article);
    }
}
