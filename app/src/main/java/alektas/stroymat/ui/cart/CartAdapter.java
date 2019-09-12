package alektas.stroymat.ui.cart;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import alektas.stroymat.R;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.utils.AppUtils;
import alektas.stroymat.utils.StringUtils;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemHolder> {
    private static final String TAG = "CartAdapter";
    private CartViewModel mViewModel;
    private List<CartItem> mItems;

    public CartAdapter(CartViewModel model) {
        mViewModel = model;
        setHasStableIds(true);
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView quantity;
        TextView units;
        TextView price;
        EditText quantityEdit;
        ImageButton quantityApplyBtn;
        ImageButton removeBtn;

        ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            units = itemView.findViewById(R.id.item_quantity_units);
            price = itemView.findViewById(R.id.item_price);
            quantityEdit = itemView.findViewById(R.id.item_quantity_input);
            quantityApplyBtn = itemView.findViewById(R.id.item_apply_btn);
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
        int inputType = EditorInfo.TYPE_CLASS_NUMBER;
        inputType |= StringUtils.isSquareUnit(item.getUnit()) ?
                EditorInfo.TYPE_NUMBER_FLAG_DECIMAL : 0;
        holder.quantityEdit.setInputType(inputType);
        holder.quantityEdit.setText(StringUtils.formatQuantity(item.getQuantity()));
        holder.quantityEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                holder.quantityApplyBtn.performClick();
                return true;
            }
            return false;
        });
        holder.quantityApplyBtn.setOnClickListener(v -> {
            try {
                float q = StringUtils.getQuantity(holder.quantityEdit);
                if (q != 0) {
                    item.setQuantity(q);
                    AppUtils.hideKeyboard(holder.itemView);
                    mViewModel.changeQuantity(item);
                    return;
                }
            } catch (NumberFormatException e) {
                Log.e(TAG, "onTextChanged: quantity of item must be an integer", e);
            }
            Toast.makeText(holder.itemView.getContext(),
                    "Укажите корректное количество товара (больше 0)",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mItems == null ? position : getItems().get(position).getArticle();
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
