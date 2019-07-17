package alektas.stroymat.ui.calculators.stove;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.utils.ItemUtils;
import alektas.stroymat.utils.ResourcesUtils;
import alektas.stroymat.utils.StringUtils;

public class StoveFragment extends Fragment {
    private StoveViewModel mViewModel;
    private static final int BASE_WIDTH_TAG = 0;
    private static final int BASE_HEIGHT_TAG = 1;
    private static final int STOVE_HEIGHT_TAG = 2;
    private static final int TUBE_HEIGHT_TAG = 3;
    private EditText baseWidthInput;
    private EditText baseHeightInput;
    private EditText stoveHeightInput;
    private EditText tubeHeightInput;

    public static StoveFragment newInstance() {
        return new StoveFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stove_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StoveViewModel.class);

        baseWidthInput = requireView().findViewById(R.id.stove_base_width_input);
        baseHeightInput = requireView().findViewById(R.id.stove_base_length_input);
        stoveHeightInput = requireView().findViewById(R.id.stove_height_input);
        tubeHeightInput = requireView().findViewById(R.id.stove_tube_height_input);
        baseWidthInput.addTextChangedListener(new FieldWatcher(BASE_WIDTH_TAG));
        baseHeightInput.addTextChangedListener(new FieldWatcher(BASE_HEIGHT_TAG));
        stoveHeightInput.addTextChangedListener(new FieldWatcher(STOVE_HEIGHT_TAG));
        tubeHeightInput.addTextChangedListener(new FieldWatcher(TUBE_HEIGHT_TAG));

        setupBricksDropdown(mViewModel, requireView());
        setupReserveSeekbar(mViewModel, requireView());
        setupTubeSizesGroup(mViewModel, requireView());

        subscribeOnModel(mViewModel, requireView());
    }


    private void setupBricksDropdown(StoveViewModel viewModel, View rootView) {
        AutoCompleteTextView dropdown =
                requireView().findViewById(R.id.stove_brick_dropdown);
        final List<PricelistItem> items = new ArrayList<>();
        viewModel.getItems().observe(getViewLifecycleOwner(), (newItems -> {
            items.clear();
            items.addAll(newItems);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    R.layout.spinner_dropdown_item, ItemUtils.getPricelistNames(newItems));
            dropdown.setAdapter(adapter);
        }));
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.selectItem(items.get(position));
            rootView.requestLayout();
        });
        TextView priceText = rootView.findViewById(R.id.stove_brick_price);
        ImageButton clearBtn = requireView().findViewById(R.id.brick_clear_btn);
        clearBtn.setOnClickListener(v -> {
            dropdown.setText("");
            priceText.setText(ResourcesUtils.getString(R.string.null_price));
            viewModel.selectItem(null);
            rootView.requestLayout();
        });
    }

    private void setupReserveSeekbar(StoveViewModel viewModel, View rootView) {
        TextView text = rootView.findViewById(R.id.stove_brick_reserve);
        SeekBar seekbar = rootView.findViewById(R.id.stove_brick_reserve_seekbar);
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

    private void setupTubeSizesGroup(StoveViewModel viewModel, View view) {
        MaterialButtonToggleGroup tubeSizes = view.findViewById(R.id.stove_tube_sizes);
        tubeSizes.check(R.id.tube_size_6);
        mViewModel.onTubeRowBricksChanged(6);
        int[] prevCheckedId = { R.id.tube_size_6 };
        tubeSizes.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (!isChecked) {
                if (prevCheckedId[0] == checkedId) group.check(checkedId);
                return;
            }
            prevCheckedId[0] = checkedId;
            int size = 0;
            switch (checkedId) {
                case R.id.tube_size_4: size = 4; break;
                case R.id.tube_size_5: size = 5; break;
                case R.id.tube_size_6: size = 6; break;
            }
            viewModel.onTubeRowBricksChanged(size);
        });
    }

    private void subscribeOnModel(StoveViewModel viewModel, View view) {
        TextView brickPriceText = view.findViewById(R.id.stove_brick_price);
        TextView unitsText = view.findViewById(R.id.stove_brick_price_units);
        viewModel.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            brickPriceText.setText(StringUtils.format(item == null ? 0f : item.getPrice()));
            unitsText.setText(item == null || TextUtils.isEmpty(item.getUnit())?
                    requireContext().getString(R.string.quantity_units_default) : item.getUnit());
            view.requestLayout();
        });
        TextView baseSquareText = view.findViewById(R.id.result_stove_base_square);
        viewModel.getBaseSquare().observe(getViewLifecycleOwner(), baseSquare -> {
            baseSquareText.setText(StringUtils.format(baseSquare));
            view.requestLayout();
        });
        TextView bricksCountText = view.findViewById(R.id.result_bricks_quantity);
        viewModel.getBricksCount().observe(getViewLifecycleOwner(), bricksCount -> {
            bricksCountText.setText(String.valueOf(bricksCount));
            view.requestLayout();
        });
        TextView priceText = view.findViewById(R.id.result_stove_price);
        viewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            priceText.setText(StringUtils.format(price));
            view.requestLayout();
        });
    }

    private void onFieldChanged(int fieldTag) {
        switch (fieldTag) {
            case BASE_WIDTH_TAG:
                mViewModel.onBaseWidthChanged(StringUtils.getFloat(baseWidthInput));
                break;
            case BASE_HEIGHT_TAG:
                mViewModel.onBaseLengthChanged(StringUtils.getFloat(baseHeightInput));
                break;
            case STOVE_HEIGHT_TAG:
                mViewModel.onStoveHeightChanged(StringUtils.getFloat(stoveHeightInput));
                break;
            case TUBE_HEIGHT_TAG:
                mViewModel.onTubeHeightChanged(StringUtils.getFloat(tubeHeightInput));
                break;
        }
    }

    private class FieldWatcher implements TextWatcher {
        private int mFieldTag;

        public FieldWatcher(int fieldTag) {
            mFieldTag = fieldTag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onFieldChanged(mFieldTag);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
