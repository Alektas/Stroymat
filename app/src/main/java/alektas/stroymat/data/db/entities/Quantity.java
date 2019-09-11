package alektas.stroymat.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "quantities",
        indices = {
                @Index(value = "item_article", unique = true)},
        foreignKeys = @ForeignKey(
                entity = PricelistItem.class,
                parentColumns = "article",
                childColumns = "item_article"))
public class Quantity {
    @PrimaryKey
    @ColumnInfo(name = "item_article")
    private int itemArticle;
    @ColumnInfo(name = "cart_quantity")
    private float cartQuantity = 1;

    public Quantity(int itemArticle, float cartQuantity) {
        this.itemArticle = itemArticle;
        this.cartQuantity = cartQuantity;
    }

    public int getItemArticle() {
        return itemArticle;
    }

    public void setItemArticle(int itemArticle) {
        this.itemArticle = itemArticle;
    }

    public float getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(float cartQuantity) {
        this.cartQuantity = cartQuantity;
    }
}
