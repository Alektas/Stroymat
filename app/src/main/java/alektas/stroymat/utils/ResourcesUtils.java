package alektas.stroymat.utils;

import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;

import alektas.stroymat.App;
import alektas.stroymat.BuildConfig;

public class ResourcesUtils {
    private static final String TAG = "ResourcesUtils";
    private static Resources mResources = App.getComponent().getResources();

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static String getString(int stringResId) {
        return mResources.getString(stringResId);
    }

    public static String getString(@NonNull String stringResName) {
        return getString(getStringId(stringResName));
    }

    public static int getResId(String resName, String type) {
        if (resName == null) return 0;
        return mResources.getIdentifier(resName,
                type,
                App.getComponent().getContext().getPackageName());
    }

    public static int getImgId(String imgResName) {
        if (imgResName == null) return 0;
        return mResources.getIdentifier(imgResName,
                "drawable",
                App.getComponent().getContext().getPackageName());
    }

    public static int getStringId(String stringResName) {
        if (stringResName == null) return 0;
        return mResources.getIdentifier(stringResName,
                "string",
                App.getComponent().getContext().getPackageName());
    }


    public static String getResIdName(int resId) {
        try {
            return mResources.getResourceEntryName(resId);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "getResIdName: resource not found: " + resId, e);
        }
        return null;
    }

}
