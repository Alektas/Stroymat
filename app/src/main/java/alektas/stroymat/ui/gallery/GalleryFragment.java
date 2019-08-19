package alektas.stroymat.ui.gallery;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import alektas.stroymat.R;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private GalleryViewModel mViewModel;
    private boolean isReloaded = false; // Для попытки перезагрузить фотографии с сервера

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_fragment, container, false);
        RecyclerView rv = view.findViewById(R.id.gallery_grid);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setItemViewCacheSize(10); // Исправляет баг с хаотичными скачками фотографий
        rv.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        RecyclerView rv = requireView().findViewById(R.id.gallery_grid);
        GalleryAdapter galleryAdapter = new GalleryAdapter(Glide.with(this), mViewModel);
        rv.setAdapter(galleryAdapter);

        MaterialButton btn = requireView().findViewById(R.id.gallery_placeholder_reloader);
        btn.setOnClickListener(v -> mViewModel.loadPhotos());

        View placeholder = requireView().findViewById(R.id.gallery_placeholder);
        placeholder.setVisibility(View.VISIBLE);

        View loadingBar = requireActivity().findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);

        mViewModel.getPhotos().observe(getViewLifecycleOwner(), photos -> {
            if (photos.size() != 0) {
                galleryAdapter.setItems(photos);
                placeholder.setVisibility(View.INVISIBLE);
            } else {
                // Не удалось загрузить, пробуем еще раз
                if (!isReloaded) {
                    isReloaded = true;
                    mViewModel.loadPhotos();
                } else {
                    placeholder.setVisibility(View.VISIBLE);
                }
            }
            loadingBar.setVisibility(View.INVISIBLE);
        });

        // Пользователь выбрал фото, открываем его в диалоге на весь экран
        mViewModel.getUrl().observe(getViewLifecycleOwner(), url -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            DialogFragment photoFragment = PhotoFragment.newInstance(url);
            photoFragment.show(ft, "dialog");
        });
    }

}
