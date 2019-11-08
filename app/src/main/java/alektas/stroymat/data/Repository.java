package alektas.stroymat.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.model.ProfnastilItem;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.model.SizedItem;
import alektas.stroymat.data.db.entities.StoveBrick;
import alektas.stroymat.data.db.entities.Photo;

public interface Repository {
    List<PricelistItem> getItems(int categ);
    PricelistItem getItem(int article);
    void setCategory(int category);
    Category getCategory(int categ);
    void setFoundItems(String query);
    void loadGallery();

    LiveData<List<PricelistItem>> getItems();
    LiveData<List<CartItem>> getCartItems();
    LiveData<List<Category>> getCategories();
    LiveData<List<Photo>> getGalleryPhotos();
    LiveData<List<ProfnastilItem>> getProfnastil();
    LiveData<List<PricelistItem>> getStoveBricks();
    LiveData<List<SizedItem>> getSizedItems();
    LiveData<List<SizedItem>> getSiding();
    LiveData<List<SizedItem>> getPlity();
    LiveData<List<SizedItem>> getBordury();

    List<Size> getSizesList();
    List<Profnastil> getProfnastilList();
    List<Siding> getSidingList();
    List<StoveBrick> getStoveBricksList();
    List<Plita> getPlityList();
    List<Bordur> getBorduryList();

    void setCategories(List<Category> categories);
    void setGalleryPhotos(List<Photo> photos);
    void setPricelist(List<PricelistItem> pricelist);
    void setSizes(List<Size> sizes);
    void setPlity(List<Plita> plity);
    void setBordury(List<Bordur> bordury);
    void setSiding(List<Siding> sidings);
    void setProfnastil(List<Profnastil> profnastil);
    void setStoveBricks(List<StoveBrick> bricks);

    void addCartItem(CartItem item);
    void removeCartItem(CartItem item);
    void clearCart();
    int getCartQuantity(int article);
}
