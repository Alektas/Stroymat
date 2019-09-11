package alektas.stroymat.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import alektas.stroymat.R;

public class StringUtils {

    public static void makeLinkable(TextView tv, String link) {
        SpannableString ss = new SpannableString(tv.getText());
        ss.setSpan(new URLSpan(link), 0, tv.getText().length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ss);
    }

    /**
     * Получить реальное целое вещественное число из EditText'а
     * @param editText
     * @return положительное целое число или 1 если текст не парсится
     */
    public static float getQuantity(EditText editText) {
        String text = editText.getText().toString();
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public static String formatQuantity(float q) {
        if (q % 1 == 0) return String.format(Locale.US,"%.0f", q);
        return String.format(Locale.US,"%.1f", q);
    }

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

    public static String formatPrice(float price, String currency, String unit) {
        return String.format(Locale.US,"%.0f %s%s", price, currency, unit);
    }

    public static String formatOrderPrice(float f) {
        return String.format(Locale.US,"%.2f", f);
    }

    public static String formatSquare(float f) {
        return String.format(Locale.US,"%.2f", f);
    }

    public static boolean isSquareUnit(String u) {
        return (u.equals("м2") || u.equals(ResourcesUtils.getString(R.string.square_units)));
    }
}
