package alektas.stroymat.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories",
        indices = {
                @Index(value = "categ", unique = true)})
public class Category {
    @PrimaryKey
    @NonNull
    private int categ;
    @NonNull
    @ColumnInfo(name = "categ_name")
    private String categName;
    @ColumnInfo(name = "categ_img")
    private String categImg;

    public Category(int categ, @NonNull String categName, String categImg) {
        this.categ = categ;
        this.categName = categName;
        this.categImg = categImg;
    }

    public int getCateg() {
        return categ;
    }

    public void setCateg(int categ) {
        this.categ = categ;
    }

    @NonNull
    public String getCategName() {
        return categName;
    }

    public void setCategName(@NonNull String categName) {
        this.categName = categName;
    }

    public String getCategImg() {
        return categImg;
    }

    public void setCategImg(String categImg) {
        this.categImg = categImg;
    }
}
