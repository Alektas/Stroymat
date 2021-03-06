package alektas.stroymat.ui.pricelist;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.App;
import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.utils.StringUtils;

public class PricelistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "PricelistAdapter";
    private List<PricelistItem> mPricelistItems;
    private PricelistViewModel mModel;

    public PricelistAdapter(PricelistViewModel model) {
        mModel = model;
        mPricelistItems = new ArrayList<>();
        setHasStableIds(true);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {
        View view;
        TextView articleText;
        TextView nameText;
        TextView priceText;
        TextView unitText;
        ImageView image;

        private ItemHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            articleText = itemView.findViewById(R.id.item_article);
            nameText = itemView.findViewById(R.id.item_name);
            priceText = itemView.findViewById(R.id.item_price_label);
            unitText = itemView.findViewById(R.id.item_price_units);
            image = itemView.findViewById(R.id.item_img);
        }
    }

    @Override
    public int getItemCount() {
        if (mPricelistItems == null) return 0;
        return mPricelistItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mPricelistItems.get(position).getArticle();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pricelist, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        final PricelistItem item = mPricelistItems.get(position);
        ItemHolder vh = (ItemHolder) viewHolder;

        vh.view.setOnClickListener(view -> {
            mModel.onItemSelected(item.getArticle());
        });

        vh.articleText.setText(String.valueOf(item.getArticle()));
        vh.nameText.setText(item.getName());
        vh.unitText.setText(item.getUnit());
        float price = item.getPrice();
        if (price == 0.00f) {
            vh.view.findViewById(R.id.item_price_currency).setVisibility(View.GONE);
            vh.priceText.setVisibility(View.GONE);
            vh.unitText.setVisibility(View.GONE);
        } else {
            vh.view.findViewById(R.id.item_price_currency).setVisibility(View.VISIBLE);
            vh.priceText.setVisibility(View.VISIBLE);
            vh.priceText.setText(StringUtils.formatPrice(price));
        }

        vh.image.setImageResource(R.drawable.img_placeholder);
        String imgUrl;
        if (TextUtils.isEmpty(item.getImgResName()) || item.getImgResName().equals("img_placeholder")) {
            imgUrl = mModel.getCategory(item.getCateg()).getCategImg();
            if (TextUtils.isEmpty(imgUrl)) return;
        } else {
            imgUrl = item.getImgResName();
        }

        Glide.with(App.getComponent().getContext())
                .load(imgUrl)
                .thumbnail(0.1f)
                .optionalCenterCrop()
                .optionalFitCenter()
                .placeholder(R.drawable.img_placeholder)
                .into(vh.image);
    }

    public List<PricelistItem> getItems() {
        return mPricelistItems;
    }

    public void setItems(List<PricelistItem> newItems) {
        mPricelistItems.clear();
        mPricelistItems.addAll(newItems);
        notifyDataSetChanged();
    }
}
