package alektas.stroymat.data.db.dao;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.ProfnastilItem;
import alektas.stroymat.data.db.entities.SizedItem;

@Dao
public abstract class PricelistDao {
    private static final String TAG = "PricelistDao";

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items ORDER BY categ, article, name")
    public abstract List<PricelistItem> getItems();

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE items.categ = :category ORDER BY article, name ASC")
    public abstract List<PricelistItem> getItems(int category);

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE items.article = :article")
    public abstract PricelistItem getItem(int article);

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items WHERE name LIKE :query OR article LIKE :query")
    public abstract List<PricelistItem> search(String query);

    @Query("SELECT categ_name FROM categories WHERE categ = :category")
    public abstract String getCategoryName(int category);

    @Query("SELECT items.name, items.price, items.unit, sizes.length, sizes.width FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "WHERE items.categ = :category")
    public abstract List<SizedItem> getSizedItems(int category);

    @Query("SELECT items.name, items.price, items.unit, profnastil.length, profnastil.width, profnastil.overlap " +
            "FROM items " +
            "INNER JOIN profnastil ON items.article = profnastil.item_article")
    public abstract List<ProfnastilItem> getProfnastil();

}
