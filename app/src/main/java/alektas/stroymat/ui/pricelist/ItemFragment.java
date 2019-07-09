package alektas.stroymat.ui.pricelist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.utils.ResourcesUtils;
import alektas.stroymat.utils.StringUtils;

public class ItemFragment extends Fragment {
    private PricelistViewModel mViewModel;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.item_details_toolbar, menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_pricelist_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        mViewModel = ViewModelProviders.of(getActivity()).get(PricelistViewModel.class);
        mViewModel.getSelectedItem().observe(this, item -> {
            bind(getView(), item);
        });
    }

    private void bind(View itemView, PricelistItem item) {
        ImageView img = itemView.findViewById(R.id.item_img);
        int imgRes = ResourcesUtils.getImgId(item.getImgResName());
        if (imgRes != 0) {
            img.setImageResource(imgRes);
        }
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

}
