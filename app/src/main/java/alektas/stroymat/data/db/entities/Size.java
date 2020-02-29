package alektas.stroymat.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sizes",
        indices = {
                @Index(value = "item_article", unique = true)},
        foreignKeys = @ForeignKey(
                entity = PricelistItem.class,
                parentColumns = "article",
                childColumns = "item_article",
                onDelete = ForeignKey.CASCADE))
public class Size {
    @PrimaryKey
    @ColumnInfo(name = "item_article")
    private int itemArticle;
    @ColumnInfo(defaultValue = "0.0")
    private float length;
    @ColumnInfo(defaultValue = "0.0")
    private float width;

    public Size(int itemArticle, float length, float width) {
        this.itemArticle = itemArticle;
        this.length = length;
        this.width = width;
    }

    public int getItemArticle() {
        return itemArticle;
    }

    public void setItemArticle(int itemArticle) {
        this.itemArticle = itemArticle;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
