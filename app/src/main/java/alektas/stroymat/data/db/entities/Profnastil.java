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
                childColumns = "item_article",
                onDelete = ForeignKey.CASCADE))
public class Profnastil {
    @PrimaryKey
    @ColumnInfo(name = "item_article")
    private int itemArticle;
    @ColumnInfo(defaultValue = "0.0")
    private float overlap;

    public Profnastil(int itemArticle, float overlap) {
        this.itemArticle = itemArticle;
        this.overlap = overlap;
    }

    public int getItemArticle() {
        return itemArticle;
    }

    public float getOverlap() {
        return overlap;
    }
}
