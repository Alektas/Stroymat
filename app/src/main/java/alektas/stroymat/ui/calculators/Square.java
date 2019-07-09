package alektas.stroymat.ui.calculators;

public class Square {
    private float width;
    private float height;
    private float square;

    public Square(float width, float height) {
        this.width = width;
        this.height = height;
        this.square = width * height;
    }

    public Square(float width, float height, boolean isTriangle) {
        this.width = width;
        this.height = height;
        this.square = isTriangle ? (width * height) / 2 : width * height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSquare() {
        return square;
    }

    public void setSquare(float square) {
        this.square = square;
    }
}
