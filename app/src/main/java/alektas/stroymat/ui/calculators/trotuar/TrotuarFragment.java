package alektas.stroymat.ui.calculators.trotuar;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.model.SizedItem;
import alektas.stroymat.ui.calculators.Square;
import alektas.stroymat.ui.calculators.SquaresAdapter;
import alektas.stroymat.utils.ItemUtils;
import alektas.stroymat.utils.ResourcesUtils;
import alektas.stroymat.utils.StringUtils;

public class TrotuarFragment extends Fragment {
    private TrotuarViewModel mViewModel;

    public static TrotuarFragment newInstance() {
        return new TrotuarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trotuar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TrotuarViewModel.class);
        setupDropdown(TrotuarViewModel.CATEG_PLITY, mViewModel, requireView());
        setupDropdown(TrotuarViewModel.CATEG_BORDURY, mViewModel, requireView());
        setupReserveSeekbar(TrotuarViewModel.CATEG_BORDURY, mViewModel, requireView());
        setupReserveSeekbar(TrotuarViewModel.CATEG_PLITY, mViewModel, requireView());

        SquaresAdapter adapter = new SquaresAdapter(mViewModel, SquaresAdapter.SQUARE_TYPE_TROTUAR);
        LinearLayoutManager mng = new LinearLayoutManager(getContext());
        RecyclerView rv = requireView().findViewById(R.id.trotuar_list);
        rv.setLayoutManager(mng);
        rv.setAdapter(adapter);

        EditText trotuarWidthInput = requireView().findViewById(R.id.input_trotuar_width);
        EditText trotuarHeightInput = requireView().findViewById(R.id.input_trotuar_height);
        ImageButton addTrotuarBtn = requireView().findViewById(R.id.trotuar_add_btn);
        addTrotuarBtn.setOnClickListener(v -> {
            float width = StringUtils.getFloat(trotuarWidthInput);
            float height = StringUtils.getFloat(trotuarHeightInput);
            mViewModel.addTrotuar(new Square(width, height));
        });

        subscribeOnModel(mViewModel, requireView(), adapter);
    }

    private void setupDropdown(int type, TrotuarViewModel viewModel, View rootView) {
        int dropdownRes = 0;
        int priceTextRes = 0;
        int unitsTextRes = 0;
        int squareTextRes = 0;
        int clearBtnRes = 0;
        if (TrotuarViewModel.CATEG_PLITY == type) {
            dropdownRes = R.id.trotuar_plity_dropdown;
            priceTextRes = R.id.trotuar_plity_price;
            squareTextRes = R.id.plity_square;
            unitsTextRes = R.id.trotuar_plity_price_units;
            clearBtnRes = R.id.plity_clear_btn;
        } else if (TrotuarViewModel.CATEG_BORDURY == type) {
            dropdownRes = R.id.trotuar_bordury_dropdown;
            priceTextRes = R.id.trotuar_bordury_price;
            squareTextRes = R.id.bordury_square;
            unitsTextRes = R.id.trotuar_bordury_price_units;
            clearBtnRes = R.id.bordury_clear_btn;
        }

        AutoCompleteTextView dropdown =
                requireView().findViewById(dropdownRes);
        final List<SizedItem> items = new ArrayList<>();
        viewModel.getItems(type).observe(getViewLifecycleOwner(), (newItems -> {
            items.clear();
            items.addAll(newItems);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                    R.layout.spinner_dropdown_item, ItemUtils.getNames(newItems));
            dropdown.setAdapter(adapter);
        }));
        dropdown.setOnItemClickListener((parent, view, position, id) -> {
            viewModel.selectItem(type, items.get(position));
            rootView.requestLayout();
        });
        TextView priceText = rootView.findViewById(priceTextRes);
        TextView unitsText = rootView.findViewById(unitsTextRes);
        TextView squareText = rootView.findViewById(squareTextRes);
        viewModel.getSelectedItem(type).observe(getViewLifecycleOwner(), item -> {
            priceText.setText(StringUtils.formatPrice(item == null ? 0f : item.getPrice()));
            squareText.setText(StringUtils.formatSquare(item == null ? 0f : item.getSquare()));
            unitsText.setText(item == null || TextUtils.isEmpty(item.getUnit())?
                    requireContext().getString(R.string.quantity_units_default) : item.getUnit());
        });
        ImageButton clearBtn = requireView().findViewById(clearBtnRes);
        clearBtn.setOnClickListener(v -> {
            dropdown.setText("");
            priceText.setText(ResourcesUtils.getString(R.string.null_price));
            rootView.requestLayout();
            viewModel.selectItem(type, null);
        });
    }

    private void setupReserveSeekbar(int type, TrotuarViewModel viewModel, View rootView) {
        int textRes = 0;
        int seekbarRes = 0;
        if (TrotuarViewModel.CATEG_PLITY == type) {
            textRes = R.id.plity_reserve;
            seekbarRes = R.id.plity_reserve_seekbar;
        } else if (TrotuarViewModel.CATEG_BORDURY == type) {
            textRes = R.id.bordury_reserve;
            seekbarRes = R.id.bordury_reserve_seekbar;
        }
        TextView text = rootView.findViewById(textRes);
        SeekBar seekbar = rootView.findViewById(seekbarRes);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                text.setText(String.valueOf(progress));
                viewModel.setReserve(type, progress);
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

    private void subscribeOnModel(TrotuarViewModel viewModel, View view, SquaresAdapter adapter) {
        viewModel.getTrotuars().observe(getViewLifecycleOwner(), trotuars -> {
            adapter.setSquares(trotuars);
            view.requestLayout();
        });
        TextView trotuarSquareText = view.findViewById(R.id.result_trotuar_square);
        viewModel.getTrotuarsSquare().observe(getViewLifecycleOwner(), trotuarSquare -> {
            trotuarSquareText.setText(StringUtils.formatSquare(trotuarSquare));
        });
        TextView bordursCountText = view.findViewById(R.id.result_bordury_quantity);
        viewModel.getBordursCount().observe(getViewLifecycleOwner(), bordursCount -> {
            bordursCountText.setText(String.valueOf(bordursCount));
        });
        TextView plityCountText = view.findViewById(R.id.result_plity_quantity);
        viewModel.getPlityCount().observe(getViewLifecycleOwner(), plityCount -> {
            plityCountText.setText(String.valueOf(plityCount));
        });
        TextView priceText = view.findViewById(R.id.result_trotuar_price);
        viewModel.getPrice().observe(getViewLifecycleOwner(), price -> {
            priceText.setText(StringUtils.formatPrice(price));
        });
    }

}
