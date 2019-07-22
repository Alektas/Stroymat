package alektas.stroymat.ui.gallery;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import alektas.stroymat.data.ItemsRepository;
import alektas.stroymat.data.db.entities.Photo;

public class GalleryViewModel extends AndroidViewModel {
    private MutableLiveData<String> mUrl = new MutableLiveData<>();
    private LiveData<List<Photo>> mPhotos;

    public GalleryViewModel(@NonNull Application application) {
        super(application);
        mPhotos = ItemsRepository.getInstance(application).getGalleryPhotos();
    }

    public void onPhotoSelected(String url) {
        mUrl.setValue(url);
    }

    public LiveData<String> getUrl() {
        return mUrl;
    }

    public LiveData<List<Photo>> getPhotos() {
        return mPhotos;
    }
}
