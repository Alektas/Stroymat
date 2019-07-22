package alektas.stroymat.data.model;

import androidx.room.Ignore;

import alektas.stroymat.data.db.entities.PricelistItem;

public class SizedItem extends PricelistItem {
    private float length;
    private float width;

    public SizedItem(int article, String name, float price, String unit, String imgResName,
                     int categ) {
        super(article, name, price, unit, imgResName, categ);
        this.length = 0f;
        this.width = 0f;
    }

    @Ignore
    public SizedItem(int article, String name, float price, String unit, String imgResName,
                     int categ, float length, float width) {
        super(article, name, price, unit, imgResName, categ);
        this.length = length;
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public float getSquare() {
        return width * length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
