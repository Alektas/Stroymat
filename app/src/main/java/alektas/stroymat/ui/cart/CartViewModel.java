package alektas.stroymat.ui.cart;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import alektas.stroymat.App;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.CartItem;

public class CartViewModel extends AndroidViewModel {
    @Inject
    Repository mRepository;
    private LiveData<List<CartItem>> mItems;
    private MutableLiveData<Float> mPrice = new MutableLiveData<>();

    public CartViewModel(Application app) {
        super(app);
        App.getComponent().inject(this);

        mItems = mRepository.getCartItems();
    }

    public LiveData<List<CartItem>> getItems() {
        return mItems;
    }

    public LiveData<Float> getPrice() {
        return mPrice;
    }

    public void removeItem(CartItem item) {
        mRepository.removeCartItem(item);
    }

    public void clearCart() {
        mRepository.clearCart();
    }

    public void recalcPrice() {
        if (mItems.getValue() == null) {
            mPrice.setValue(0f);
            return;
        }

        float p = 0f;
        for (CartItem item : mItems.getValue()) {
            p += item.getQuantity() * item.getPrice();
        }
        mPrice.setValue(p);
    }
}
