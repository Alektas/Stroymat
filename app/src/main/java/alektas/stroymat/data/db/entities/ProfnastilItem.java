package alektas.stroymat.data.db.entities;

import androidx.room.Ignore;

public class ProfnastilItem extends SizedItem {
    private float overlap;

    @Ignore
    public ProfnastilItem() {
        super("", 0f, "", 0f, 0f);
        this.overlap = 0f;
    }

    public ProfnastilItem(String name, float price, String unit, float length, float width, float overlap) {
        super(name, price, unit, length, width);
        this.overlap = overlap;
    }

    public float getOverlap() {
        return overlap;
    }
}
