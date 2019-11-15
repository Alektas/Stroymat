package alektas.stroymat.ui.server;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import alektas.stroymat.R;

public class AdminFragment extends Fragment {

    private AdminViewModel mViewModel;

    public static AdminFragment newInstance() {
        return new AdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AdminViewModel.class);

        requireView().findViewById(R.id.btn_reset_pricelist).setOnClickListener(v -> {
            mViewModel.resetPricelist();
            Toast.makeText(requireContext(), "Запрос на удаление прайслиста отправлен", Toast.LENGTH_SHORT).show();
        });

        requireView().findViewById(R.id.btn_upload_pricelist).setOnClickListener(v -> {
            mViewModel.uploadPricelist();
            Toast.makeText(requireContext(), "Запрос на загрузку прайслиста отправлен", Toast.LENGTH_SHORT).show();
        });

        requireView().findViewById(R.id.btn_reset_categories).setOnClickListener(v -> {
            mViewModel.resetCategories();
            Toast.makeText(requireContext(), "Запрос на удаление категорий отправлен", Toast.LENGTH_SHORT).show();
        });

        requireView().findViewById(R.id.btn_upload_categories).setOnClickListener(v -> {
            mViewModel.uploadCategories();
            Toast.makeText(requireContext(), "Запрос на загрузку категорий отправлен", Toast.LENGTH_SHORT).show();
        });
    }

}
