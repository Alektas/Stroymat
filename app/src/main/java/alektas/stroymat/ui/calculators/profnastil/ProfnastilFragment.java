package alektas.stroymat.ui.calculators.profnastil;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquaresAdapter;
import alektas.stroymat.utils.StringUtils;

public class ProfnastilFragment extends Fragment implements TextWatcher {
    private ProfnastilViewModel mViewModel;
    private EditText roofWidthInput;
    private EditText roofHeightInput;
    private EditText profnastilWidthInput;
    private EditText profnastilHeightInput;
    private EditText profnastilPriceInput;
    private TextView roofSquare;
    private TextView profnastilPrice;
    private TextView profnastilSquare;
    private TextView profnastilQuantity;
    private SquaresAdapter profnastilAdapter;

    public static ProfnastilFragment newInstance() {
        return new ProfnastilFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profnastil_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        roofSquare = view.findViewById(R.id.result_roof_square);
        profnastilPrice = view.findViewById(R.id.result_profnastil_price);
        profnastilSquare = view.findViewById(R.id.result_profnastil_square);
        profnastilQuantity = view.findViewById(R.id.result_profnastil_quantity);
        roofWidthInput = view.findViewById(R.id.input_roof_width);
        roofHeightInput = view.findViewById(R.id.input_roof_height);
        profnastilWidthInput = view.findViewById(R.id.input_profnastil_width);
        profnastilHeightInput = view.findViewById(R.id.input_profnastil_height);
        profnastilPriceInput = view.findViewById(R.id.input_profnastil_price);
        ImageButton addRoofBtn = view.findViewById(R.id.roof_add_btn);
        addRoofBtn.setOnClickListener(v -> addRoof());

        RecyclerView roofRv = view.findViewById(R.id.roof_list);
        RecyclerView.LayoutManager wallsLayoutManager = new LinearLayoutManager(getContext());
        roofRv.setLayoutManager(wallsLayoutManager);
        roofRv.setHasFixedSize(true);

        profnastilHeightInput.addTextChangedListener(this);
        profnastilWidthInput.addTextChangedListener(this);
        profnastilPriceInput.addTextChangedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfnastilViewModel.class);
        profnastilAdapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_ROOF);
        RecyclerView roofRv = getView().findViewById(R.id.roof_list);
        roofRv.setAdapter(profnastilAdapter);
        mViewModel.getRoofs().observe(getViewLifecycleOwner(), roofs -> {
            profnastilAdapter.setSquares(roofs);
            requireView().requestLayout();
        });
        mViewModel.getRoofSquare().observe(this, square -> updateSidingCalc());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        profnastilHeightInput.removeTextChangedListener(this);
        profnastilWidthInput.removeTextChangedListener(this);
        profnastilPriceInput.removeTextChangedListener(this);
    }

    private void addRoof() {
        String widthString = roofWidthInput.getText().toString();
        float width = TextUtils.isEmpty(widthString) ? 0f : Float.valueOf(widthString);
        String heightString = roofHeightInput.getText().toString();
        float height = TextUtils.isEmpty(heightString) ? 0f : Float.valueOf(heightString);
        Square roof = new Square(width, height);
        mViewModel.addRoof(roof);
    }

    private void updateSidingCalc() {
        float roofSquare = 0;
        List<Square> walls = profnastilAdapter.getSquares();
        for (Square wall : walls) {
            roofSquare += wall.getSquare();
        }

        float profnastilOneSquare = getFloat(profnastilWidthInput) * getFloat(profnastilHeightInput);
        float profnastilPricePerMeter = getFloat(profnastilPriceInput);
        int profnastilQuantity =
                profnastilOneSquare == 0 ? 0 : (int) Math.ceil(roofSquare / profnastilOneSquare);

        String roofSquareString =  StringUtils.format(roofSquare);
        String profnastilSquareString = StringUtils.format(profnastilOneSquare);
        String profnastilQuantityString = String.valueOf(profnastilQuantity);
        String profnastilPriceString = StringUtils.format(
                profnastilPricePerMeter
                * profnastilQuantity
                * profnastilOneSquare);

        this.roofSquare.setText(roofSquareString);
        this.profnastilSquare.setText(profnastilSquareString);
        this.profnastilPrice.setText(profnastilPriceString);
        this.profnastilQuantity.setText(profnastilQuantityString);
    }

    private float getFloat(EditText editText) {
        String text = editText.getText().toString();
        if (text.equals("")) return 0f;
        return Float.parseFloat(text);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        updateSidingCalc();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
