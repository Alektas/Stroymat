package alektas.stroymat.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

public class CartItem extends PricelistItem {
    @ColumnInfo(name = "cart_quantity")
    private float quantity = 1;

    @Ignore
    public CartItem(PricelistItem item, float quantity) {
        this(item.getArticle(), item.getName(), item.getPrice(), item.getUnit(),
                item.getImgResName(), item.getCateg());
        this.quantity = quantity;
    }

    @Ignore
    public CartItem(int article, @NonNull String name, float price, @NonNull String unit,
                    String imgResName, int categ) {
        super(article, name, price, unit, imgResName, categ);
    }

    public CartItem(int article, @NonNull String name, float price, @NonNull String unit,
                    String imgResName, int categ, float quantity) {
        super(article, name, price, unit, imgResName, categ);
        this.quantity = quantity;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }
}
