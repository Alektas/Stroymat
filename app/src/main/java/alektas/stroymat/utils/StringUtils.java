package alektas.stroymat.utils;

import java.util.Locale;

public class StringUtils {
    public static String format(float f) {
        return String.format(Locale.US,"%.2f", f);
    }

    public static String formatPrice(float price, String currency, String unit) {
        if (price == 0.00f) return unit;
        return price + " " + currency + unit;
    }
}
