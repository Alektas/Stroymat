package alektas.stroymat.ui.gallery;

import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import alektas.stroymat.R;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int GALLERY_MAX_SIZE = 20;
    private GalleryViewModel mViewModel;
    private GalleryAdapter galleryAdapter;

    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        RecyclerView rv = requireView().findViewById(R.id.gallery_grid);
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        galleryAdapter = new GalleryAdapter(mViewModel);
        rv.setAdapter(galleryAdapter);
        new loadItemsAsync(galleryAdapter).execute();
    }

    private static class loadItemsAsync extends AsyncTask<Void, Void, Void> {
        private GalleryAdapter mAdapter;

        loadItemsAsync(GalleryAdapter adapter) {
            mAdapter = adapter;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String filePrefix = "gallery_";
            String fileSuffix = ".jpg";
            StorageReference imagesFolder = FirebaseStorage.getInstance().getReference("images");
            StorageReference ref;

            for (int i = 1; i < GALLERY_MAX_SIZE; i++) {
                String imgName = filePrefix + i + fileSuffix;
                ref = imagesFolder.child(imgName);
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    Photo photo = new Photo(uri, imgName);
                    mAdapter.addPhoto(photo);
                });
            }
            return null;
        }
    }

}
