package alektas.stroymat.data.model;

import androidx.room.Ignore;

public class ProfnastilItem extends SizedItem {
    private float overlap;

    @Ignore
    public ProfnastilItem() {
        super(0, "", 0f, "", "", 0, 0f, 0f);
        this.overlap = 0f;
    }

    public ProfnastilItem(int article, String name, float price, String unit, String imgResName,
                          int categ, float length, float width, float overlap) {
        super(article, name, price, unit, imgResName, categ, length, width);
        this.overlap = overlap;
    }

    public float getOverlap() {
        return overlap;
    }
}
