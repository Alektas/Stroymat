package alektas.stroymat.data;

import androidx.lifecycle.LiveData;

import java.util.List;

import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.ProfnastilItem;
import alektas.stroymat.data.db.entities.SizedItem;

public interface Repository {
    LiveData<List<PricelistItem>> getItems();
    List<PricelistItem> getItems(int categ);
    PricelistItem getItem(int article);
    LiveData<Boolean> getItemsLoading();
    void setCategory(int category);
    String getCategoryName(int categ);
    void setFoundItems(String query);
    List<SizedItem> getSizedItems(int categ);
    List<ProfnastilItem> getProfnastil();
}
