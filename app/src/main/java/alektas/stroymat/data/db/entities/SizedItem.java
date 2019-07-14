package alektas.stroymat.data.db.entities;

public class SizedItem {
    private String name;
    private float price;
    private String unit;
    private float length;
    private float width;

    public SizedItem(String name, float price, String unit, float length, float width) {
        this.name = name;
        this.price = price;
        this.unit = unit;
        this.length = length;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
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
}
