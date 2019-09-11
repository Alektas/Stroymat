package alektas.stroymat.ui.cart;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.utils.StringUtils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemHolder> {
    private static final String TAG = "CartAdapter";
    private CartViewModel mViewModel;
    private List<CartItem> mItems;

    public CartAdapter(CartViewModel model) {
        mViewModel = model;
    }

    public CartAdapter(CartViewModel model, List<CartItem> items) {
        mViewModel = model;
        mItems = items;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView units;
        TextView price;
        EditText quantityEdit;
        ImageButton removeBtn;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            units = itemView.findViewById(R.id.item_quantity_units);
            price = itemView.findViewById(R.id.item_price);
            quantityEdit = itemView.findViewById(R.id.item_quantity_input);
            removeBtn = itemView.findViewById(R.id.item_del_btn);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        CartItem item = mItems.get(position);
        holder.name.setText(item.getName());
        holder.quantity.setText(StringUtils.formatQuantity(item.getQuantity()));
        holder.units.setText(item.getUnit());
        holder.price.setText(StringUtils.formatPrice(item.getQuantity() * item.getPrice()));
        holder.removeBtn.setOnClickListener(v -> {
            mViewModel.removeItem(mItems.get(position));
        });
        holder.quantityEdit.setText(String.valueOf(item.getQuantity()));
        holder.quantityEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                try {
//                    int q = Integer.parseInt(s.toString());
//                    item.setQuantity(q);
//                    mViewModel.changeQuantity(item);
//                } catch (NumberFormatException e) {
//                    Log.e(TAG, "onTextChanged: quantity of item must be an integer", e);
//                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    public List<CartItem> getItems() {
        return mItems;
    }

    public void setItems(List<CartItem> items) {
        if (mItems == null) {
            mItems = items;
        } else {
            mItems.clear();
            mItems.addAll(items);
        }
        notifyDataSetChanged();
    }
}
