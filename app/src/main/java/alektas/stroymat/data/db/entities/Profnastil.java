package alektas.stroymat.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "profnastil",
        indices = {
                @Index(value = "item_article", unique = true)},
        foreignKeys = @ForeignKey(
                entity = PricelistItem.class,
                parentColumns = "article",
                childColumns = "item_article"))
public class Profnastil {
    @PrimaryKey
    @ColumnInfo(name = "item_article")
    private int itemArticle;
    private float overlap;
    private float length;
    private float width;

    public Profnastil(int itemArticle, float overlap, float length, float width) {
        this.itemArticle = itemArticle;
        this.overlap = overlap;
        this.length = length;
        this.width = width;
    }

    public int getItemArticle() {
        return itemArticle;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public float getOverlap() {
        return overlap;
    }
}
