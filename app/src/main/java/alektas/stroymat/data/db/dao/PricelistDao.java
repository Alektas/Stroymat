package alektas.stroymat.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import alektas.stroymat.data.db.entities.PricelistItem;

@Dao
public abstract class PricelistDao {
    private static final String TAG = "PricelistDao";

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items ORDER BY categ, name")
    public abstract List<PricelistItem> getItems();

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE items.categ = :category ORDER BY name ASC")
    public abstract List<PricelistItem> getItems(int category);

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE items.article = :article")
    public abstract PricelistItem getItem(int article);

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE name LIKE :query")
    public abstract List<PricelistItem> search(String query);

    @Query("SELECT categ_name FROM categories WHERE categ = :category")
    public abstract String getCategoryName(int category);

}