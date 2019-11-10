package alektas.stroymat.ui.calculators.profnastil;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.model.ProfnastilItem;
import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquaresAdapter;
import alektas.stroymat.utils.AppUtils;
import alektas.stroymat.utils.ItemUtils;
import alektas.stroymat.utils.ResourcesUtils;
import alektas.stroymat.utils.StringUtils;

public class ProfnastilFragment extends Fragment {
    private ProfnastilViewModel mViewModel;

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

        EditText roofWidthInput = view.findViewById(R.id.input_roof_width);
        EditText roofHeightInput = view.findViewById(R.id.input_roof_height);

        ImageButton addRoofBtn = view.findViewById(R.id.roof_add_btn);
        addRoofBtn.setOnClickListener(v -> {
            float width = StringUtils.getFloat(roofWidthInput);
            float height = StringUtils.getFloat(roofHeightInput);
            mViewModel.addRoof(new Square(width, height));
        });

        roofHeightInput.setOnEditorActionListener((v, actionId, event) -> {
            addRoofBtn.performClick();
            return false;
        });

        RecyclerView roofRv = view.findViewById(R.id.roof_list);
        RecyclerView.LayoutManager wallsLayoutManager = new LinearLayoutManager(getContext());
        roofRv.setLayoutManager(wallsLayoutManager);
        roofRv.setHasFixedSize(true);

        EditText profnastilLengthInput = view.findViewById(R.id.input_profnastil_length);
        profnastilLengthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mViewModel.setProfnastilLength(StringUtils.getFloat(profnastilLengthInput));
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        MaterialButton toCartBtn = view.findViewById(R.id.item_to_cart_btn);
        toCartBtn.setOnClickListener(v -> {
            boolean isSuccess = mViewModel.addToCart();
            Toast.makeText(getContext(),
                    isSuccess ? "Товар добавлен в корзину" :
                            "Укажите корректное количество товара (больше 0)",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfnastilViewModel.class);

        SquaresAdapter profnastilAdapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_ROOF);
        RecyclerView roofRv = requireView().findViewById(R.id.roof_list);
        roofRv.setAdapter(profnastilAdapter);

        setupProfnastilDropdown(mViewModel, requireView());
        setupWaveOverlapGroup(mViewModel, requireView());
        setupReserveSeekbar(mViewModel, requireView());

        subscribeOnModel(mViewModel, requireView(), profnastilAdapter);
    }

    private void setupProfnastilDropdown(ProfnastilViewModel viewModel, View rootView) {
        AutoCompleteTextView dropdown =
                requireView().findViewById(R.id.profnastil_dropdown);

        final List<ProfnastilItem> items = new ArrayList<>();
        viewModel.getItems().observe(getViewLifecycleOwner(), (newItems -> {
            items.clear();
            items.addAll(newItems);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    R.layout.spinner_dropdown_item, ItemUtils.getProfnastilNames(newItems));
            dropdown.setAdapter(adapter);
        }));

        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.selectItem(items.get(position));
            rootView.requestLayout();
        });

        dropdown.setOnClickListener(v -> {
            AppUtils.hideKeyboard(this);
        });

        TextView priceText = rootView.findViewById(R.id.profnastil_price);
        TextView widthText = rootView.findViewById(R.id.profnastil_width);
        ImageButton clearBtn = requireView().findViewById(R.id.profnastil_clear_btn);
        clearBtn.setOnClickListener(v -> {
            dropdown.setText("");
            priceText.setText(ResourcesUtils.getString(R.string.null_price));
            widthText.setText(ResourcesUtils.getString(R.string.null_price));
            viewModel.selectItem(new ProfnastilItem());
            rootView.requestLayout();
        });
    }

    private void setupReserveSeekbar(ProfnastilViewModel viewModel, View rootView) {
        TextView text = rootView.findViewById(R.id.profnastil_reserve);
        SeekBar seekbar = rootView.findViewById(R.id.profnastil_reserve_seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text.setText(String.valueOf(progress));
                viewModel.setReserve(progress);
                rootView.requestLayout();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setupWaveOverlapGroup(ProfnastilViewModel viewModel, View view) {
        MaterialButtonToggleGroup overlapGroup = view.findViewById(R.id.profnastil_overlap);
        overlapGroup.check(R.id.profnastil_overlap_1);
        mViewModel.setWaveOverlap(1);
        int[] prevCheckedId = { R.id.profnastil_overlap_1 };
        overlapGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                if (prevCheckedId[0] == checkedId) group.check(checkedId);
                return;
            }
            prevCheckedId[0] = checkedId;
            int size = 0;
            switch (checkedId) {
                case R.id.profnastil_overlap_1: size = 1; break;
                case R.id.profnastil_overlap_2: size = 2; break;
                case R.id.profnastil_overlap_3: size = 3; break;
            }
            viewModel.setWaveOverlap(size);
        });
    }

    private void subscribeOnModel(ProfnastilViewModel viewModel, View view, SquaresAdapter adapter) {
        mViewModel.getRoofs().observe(getViewLifecycleOwner(), roofs -> {
            adapter.setSquares(roofs);
            requireView().requestLayout();
        });
        TextView roofSquare = view.findViewById(R.id.result_roof_square);
        mViewModel.getRoofSquare().observe(this, square -> {
            roofSquare.setText(StringUtils.formatSquare(square));
        });
        TextView profnastilSquareText = view.findViewById(R.id.profnastil_square);
        mViewModel.getProfnastilSquare().observe(getViewLifecycleOwner(), square -> {
            profnastilSquareText.setText(StringUtils.formatSquare(square));
        });
        TextView profnastilPriceText = view.findViewById(R.id.profnastil_price);
        TextView profnastilWidthText = view.findViewById(R.id.profnastil_width);
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            profnastilPriceText.setText(StringUtils.formatPrice(item == null ? 0f : item.getPrice()));
            profnastilWidthText.setText(String.valueOf(item == null ? 0f : item.getWidth()));
            view.requestLayout();
        });
        TextView profnastilCountText = view.findViewById(R.id.result_profnastil_quantity);
        viewModel.getQuantity().observe(getViewLifecycleOwner(), bricksCount -> {
            profnastilCountText.setText(String.valueOf(bricksCount));
            view.requestLayout();
        });
        TextView priceText = view.findViewById(R.id.result_profnastil_price);
        viewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            priceText.setText(StringUtils.formatPrice(price));
            view.requestLayout();
        });
    }

}
