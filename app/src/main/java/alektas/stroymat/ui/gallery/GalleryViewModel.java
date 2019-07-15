package alektas.stroymat.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {
    private MutableLiveData<String> mUrl = new MutableLiveData<>();

    public void onPhotoSelected(String url) {
        mUrl.setValue(url);
    }

    public LiveData<String> getUrl() {
        return mUrl;
    }
}
