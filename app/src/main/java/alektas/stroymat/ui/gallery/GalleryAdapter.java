package alektas.stroymat.ui.gallery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "GalleryAdapter";
    private List<Photo> mPhotos;
    private GalleryViewModel mModel;

    public GalleryAdapter(GalleryViewModel model) {
        mModel = model;
        mPhotos = new ArrayList<>();
        setHasStableIds(true);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        View view;
//        TextView titleText;
        ImageView image;

        private ItemHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
//            titleText = itemView.findViewById(R.id.item_title);
            image = itemView.findViewById(R.id.item_photo);
        }
    }

    @Override
    public int getItemCount() {
        if (mPhotos == null) return 0;
        return mPhotos.size();
    }

    @Override
    public long getItemId(int position) {
        return mPhotos.get(position).getUrl().hashCode();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gallery, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final Photo item = mPhotos.get(position);
        ItemHolder vh = (ItemHolder) viewHolder;

        vh.view.setOnClickListener(view -> mModel.onItemSelected(item.getName()));

        StorageReference imagesFolder = FirebaseStorage.getInstance().getReference("images");
        StorageReference imgRef = imagesFolder.child(item.getName());

        imgRef.getDownloadUrl().addOnFailureListener(e -> {
            Log.e(TAG, "onBindViewHolder: " + imgRef.toString(), e);
        });

        GlideApp.with(vh.view.getContext())
                .load(item.getUrl())
                .optionalCenterCrop()
                .optionalFitCenter()
                .thumbnail(0.1f)
                .placeholder(R.drawable.img_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(vh.image);
    }

    public List<Photo> getItems() {
        return mPhotos;
    }

    public void setItems(List<Photo> newItems) {
        mPhotos.clear();
        mPhotos.addAll(newItems);
        notifyDataSetChanged();
    }

    public void addPhoto(Photo photo) {
        mPhotos.add(photo);
        notifyDataSetChanged();
    }

}
