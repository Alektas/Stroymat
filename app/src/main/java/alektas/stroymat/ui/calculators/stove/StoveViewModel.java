package alektas.stroymat.ui.calculators.stove;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import alektas.stroymat.data.ItemsRepository;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.PricelistItem;

public class StoveViewModel extends AndroidViewModel {
    private Repository mRepository;
    private LiveData<List<PricelistItem>> mBricks;
    private MutableLiveData<Float> mBaseSquare = new MutableLiveData<>();
    private MutableLiveData<PricelistItem> mSelectedBrick = new MutableLiveData<>();
    private MutableLiveData<Integer> mBricksCount = new MutableLiveData<>();
    private MutableLiveData<Float> mPrice = new MutableLiveData<>();
    private float mStoveHeight;
    private float mTubeHeight;
    private float mBaseWidth;
    private float mBaseLength;
    private final float mBrickHeight = 0.065f;
    private final float mBrickWidth = 0.12f;
    private final float mBrickLength = 0.25f;
    private final float mBrickSquare = mBrickLength * mBrickWidth;
    private final float mVerticalGap = 0.005f;
    private final float mRowHeight = mBrickHeight + mVerticalGap;
    private int mTubeRowBricks = 0;
    private int mBricksReserve;

    public StoveViewModel(@NonNull Application application) {
        super(application);
        mRepository = ItemsRepository.getInstance(application);
        mBricks = mRepository.getStoveBricks();
    }

    public MutableLiveData<Float> getBaseSquare() {
        return mBaseSquare;
    }

    public LiveData<List<PricelistItem>> getItems() {
        return mBricks;
    }

    public void selectItem(PricelistItem pricelistItem) {
        mSelectedBrick.setValue(pricelistItem);
        recalculate();
    }

    public LiveData<PricelistItem> getSelectedItem() {
        return mSelectedBrick;
    }

    /**
     * Установка запаса товара
     *
     * @param reserve запас товара в процентах
     */
    public void setReserve(int reserve) {
        mBricksReserve = reserve;
        recalculate();
    }

    public LiveData<Integer> getBricksCount() {
        return mBricksCount;
    }

    public LiveData<Float> getPrice() {
        return mPrice;
    }

    public void onTubeRowBricksChanged(int tubeRowBricks) {
        mTubeRowBricks = tubeRowBricks;
        recalculate();
    }

    public void onTubeHeightChanged(float tubeHeight) {
        mTubeHeight = tubeHeight;
        recalculate();
    }

    public void onStoveHeightChanged(float stoveHeight) {
        mStoveHeight = stoveHeight;
        recalculate();
    }

    public void onBaseWidthChanged(float baseWidth) {
        mBaseWidth = baseWidth;
        recalculate();
    }

    public void onBaseLengthChanged(float baseLength) {
        mBaseLength = baseLength;
        recalculate();
    }

    private void recalculate() {
        float baseSquare = mBaseWidth * mBaseLength;
        int rowBricks = (int) Math.ceil(baseSquare / mBrickSquare);
        int stoveRows = (int) Math.ceil(mStoveHeight / mRowHeight);
        int stoveBricks = (int) (0.666f * (stoveRows * rowBricks));
        int tubeBricks = (int) (mTubeHeight / mRowHeight * mTubeRowBricks);

        float actualBaseSquare = rowBricks * mBrickSquare;
        mBaseSquare.setValue(actualBaseSquare);

        int bricks = (int) Math.ceil((stoveBricks + tubeBricks) * (1 + 0.01f * mBricksReserve));
        mBricksCount.setValue(bricks);

        float brickPrice = mSelectedBrick.getValue() == null ?
                0f : mSelectedBrick.getValue().getPrice();
        mPrice.setValue(brickPrice * bricks);
    }

    public boolean addToCart() {
        PricelistItem item = mSelectedBrick.getValue();
        float quantity = mBricksCount.getValue() == null ? 0f : mBricksCount.getValue();
        if (item == null || quantity == 0) return false;

        mRepository.addCartItem(new CartItem(item, quantity));
        return true;
    }
}
