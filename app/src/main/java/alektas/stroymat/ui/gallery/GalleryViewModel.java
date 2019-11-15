package alektas.stroymat.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import alektas.stroymat.App;
import alektas.stroymat.data.Repository;
import alektas.stroymat.data.db.entities.Photo;

public class GalleryViewModel extends ViewModel {
    @Inject
    public Repository mRepository;
    private MutableLiveData<String> mUrl = new MutableLiveData<>();
    private LiveData<List<Photo>> mPhotos;

    public GalleryViewModel() {
        App.getComponent().inject(this);
        mPhotos = mRepository.getGalleryPhotos();
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

    public void loadPhotos() {
        mRepository.loadGallery();
    }
}
