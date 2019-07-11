package alektas.stroymat.ui.gallery;

import android.net.Uri;

public class Photo {
    private Uri mUrl;
    private String mName;

    public Photo(String name) {
        mName = name;
    }

    public Photo(Uri url, String name) {
        mUrl = url;
        mName = name;
    }

    public Uri getUrl() {
        return mUrl;
    }

    public void setUrl(Uri url) {
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
