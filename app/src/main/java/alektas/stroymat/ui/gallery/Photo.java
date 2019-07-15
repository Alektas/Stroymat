package alektas.stroymat.ui.gallery;

public class Photo {
    private String mUrl;
    private String mName;

    public Photo(String name) {
        mName = name;
    }

    public Photo(String url, String name) {
        mUrl = url;
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
