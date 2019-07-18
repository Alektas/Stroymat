package alektas.stroymat.ui.pricelist;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.utils.StringUtils;

public class ItemFragment extends Fragment {
    private PricelistViewModel mViewModel;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_pricelist_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(PricelistViewModel.class);
        mViewModel.getSelectedItem().observe(this, item -> bind(requireView(), item));
    }

    private void bind(View itemView, PricelistItem item) {
        ImageView img = itemView.findViewById(R.id.item_img);
        loadImage(item, img);
        TextView name = itemView.findViewById(R.id.item_name);
        name.setText(item.getName());
        TextView article = itemView.findViewById(R.id.item_article);
        article.setText(String.valueOf(item.getArticle()));
        TextView category = itemView.findViewById(R.id.item_category);
        category.setText(mViewModel.getCategoryName(item.getCateg()));
        float price = item.getPrice();
        TextView priceText = itemView.findViewById(R.id.item_price);
        if (price == 0.00f) {
            itemView.findViewById(R.id.item_price_currency).setVisibility(View.GONE);
            itemView.findViewById(R.id.item_price_text).setVisibility(View.GONE);
            priceText.setVisibility(View.GONE);
        } else {
            priceText.setText(StringUtils.format(price));
        }
        TextView units = itemView.findViewById(R.id.item_units);
        units.setText(item.getUnit());
    }

    private void loadImage(PricelistItem item, ImageView imageView) {
        Glide.with(requireContext())
                .load(item.getImgResName())
                .thumbnail(0.1f)
                .optionalCenterCrop()
                .optionalFitCenter()
                .placeholder(R.drawable.img_placeholder)
                .into(imageView);
    }

}
