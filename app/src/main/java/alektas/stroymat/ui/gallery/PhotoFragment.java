package alektas.stroymat.ui.gallery;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import alektas.stroymat.R;

public class PhotoFragment extends DialogFragment {
    private String mUrl;

    static PhotoFragment newInstance(String url) {
        PhotoFragment f = new PhotoFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUrl = getArguments().getString("url");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.photo_fragment, container, false);
        PhotoView photo = v.findViewById(R.id.photo);
        photo.setOnOutsidePhotoTapListener(imageView -> dismiss());
        Glide.with(requireContext())
                .load(mUrl)
                .thumbnail(0.1f)
                .placeholder(R.drawable.img_placeholder)
                .into(photo);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
