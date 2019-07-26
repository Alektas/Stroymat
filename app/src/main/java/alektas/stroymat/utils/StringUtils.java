package alektas.stroymat.utils;

import android.widget.EditText;

import java.util.Locale;

public class StringUtils {
    public static float getFloat(EditText editText) {
        String text = editText.getText().toString();
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    public static String formatPrice(float f) {
        return String.format(Locale.US,"%.0f", f);
    }

    public static String formatSquare(float f) {
        return String.format(Locale.US,"%.3f", f);
    }

    public static String formatPrice(float price, String currency, String unit) {
        return String.format(Locale.US,"%.0f %s%s", price, currency, unit);
    }
}
