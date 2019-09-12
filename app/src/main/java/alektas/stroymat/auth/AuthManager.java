package alektas.stroymat.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import alektas.stroymat.R;

public class AuthManager {
    private Context mContext;
    private SharedPreferences mPrefs;

    public AuthManager(Context context) {
        mContext = context;
        mPrefs = context.getSharedPreferences(
                context.getString(R.string.PREFS_LOGIN_KEY), Context.MODE_PRIVATE);
    }

    public void save(String surname, String name, String phone) {
        mPrefs.edit().putString(
                mContext.getString(R.string.LOGIN_SURNAME_KEY),
                surname)
                .apply();

        mPrefs.edit().putString(
                mContext.getString(R.string.LOGIN_NAME_KEY),
                name)
                .apply();

        mPrefs.edit().putString(
                mContext.getString(R.string.LOGIN_PHONE_KEY),
                phone)
                .apply();
    }

    public void reset() {
        mPrefs.edit().clear().apply();
    }

    public boolean isLogin() {
        return !TextUtils.isEmpty(getSurname()) &&
                !TextUtils.isEmpty(getName()) &&
                !TextUtils.isEmpty(getPhone());
    }

    public String getSurname() {
        return mPrefs.getString(mContext.getString(R.string.LOGIN_SURNAME_KEY), "");
    }

    public String getName() {
        return mPrefs.getString(mContext.getString(R.string.LOGIN_NAME_KEY), "");
    }

    public String getPhone() {
        return mPrefs.getString(mContext.getString(R.string.LOGIN_PHONE_KEY), "");
    }
}
