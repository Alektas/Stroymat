package alektas.stroymat.ui.calculators.profnastil;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import alektas.stroymat.App;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.model.ProfnastilItem;
import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquareViewModelBase;
import alektas.stroymat.ui.calculators.SquaresAdapter;

public class ProfnastilViewModel extends ViewModel implements SquareViewModelBase {
    @Inject
    public Repository mRepository;
    private LiveData<List<ProfnastilItem>> mProfnastil;
    private MutableLiveData<List<Square>> mRoofs = new MutableLiveData<>();
    private MutableLiveData<Float> mRoofSquare = new MutableLiveData<>();
    private MutableLiveData<ProfnastilItem> mSelectedProfnastil = new MutableLiveData<>();
    private MutableLiveData<Integer> mProfnastilQuantity = new MutableLiveData<>();
    private MutableLiveData<Float> mProfnastilPrice = new MutableLiveData<>();
    private MutableLiveData<Float> mProfnastilSquare = new MutableLiveData<>();
    private float mProfnastilLength;
    private int mOverlapWaves;
    private int mProfnastilReserve;

    public ProfnastilViewModel() {
        App.getComponent().inject(this);
        mProfnastil = mRepository.getProfnastil();
    }

    public void selectItem(ProfnastilItem pricelistItem) {
        mSelectedProfnastil.setValue(pricelistItem);
        recalculate();
    }

    public LiveData<List<ProfnastilItem>> getItems() {
        return mProfnastil;
    }

    public LiveData<ProfnastilItem> getSelectedItem() {
        return mSelectedProfnastil;
    }

    public LiveData<List<Square>> getRoofs() {
        return mRoofs;
    }

    public LiveData<Float> getRoofSquare() {
        return mRoofSquare;
    }

    public LiveData<Float> getPrice() {
        return mProfnastilPrice;
    }

    public LiveData<Integer> getQuantity() {
        return mProfnastilQuantity;
    }

    public MutableLiveData<Float> getProfnastilSquare() {
        return mProfnastilSquare;
    }

    @Override
    public void removeSquare(Square square, String squareType) {
        if (SquaresAdapter.SQUARE_TYPE_ROOF.equals(squareType)) {
            removeRoof(square);
        }
    }

    public void addRoof(Square roof) {
        List<Square> roofs = mRoofs.getValue();
        if (roofs == null) roofs = new ArrayList<>();
        roofs.add(roof);
        mRoofs.setValue(roofs);
        recalculate();
    }

    public void removeRoof(Square roof) {
        List<Square> roofs = mRoofs.getValue();
        if (roofs == null) return;
        roofs.remove(roof);
        mRoofs.setValue(roofs);
        recalculate();
    }

    public void setProfnastilLength(float length) {
        mProfnastilLength = length;
        recalculate();
    }

    /**
     * Установка запаса товара
     *
     * @param reserve запас товара в процентах
     */
    public void setReserve(int reserve) {
        mProfnastilReserve = reserve;
        recalculate();
    }

    public void setWaveOverlap(int waves) {
        mOverlapWaves = waves;
        recalculate();
    }

    private void recalculate() {
        float roof = calculateRoofSquare();
        ProfnastilItem profnastil = getSelectedItem().getValue() == null ?
                new ProfnastilItem() : getSelectedItem().getValue();
        float profnastilSquare = profnastil.getWidth() * mProfnastilLength;
        float overlap = mProfnastilLength * mOverlapWaves * profnastil.getOverlap();
        int profnastilQuantity = profnastilSquare == 0f ? 0 : (int) Math.ceil(
                (1 + 0.01 * mProfnastilReserve) * (roof - overlap) / (profnastilSquare - overlap) );
        float profnastilPrice = profnastilSquare * profnastilQuantity * profnastil.getPrice();

        mRoofSquare.setValue(roof);
        mProfnastilSquare.setValue(profnastilSquare);
        mProfnastilQuantity.setValue(profnastilQuantity);
        mProfnastilPrice.setValue(profnastilPrice);
    }

    public float calculateRoofSquare() {
        float roofSquare = 0;
        List<Square> roofs = mRoofs.getValue();
        if (roofs == null) return 0f;
        for (Square roof : roofs) {
            roofSquare += roof.getSquare();
        }
        return roofSquare;
    }

    public boolean addToCart() {
        PricelistItem item = mSelectedProfnastil.getValue();
        if (item == null ||
                mProfnastilSquare.getValue() == null ||
                mProfnastilQuantity.getValue() == null) {
            return false;
        }

        float square = mProfnastilSquare.getValue();
        int lists = mProfnastilQuantity.getValue();
        float quantity = square * lists;
        if (quantity == 0) return false;

        mRepository.addCartItem(new CartItem(item, quantity));
        return true;
    }

}
