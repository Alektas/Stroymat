package alektas.stroymat.utils;

import java.util.ArrayList;
import java.util.List;

import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.ProfnastilItem;
import alektas.stroymat.data.db.entities.SizedItem;

public class ItemUtils {
    public static List<String> getNames(List<SizedItem> items) {
        List<String> names = new ArrayList<>();
        for (SizedItem item : items) {
            names.add(item.getName());
        }
        return names;
    }

    public static List<String> getPricelistNames(List<PricelistItem> items) {
        List<String> names = new ArrayList<>();
        for (PricelistItem item : items) {
            names.add(item.getName());
        }
        return names;
    }

    public static List<String> getProfnastilNames(List<ProfnastilItem> items) {
        List<String> names = new ArrayList<>();
        for (ProfnastilItem item : items) {
            names.add(item.getName());
        }
        return names;
    }
}
