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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final String DEFAULT_NAME = "Photo";
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
        View loadingBar = requireActivity().findViewById(R.id.loading_bar);
        loadingBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gallery")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Photo> photos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uri = (String) document.getData().get("url");
                            photos.add(new Photo(uri, DEFAULT_NAME));
                        }
                        galleryAdapter.setItems(photos);
                        loadingBar.setVisibility(View.GONE);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        loadingBar.setVisibility(View.GONE);
                    }
                });
    }

}
