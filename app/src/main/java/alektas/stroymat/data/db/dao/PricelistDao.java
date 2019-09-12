package alektas.stroymat.data.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Photo;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.db.entities.Quantity;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.db.entities.StoveBrick;
import alektas.stroymat.data.model.ProfnastilItem;
import alektas.stroymat.data.model.SizedItem;

@Dao
public abstract class PricelistDao {
    private static final String TAG = "PricelistDao";

    @Query("UPDATE items SET img_res = :url WHERE article = :article")
    public abstract void setItemImage(int article, String url);

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

    @Query("SELECT categ, categ_name, categ_img FROM categories WHERE categ = :category")
    public abstract Category getCategory(int category);

    @Query("SELECT cart_quantity FROM quantities WHERE item_article = :article")
    public abstract int getCartQuantity(int article);

    @Query("SELECT items.article, items.name, items.price, items.unit, items.img_res, items.categ, " +
            "sizes.length, sizes.width " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "WHERE items.categ = :category")
    public abstract List<SizedItem> getSizedItems(int category);

    @Query("SELECT item_article, overlap FROM profnastil")
    public abstract List<Profnastil> getProfnastilList();

    @Query("SELECT item_article FROM siding")
    public abstract List<Siding> getSidingList();

    @Query("SELECT item_article FROM stove_bricks")
    public abstract List<StoveBrick> getStoveBricksList();

    @Query("SELECT item_article, length, width FROM sizes")
    public abstract List<Size> getSizesList();

    @Query("SELECT item_article FROM bordury")
    public abstract List<Bordur> getBorduryList();

    @Query("SELECT item_article FROM plity")
    public abstract List<Plita> getPlityList();

    @Query("SELECT categ, categ_name FROM categories")
    public abstract LiveData<List<Category>> getCategories();

    @Query("SELECT _id, url, name FROM photos ORDER BY name")
    public abstract LiveData<List<Photo>> getGalleryPhotos();

    @Query("SELECT article, name, price, unit, img_res, categ, sizes.length, sizes.width " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "INNER JOIN siding ON items.article = siding.item_article")
    public abstract LiveData<List<SizedItem>> getSiding();

    @Query("SELECT article, name, price, unit, img_res, categ " +
            "FROM items " +
            "INNER JOIN stove_bricks ON items.article = stove_bricks.item_article")
    public abstract LiveData<List<PricelistItem>> getStoveBricks();

    @Query("SELECT article, name, price, unit, img_res, categ, sizes.length, sizes.width " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "INNER JOIN plity ON items.article = plity.item_article")
    public abstract LiveData<List<SizedItem>> getPlity();

    @Query("SELECT article, name, price, unit, img_res, categ, sizes.length, sizes.width " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "INNER JOIN bordury ON items.article = bordury.item_article")
    public abstract LiveData<List<SizedItem>> getBordury();

    @Query("SELECT article, name, price, unit, img_res, categ, sizes.length, sizes.width, " +
            "profnastil.overlap " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article " +
            "INNER JOIN profnastil ON items.article = profnastil.item_article")
    public abstract LiveData<List<ProfnastilItem>> getProfnastil();

    @Query("SELECT article, name, price, unit, img_res, categ, sizes.length, sizes.width " +
            "FROM items " +
            "INNER JOIN sizes ON items.article = sizes.item_article")
    public abstract LiveData<List<SizedItem>> getSizes();

    @Query("SELECT article, name, price, unit, img_res, categ, cart_quantity " +
            "FROM items " +
            "INNER JOIN quantities ON items.article = quantities.item_article")
    public abstract LiveData<List<CartItem>> getCartItems();

    @Transaction
    public void setCategories(List<Category> categories) {
        clearCategories();
        insertCategories(categories);
    }

    @Transaction
    public void setPricelist(List<PricelistItem> pricelist) {
        clearPricelist();
        insertPricelist(pricelist);
    }

    @Transaction
    public void setSizes(List<Size> sizes) {
        clearSizes();
        insertSizes(sizes);
    }

    @Transaction
    public void setStoveBricks(List<StoveBrick> bricks) {
        clearStoveBricks();
        insertStoveBricks(bricks);
    }

    @Transaction
    public void setProfnastil(List<Profnastil> profnastil) {
        clearProfnastil();
        insertProfnastil(profnastil);
    }

    @Transaction
    public void setSiding(List<Siding> siding) {
        clearSiding();
        insertSiding(siding);
    }

    @Transaction
    public void setPlity(List<Plita> plity) {
        clearPlity();
        insertPlity(plity);
    }

    @Transaction
    public void setBordury(List<Bordur> bordury) {
        clearBordury();
        insertBordury(bordury);
    }

    @Transaction
    public void setGallery(List<Photo> photos) {
        clearGallery();
        insertGalleryPhotos(photos);
    }

    @Query("DELETE FROM categories")
    public abstract void clearCategories();

    @Query("DELETE FROM items")
    public abstract void clearPricelist();

    @Query("DELETE FROM sizes")
    public abstract void clearSizes();

    @Query("DELETE FROM profnastil")
    public abstract void clearProfnastil();

    @Query("DELETE FROM siding")
    public abstract void clearSiding();

    @Query("DELETE FROM stove_bricks")
    public abstract void clearStoveBricks();

    @Query("DELETE FROM plity")
    public abstract void clearPlity();

    @Query("DELETE FROM bordury")
    public abstract void clearBordury();

    @Query("DELETE FROM photos")
    public abstract void clearGallery();

    @Query("DELETE FROM quantities")
    public abstract void clearCart();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertCategories(List<Category> categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPricelist(List<PricelistItem> pricelist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSizes(List<Size> sizes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertProfnastil(List<Profnastil> profnastil);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertSiding(List<Siding> siding);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertStoveBricks(List<StoveBrick> bricks);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertPlity(List<Plita> plity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertBordury(List<Bordur> bordury);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertGalleryPhotos(List<Photo> photos);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void add(Quantity quantity);

    @Delete
    public abstract void remove(Quantity quantity);

}
