package alektas.stroymat.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alektas.stroymat.App;
import alektas.stroymat.R;
import alektas.stroymat.data.db.AppDatabase;
import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.ProfnastilItem;
import alektas.stroymat.data.db.entities.SizedItem;
import alektas.stroymat.ui.gallery.Photo;

public class ItemsRepository implements Repository {
    private static final String TAG = "ItemsRepository";
    private static Repository INSTANCE;
    private PricelistDao mItemsDao;
    private MutableLiveData<List<PricelistItem>> mItemsData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mItemsLoadedData = new MutableLiveData<>();
    private MutableLiveData<List<Photo>> mGalleryPhotos = new MutableLiveData<>();
    private int mCurrentCategory;

    interface ItemsLoadingListener {
        void onLoad(boolean isLoaded);
    }

    private ItemsRepository(Context context) {
        mItemsDao = AppDatabase.getInstance(context).getDao();
        FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();
        updatePricelist(firebaseDb);
        loadGallery(firebaseDb);
    }

    private void updatePricelist(FirebaseFirestore db) {
        db.collection("pricelist")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                int article = Integer.parseInt(document.getId());
                                String url = (String) document.getData().get("url");
                                new setItemImageAsync(mItemsDao, article, url).execute();
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "updatePricelist: document name should be an item article!", e);
                                break;
                            }
                        }
                        mItemsData.setValue(getItems(mCurrentCategory));
                    }
                })
                .addOnFailureListener(e -> {
                    mItemsData.setValue(getItems(mCurrentCategory));
                });
    }

    private void loadGallery(FirebaseFirestore db) {
        db.collection("gallery")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Photo> photos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uri = (String) document.getData().get("url");
                            photos.add(new Photo(uri, document.getId()));
                        }
                        mGalleryPhotos.setValue(photos);
                    }
                });
    }

    private static class setItemImageAsync extends AsyncTask<Void, Void, Void> {
        private PricelistDao mDao;
        private int mArticle;
        private String mUrl;

        public setItemImageAsync(PricelistDao dao, int article, String url) {
            mDao = dao;
            mArticle = article;
            mUrl = url;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.setItemImage(mArticle, mUrl);
            return null;
        }
    }

    public static Repository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ItemsRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ItemsRepository(context);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public LiveData<List<PricelistItem>> getItems() {
        return mItemsData;
    }

    @Override
    public PricelistItem getItem(int article) {
        PricelistItem item = null;
        try {
            item = new getItemAsync(mItemsDao).execute(article).get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "getItem: no item with such article: " + article, e);
            e.printStackTrace();
        }
        return item;
    }

    @Override
    public LiveData<Boolean> getItemsLoading() {
        return mItemsLoadedData;
    }

    @Override
    public void setCategory(int category) {
        mCurrentCategory = category;
        mItemsData.setValue(getItems(category));
    }

    @Override
    public String getCategoryName(int categ) {
        try {
            return new getCategoryNameAsync(mItemsDao).execute(categ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return App.getComponent().getResources().getString(R.string.other);
        }
    }

    @Override
    public void setFoundItems(String query) {
        List<PricelistItem> items;
        try {
            items = new searchItemsAsync(mItemsDao).execute(query).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
        mItemsData.setValue(items);
    }

    public List<PricelistItem> getItems(int category) {
        try {
            return new getItemsAsync(mItemsDao, (isLoaded) -> mItemsLoadedData.setValue(isLoaded))
                    .execute(category).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SizedItem> getSizedItems(int category) {
        try {
            return new getSizedItemsAsync(mItemsDao).execute(category).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ProfnastilItem> getProfnastil() {
        try {
            return new getProfnastilAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public LiveData<List<Photo>> getGalleryPhotos() {
        return mGalleryPhotos;
    }

    private static class getItemsAsync extends AsyncTask<Integer, Void, List<PricelistItem>> {
        private PricelistDao mDao;
        private ItemsLoadingListener mListener;

        getItemsAsync(PricelistDao dao, ItemsLoadingListener listener) {
            mDao = dao;
            mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mListener.onLoad(false);
        }

        @Override
        protected List<PricelistItem> doInBackground(Integer... integers) {
            return integers[0] == 0 ? mDao.getItems() : mDao.getItems(integers[0]);
        }

        @Override
        protected void onPostExecute(List<PricelistItem> pricelistItems) {
            super.onPostExecute(pricelistItems);
            mListener.onLoad(true);
        }
    }

    private static class getSizedItemsAsync extends AsyncTask<Integer, Void, List<SizedItem>> {
        private PricelistDao mDao;

        getSizedItemsAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<SizedItem> doInBackground(Integer... integers) {
            return mDao.getSizedItems(integers[0]);
        }
    }

    private static class getProfnastilAsync extends AsyncTask<Void, Void, List<ProfnastilItem>> {
        private PricelistDao mDao;

        getProfnastilAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<ProfnastilItem> doInBackground(Void... voids) {
            return mDao.getProfnastil();
        }
    }

    private static class getItemAsync extends AsyncTask<Integer, Void, PricelistItem> {
        private PricelistDao mDao;

        getItemAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected PricelistItem doInBackground(Integer... integers) {
            return mDao.getItem(integers[0]);
        }
    }

    private static class getCategoryNameAsync extends AsyncTask<Integer, Void, String> {
        private PricelistDao mDao;

        getCategoryNameAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected String doInBackground(Integer... integers) {
            return mDao.getCategoryName(integers[0]);
        }
    }

    private static class searchItemsAsync extends AsyncTask<String, Void, List<PricelistItem>> {
        private PricelistDao mDao;

        searchItemsAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<PricelistItem> doInBackground(String... query) {
            return mDao.search(query[0]);
        }
    }

}
