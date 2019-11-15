package alektas.stroymat.ui.calculators.trotuar;

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
import alektas.stroymat.data.model.SizedItem;
import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquareViewModelBase;
import alektas.stroymat.ui.calculators.SquaresAdapter;
import alektas.stroymat.utils.StringUtils;

public class TrotuarViewModel extends ViewModel implements SquareViewModelBase {
    public static final int CATEG_PLITY = 19;
    public static final int CATEG_BORDURY = 36;
    @Inject
    public Repository mRepository;
    private LiveData<List<SizedItem>> mPlity;
    private LiveData<List<SizedItem>> mBordury;
    private MutableLiveData<List<Square>> mTrotuars = new MutableLiveData<>();
    private MutableLiveData<Float> mTrotuarsSquare = new MutableLiveData<>();
    private MutableLiveData<SizedItem> mSelectedPlita = new MutableLiveData<>();
    private MutableLiveData<SizedItem> mSelectedBordur = new MutableLiveData<>();
    private MutableLiveData<Integer> mPlityCount = new MutableLiveData<>();
    private MutableLiveData<Integer> mBorduryCount = new MutableLiveData<>();
    private MutableLiveData<Float> mPrice = new MutableLiveData<>();
    private int mPlityReserve;
    private int mBorduryReserve;

    public TrotuarViewModel() {
        App.getComponent().inject(this);
        mPlity = mRepository.getPlity();
        mBordury = mRepository.getBordury();
    }

    @Override
    public void removeSquare(Square square, String squareType) {
        if (SquaresAdapter.SQUARE_TYPE_TROTUAR.equals(squareType)) {
            removeTrotuar(square);
        }
    }

    public void addTrotuar(Square trotuar) {
        List<Square> trotuars = mTrotuars.getValue();
        if (trotuars == null) trotuars = new ArrayList<>();
        trotuars.add(trotuar);
        mTrotuars.setValue(trotuars);
        recalculate();
    }

    public void removeTrotuar(Square trotuar) {
        List<Square> trotuars = mTrotuars.getValue();
        if (trotuars == null) return;
        trotuars.remove(trotuar);
        mTrotuars.setValue(trotuars);
        recalculate();
    }

    public MutableLiveData<List<Square>> getTrotuars() {
        return mTrotuars;
    }

    public MutableLiveData<Float> getTrotuarsSquare() {
        return mTrotuarsSquare;
    }

    public LiveData<List<SizedItem>> getItems(int type) {
        if (CATEG_BORDURY == type) return getBordury();
        else if (CATEG_PLITY == type) return getPlity();
        return null;
    }

    public LiveData<List<SizedItem>> getPlity() {
        return mPlity;
    }

    public LiveData<List<SizedItem>> getBordury() {
        return mBordury;
    }

    public void selectItem(int type, SizedItem pricelistItem) {
        if (CATEG_BORDURY == type) selectBordur(pricelistItem);
        else if (CATEG_PLITY == type) selectPlita(pricelistItem);
    }

    public void selectBordur(SizedItem pricelistItem) {
        mSelectedBordur.setValue(pricelistItem);
        recalculate();
    }

    public void selectPlita(SizedItem pricelistItem) {
        mSelectedPlita.setValue(pricelistItem);
        recalculate();
    }
    /**
     * Установка запаса товара
     * @param reserve запас товара в процентах
     */
    public void setReserve(int type, int reserve) {
        if (CATEG_BORDURY == type) setBorduryReserve(reserve);
        else if (CATEG_PLITY == type) setPlityReserve(reserve);
    }

    /**
     * Установка запаса тротуарных плит
     * @param reserve запас плит в процентах
     */
    public void setPlityReserve(int reserve) {
        mPlityReserve = reserve;
        recalculate();
    }

    /**
     * Установка запаса бордюров
     * @param reserve запас бордюров в процентах
     */
    public void setBorduryReserve(int reserve) {
        mBorduryReserve = reserve;
        recalculate();
    }

    public LiveData<SizedItem> getSelectedItem(int type) {
        if (CATEG_BORDURY == type) return getSelectedBordur();
        else if (CATEG_PLITY == type) return getSelectedPlita();
        return null;
    }

    public LiveData<SizedItem> getSelectedBordur() {
        return mSelectedBordur;
    }

    public LiveData<SizedItem> getSelectedPlita() {
        return mSelectedPlita;
    }

    public LiveData<Integer> getPlityCount() {
        return mPlityCount;
    }

    public LiveData<Integer> getBordursCount() {
        return mBorduryCount;
    }

    public LiveData<Float> getPrice() {
        return mPrice;
    }

    private void recalculate() {
        float trotuarSquare = calculateTrotuarSquare();
        float trotuarPerimeter = calculateTrotuarPerimeter();
        float bordurPieceLength = getSelectedBordur().getValue() == null ?
                0 : getSelectedBordur().getValue().getLength();
        float plitaPieceSquare = getSelectedPlita().getValue() == null ?
                0 : getSelectedPlita().getValue().getSquare();
        int plityCount = calcualtePlityCount(trotuarSquare, plitaPieceSquare);
        int borduryCount = calcualteBordursCount(trotuarPerimeter, bordurPieceLength);
        applyCalculations(trotuarSquare, plityCount, borduryCount);
    }

    private void applyCalculations(float trotuarSquare, int plityCount, int borduryCount) {
        mTrotuarsSquare.setValue(trotuarSquare);
        mPlityCount.setValue(plityCount);
        mBorduryCount.setValue(borduryCount);
        mPrice.setValue(calculatePrice(plityCount, borduryCount));
    }

    private float calculateTrotuarSquare() {
        List<Square> trotuars = mTrotuars.getValue();
        if (trotuars == null) return 0f;
        float trotuarSquare = 0;
        for (Square trotuar : trotuars) {
            trotuarSquare += trotuar.getSquare();
        }
        return trotuarSquare < 0f ? 0f : trotuarSquare;
    }

    private float calculateTrotuarPerimeter() {
        List<Square> trotuars = mTrotuars.getValue();
        if (trotuars == null) return 0f;
        float trotuarPerimeter = 0;
        for (Square trotuar : trotuars) {
            trotuarPerimeter += 2 * (trotuar.getHeight() + trotuar.getWidth());
        }
        return trotuarPerimeter;
    }

    private int calcualtePlityCount(float totalSquare, float pieceSquare) {
        int count = (int) Math.ceil( (totalSquare / pieceSquare) * (1 + 0.01 * mPlityReserve));
        return (count < 0 || pieceSquare == 0) ? 0 : count;
    }

    private int calcualteBordursCount(float totalPerimeter, float pieceLength) {
        int count = (int) Math.ceil((totalPerimeter / pieceLength) * (1 + 0.01 * mBorduryReserve));
        return (count < 0 || pieceLength == 0) ? 0 : count;
    }

    private float calculatePrice(int plityCount, int borduryCount) {
        float bordurPrice = getSelectedBordur().getValue() == null ?
                0f : getSelectedBordur().getValue().getPrice();
        SizedItem plita = getSelectedPlita().getValue();
        if (plita == null) {
            return borduryCount*bordurPrice;
        } else {
            float plitaPrice = StringUtils.isSquareUnit(plita.getUnit()) ?
                    (plita.getPrice() * plita.getSquare()) : plita.getPrice();
            return plityCount*plitaPrice + borduryCount*bordurPrice;
        }
    }

    public boolean addToCart() {
        PricelistItem bordurItem = mSelectedBordur.getValue();
        SizedItem plitaItem = mSelectedPlita.getValue();
        float plityQuantity = mPlityCount.getValue() == null ? 0f : mPlityCount.getValue();
        float bordurQuantity = mBorduryCount.getValue() == null ? 0f : mBorduryCount.getValue();

        if (plitaItem != null && StringUtils.isSquareUnit(plitaItem.getUnit())) {
            plityQuantity *= plitaItem.getSquare();
        }
        if (plityQuantity != 0 && plitaItem != null) {
            mRepository.addCartItem(new CartItem(plitaItem, plityQuantity));
        }

        if (bordurQuantity != 0 && bordurItem != null) {
            mRepository.addCartItem(new CartItem(bordurItem, bordurQuantity));
        }

        return plityQuantity != 0 || bordurQuantity != 0;
    }

}
