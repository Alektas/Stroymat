package alektas.stroymat.ui.calculators.siding;

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

public class SidingFragment extends Fragment implements TextWatcher {
    private SidingViewModel mViewModel;
    private EditText wallsWidthInput;
    private EditText wallsHeightInput;
    private EditText windowsWidthInput;
    private EditText windowsHeightInput;
    private EditText frontonsWidthInput;
    private EditText frontonsHeightInput;
    private EditText sidingWidthInput;
    private EditText sidingHeightInput;
    private EditText sidingPriceInput;
    private TextView wallSquare;
    private TextView windowSquare;
    private TextView frontonSquare;
    private TextView sidingPrice;
    private TextView sidingSquare;
    private TextView sidingQuantity;
    private SquaresAdapter wallsAdapter;
    private SquaresAdapter windowsAdapter;
    private SquaresAdapter frontonsAdapter;

    public static SidingFragment newInstance() {
        return new SidingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.siding_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wallSquare = view.findViewById(R.id.result_walls_square);
        windowSquare = view.findViewById(R.id.result_windows_square);
        frontonSquare = view.findViewById(R.id.result_fronton_square);
        sidingPrice = view.findViewById(R.id.result_siding_price);
        sidingSquare = view.findViewById(R.id.result_siding_square);
        sidingQuantity = view.findViewById(R.id.result_siding_quantity);
        wallsWidthInput = view.findViewById(R.id.input_walls_width);
        wallsHeightInput = view.findViewById(R.id.input_walls_height);
        windowsWidthInput = view.findViewById(R.id.input_windows_width);
        windowsHeightInput = view.findViewById(R.id.input_windows_height);
        frontonsWidthInput = view.findViewById(R.id.input_fronton_width);
        frontonsHeightInput = view.findViewById(R.id.input_fronton_height);
        sidingWidthInput = view.findViewById(R.id.input_siding_width);
        sidingHeightInput = view.findViewById(R.id.input_siding_height);
        sidingPriceInput = view.findViewById(R.id.input_siding_price);
        ImageButton addWallBtn = view.findViewById(R.id.walls_add_btn);
        addWallBtn.setOnClickListener(v -> addWall());
        ImageButton addWindowBtn = view.findViewById(R.id.window_add_btn);
        addWindowBtn.setOnClickListener(v -> addWindow());
        ImageButton addFrontonBtn = view.findViewById(R.id.fronton_add_btn);
        addFrontonBtn.setOnClickListener(v -> addFronton());

        wallsHeightInput.setOnEditorActionListener((v, actionId, event) -> {
            addWallBtn.performClick();
            return false;
        });

        windowsHeightInput.setOnEditorActionListener((v, actionId, event) -> {
            addWindowBtn.performClick();
            return false;
        });

        frontonsHeightInput.setOnEditorActionListener((v, actionId, event) -> {
            addFrontonBtn.performClick();
            return false;
        });

        RecyclerView wallsRv = view.findViewById(R.id.walls_list);
        RecyclerView.LayoutManager wallsLayoutManager = new LinearLayoutManager(getContext());
        wallsRv.setLayoutManager(wallsLayoutManager);
        wallsRv.setHasFixedSize(true);

        RecyclerView windowsRv = view.findViewById(R.id.windows_list);
        RecyclerView.LayoutManager windowsLayoutManager = new LinearLayoutManager(getContext());
        windowsRv.setLayoutManager(windowsLayoutManager);
        windowsRv.setHasFixedSize(true);

        RecyclerView frontonsRv = view.findViewById(R.id.fronton_list);
        RecyclerView.LayoutManager frontonsLayoutManager = new LinearLayoutManager(getContext());
        frontonsRv.setLayoutManager(frontonsLayoutManager);
        frontonsRv.setHasFixedSize(true);

        sidingHeightInput.addTextChangedListener(this);
        sidingWidthInput.addTextChangedListener(this);
        sidingPriceInput.addTextChangedListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SidingViewModel.class);

        wallsAdapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_WALL);
        windowsAdapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_WINDOW);
        frontonsAdapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_FRONTON);

        RecyclerView wallsRv = requireView().findViewById(R.id.walls_list);
        wallsRv.setAdapter(wallsAdapter);
        RecyclerView windowsRv = requireView().findViewById(R.id.windows_list);
        windowsRv.setAdapter(windowsAdapter);
        RecyclerView frontonsRv = requireView().findViewById(R.id.fronton_list);
        frontonsRv.setAdapter(frontonsAdapter);

        mViewModel.getWalls().observe(this, walls -> {
            wallsAdapter.setSquares(walls);
            requireView().requestLayout();
        });
        mViewModel.getWallsSquare().observe(this, square -> updateSidingCalc());
        mViewModel.getWindows().observe(this, windows -> {
            windowsAdapter.setSquares(windows);
            requireView().requestLayout();
        });
        mViewModel.getWindowsSquare().observe(this, square -> updateSidingCalc());
        mViewModel.getFrontons().observe(this, frontons -> {
            frontonsAdapter.setSquares(frontons);
            requireView().requestLayout();
        });
        mViewModel.getFrontonSquare().observe(this, square -> updateSidingCalc());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sidingHeightInput.removeTextChangedListener(this);
        sidingWidthInput.removeTextChangedListener(this);
        sidingPriceInput.removeTextChangedListener(this);
    }

    private void addWall() {
        String widthString = wallsWidthInput.getText().toString();
        float width = TextUtils.isEmpty(widthString) ? 0f : Float.valueOf(widthString);
        String heightString = wallsHeightInput.getText().toString();
        float height = TextUtils.isEmpty(heightString) ? 0f : Float.valueOf(heightString);
        Square wall = new Square(width, height);
        mViewModel.addWall(wall);
    }

    private void addWindow() {
        String widthString = windowsWidthInput.getText().toString();
        float width = TextUtils.isEmpty(widthString) ? 0f : Float.valueOf(widthString);
        String heightString = windowsHeightInput.getText().toString();
        float height = TextUtils.isEmpty(heightString) ? 0f : Float.valueOf(heightString);
        Square window = new Square(width, height);
        mViewModel.addWindow(window);
    }

    private void addFronton() {
        String widthString = frontonsWidthInput.getText().toString();
        float width = TextUtils.isEmpty(widthString) ? 0f : Float.valueOf(widthString);
        String heightString = frontonsHeightInput.getText().toString();
        float height = TextUtils.isEmpty(heightString) ? 0f : Float.valueOf(heightString);
        Square fronton = new Square(width, height, true);
        mViewModel.addFronton(fronton);
    }

    private void updateSidingCalc() {
        float wallsSquare = 0;
        List<Square> walls = wallsAdapter.getSquares();
        for (Square wall : walls) {
            wallsSquare += wall.getSquare();
        }

        float windowsSquare = 0;
        List<Square> windows = windowsAdapter.getSquares();
        for (Square window : windows) {
            windowsSquare += window.getSquare();
        }

        float frontonsSquare = 0;
        List<Square> frontons = frontonsAdapter.getSquares();
        for (Square fronton : frontons) {
            frontonsSquare += fronton.getSquare();
        }

        float sidingSquare = wallsSquare + frontonsSquare - windowsSquare;
        float sidingOneSquare = StringUtils.getFloat(sidingWidthInput) * StringUtils.getFloat(sidingHeightInput);
        float sidingOnePrice = StringUtils.getFloat(sidingPriceInput);
        int sidingQuantity =
                sidingOneSquare == 0 ? 0 : (int) Math.ceil(sidingSquare / sidingOneSquare);

        String wallSquareString =  StringUtils.formatPrice(wallsSquare);
        String windowSquareString = StringUtils.formatPrice(windowsSquare);
        String frontonSquareString = StringUtils.formatPrice(frontonsSquare);
        String sidingSquareString = StringUtils.formatPrice(sidingSquare);
        String sidingQuantityString = String.valueOf(sidingQuantity);
        String sidingPriceString = StringUtils.formatPrice(sidingOnePrice * sidingQuantity);

        this.wallSquare.setText(wallSquareString);
        this.windowSquare.setText(windowSquareString);
        this.frontonSquare.setText(frontonSquareString);
        this.sidingSquare.setText(sidingSquareString);
        this.sidingPrice.setText(sidingPriceString);
        this.sidingQuantity.setText(sidingQuantityString);
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
