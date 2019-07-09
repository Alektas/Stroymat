package alektas.stroymat.ui.calculators.siding;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquareViewModelBase;
import alektas.stroymat.ui.calculators.SquaresAdapter;

public class SidingViewModel extends ViewModel implements SquareViewModelBase {
    private MutableLiveData<List<Square>> mWalls = new MutableLiveData<>();
    private MutableLiveData<Float> mWallsSquare = new MutableLiveData<>();
    private MutableLiveData<List<Square>> mWindows = new MutableLiveData<>();
    private MutableLiveData<Float> mWindowsSquare = new MutableLiveData<>();
    private MutableLiveData<List<Square>> mFrontons = new MutableLiveData<>();
    private MutableLiveData<Float> mFrontonSquare = new MutableLiveData<>();

    @Override
    public void removeSquare(Square square, String squareType) {
        switch (squareType) {
            case SquaresAdapter.SQUARE_TYPE_WALL: removeWall(square); break;
            case SquaresAdapter.SQUARE_TYPE_WINDOW: removeWindow(square); break;
            case SquaresAdapter.SQUARE_TYPE_FRONTON: removeFronton(square); break;
        }
    }

    public void addWall(Square wall) {
        List<Square> walls = mWalls.getValue();
        if (walls == null) walls = new ArrayList<>();
        walls.add(wall);
        mWalls.setValue(walls);
        mWallsSquare.setValue(calculateWallsSquare());
    }

    public void removeWall(Square wall) {
        List<Square> walls = mWalls.getValue();
        if (walls == null) return;
        walls.remove(wall);
        mWalls.setValue(walls);
        mWallsSquare.setValue(calculateWallsSquare());
    }

    public float calculateWallsSquare() {
        float wallsSquare = 0;
        List<Square> walls = mWalls.getValue();
        if (walls == null) return 0f;
        for (Square wall : walls) {
            wallsSquare += wall.getSquare();
        }
        return wallsSquare;
    }

    public void addWindow(Square window) {
        List<Square> windows = mWindows.getValue();
        if (windows == null) windows = new ArrayList<>();
        windows.add(window);
        mWindows.setValue(windows);
        mWindowsSquare.setValue(calculateWindowsSquare());
    }

    public void removeWindow(Square window) {
        List<Square> windows = mWindows.getValue();
        if (windows == null) return;
        windows.remove(window);
        mWindows.setValue(windows);
        mWindowsSquare.setValue(calculateWindowsSquare());
    }

    public float calculateWindowsSquare() {
        float windowsSquare = 0;
        List<Square> windows = mWindows.getValue();
        if (windows == null) return 0f;
        for (Square window : windows) {
            windowsSquare += window.getSquare();
        }
        return windowsSquare;
    }

    public void addFronton(Square fronton) {
        List<Square> frontons = mFrontons.getValue();
        if (frontons == null) frontons = new ArrayList<>();
        frontons.add(fronton);
        mFrontons.setValue(frontons);
        mFrontonSquare.setValue(calculateFrontonSquare());
    }

    public void removeFronton(Square fronton) {
        List<Square> frontons = mFrontons.getValue();
        if (frontons == null) return;
        frontons.remove(fronton);
        mFrontons.setValue(frontons);
        mFrontonSquare.setValue(calculateFrontonSquare());
    }

    public float calculateFrontonSquare() {
        float frontonsSquare = 0;
        List<Square> frontons = mFrontons.getValue();
        if (frontons == null) return 0f;
        for (Square fronton : frontons) {
            frontonsSquare += fronton.getSquare();
        }
        return frontonsSquare;
    }

    public LiveData<List<Square>> getWalls() {
        return mWalls;
    }

    public LiveData<List<Square>> getWindows() {
        return mWindows;
    }

    public LiveData<Float> getWallsSquare() {
        return mWallsSquare;
    }

    public LiveData<Float> getWindowsSquare() {
        return mWindowsSquare;
    }

    public LiveData<List<Square>> getFrontons() {
        return mFrontons;
    }

    public LiveData<Float> getFrontonSquare() {
        return mFrontonSquare;
    }
}
