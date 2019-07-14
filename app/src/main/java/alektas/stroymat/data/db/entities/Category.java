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

    public Category(int categ, @NonNull String categName) {
        this.categ = categ;
        this.categName = categName;
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
}
