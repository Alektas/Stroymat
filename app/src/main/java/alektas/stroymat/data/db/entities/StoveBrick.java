package alektas.stroymat.data.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "stove_bricks",
        indices = {
                @Index(value = "item_article", unique = true)},
        foreignKeys = @ForeignKey(
                entity = PricelistItem.class,
                parentColumns = "article",
                childColumns = "item_article",
                onDelete = ForeignKey.CASCADE))
public class StoveBrick {
    @PrimaryKey
    @ColumnInfo(name = "item_article")
    private int itemArticle;

    public StoveBrick(int itemArticle) {
        this.itemArticle = itemArticle;
    }

    public int getItemArticle() {
        return itemArticle;
    }

    public void setItemArticle(int itemArticle) {
        this.itemArticle = itemArticle;
    }

}
