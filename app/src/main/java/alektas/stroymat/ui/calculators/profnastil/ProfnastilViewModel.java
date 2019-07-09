package alektas.stroymat.ui.calculators.profnastil;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquareViewModelBase;
import alektas.stroymat.ui.calculators.SquaresAdapter;

public class ProfnastilViewModel extends ViewModel implements SquareViewModelBase {
    private MutableLiveData<List<Square>> mRoofs = new MutableLiveData<>();
    private MutableLiveData<Float> mRoofSquare = new MutableLiveData<>();

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
        mRoofSquare.setValue(calculateRoofSquare());
    }

    public void removeRoof(Square roof) {
        List<Square> roofs = mRoofs.getValue();
        if (roofs == null) return;
        roofs.remove(roof);
        mRoofs.setValue(roofs);
        mRoofSquare.setValue(calculateRoofSquare());
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

    public MutableLiveData<List<Square>> getRoofs() {
        return mRoofs;
    }

    public MutableLiveData<Float> getRoofSquare() {
        return mRoofSquare;
    }

}
