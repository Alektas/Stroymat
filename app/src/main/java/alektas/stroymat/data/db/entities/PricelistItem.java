package alektas.stroymat.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "items",
        indices = {
                @Index(value = {"article"}, unique = true),
                @Index(value = {"name"}),
                @Index("categ")},
        foreignKeys = @ForeignKey(
                entity = Category.class,
                parentColumns = "categ",
                childColumns = "categ"))
public class PricelistItem {
    @PrimaryKey
    private int article;
    @NonNull
    private String name;
    private float price;
    @NonNull
    private String unit;
    @ColumnInfo(name = "img_res")
    private String imgResName;
    private int categ;

    public PricelistItem(int article, @NonNull String name, float price, @NonNull String unit,
                         String imgResName, int categ) {
        this.article = article;
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.imgResName = imgResName;
        this.categ = categ;
    }

    public int getArticle() {
        return article;
    }

    public void setArticle(int article) {
        this.article = article;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @NonNull
    public String getUnit() {
        return unit;
    }

    public void setUnit(@NonNull String unit) {
        this.unit = unit;
    }

    public String getImgResName() {
        return imgResName;
    }

    public void setImgResName(String imgResName) {
        this.imgResName = imgResName;
    }

    public int getCateg() {
        return categ;
    }

    public void setCateg(int categ) {
        this.categ = categ;
    }

    @NonNull
    @Override
    public String toString() {
        return "PricelistItem{" +
                "article=" + article +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", unit='" + unit + '\'' +
                ", imgResName='" + imgResName + '\'' +
                ", categ=" + categ +
                '}';
    }
}
