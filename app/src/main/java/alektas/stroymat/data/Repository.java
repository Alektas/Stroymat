package alektas.stroymat.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import alektas.stroymat.data.db.entities.PricelistItem;

public interface Repository {
    LiveData<List<PricelistItem>> getItems();
    PricelistItem getItem(int article);
    LiveData<Boolean> getItemsLoading();
    void setCategory(int category);
    String getCategoryName(int categ);
    void setFoundItems(String query);
}
