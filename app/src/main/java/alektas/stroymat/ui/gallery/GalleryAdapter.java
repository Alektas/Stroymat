package alektas.stroymat.ui.gallery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.Photo;

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
        ImageView image;

        private ItemHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
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
        return mPhotos.get(position).getName().hashCode();
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
        vh.view.setOnClickListener(view -> mModel.onPhotoSelected(item.getUrl()));
        vh.image.setImageResource(R.drawable.img_placeholder);
        GlideApp.with(vh.view.getContext())
                .load(item.getUrl())
                .optionalCenterCrop()
                .optionalFitCenter()
                .thumbnail(0.1f)
                .placeholder(R.drawable.img_placeholder)
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

}
