package alektas.stroymat.ui.calculators;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import alektas.stroymat.R;

public class CalculatorsFragment extends Fragment {

    public static CalculatorsFragment newInstance() {
        return new CalculatorsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calculators_fragment, container, false);
    }

}
