package alektas.stroymat.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alektas.stroymat.App;
import alektas.stroymat.R;
import alektas.stroymat.data.db.AppDatabase;
import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.PricelistItem;

public class ItemsRepository implements Repository {
    private static final String TAG = "ItemsRepository";
    private static Repository INSTANCE;
    private PricelistDao mItemsDao;
    private MutableLiveData<List<PricelistItem>> mItemsData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mItemsLoadedData = new MutableLiveData<>();
    private int mCurrentCategory;

    interface ItemsLoadingListener {
        void onLoad(boolean isLoaded);
    }

    private ItemsRepository(Context context) {
        mItemsDao = AppDatabase.getInstance(context).getDao();
        mItemsData.setValue(getItems(mCurrentCategory));
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

    private List<PricelistItem> getItems(int category) {
        try {
            return new getItemsAsync(mItemsDao, (isLoaded) -> mItemsLoadedData.setValue(isLoaded))
                    .execute(category).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
//            for (int i = 0; i < Math.pow(12, 7); i++) {
//                Math.pow(i, i);
//            }
            return integers[0] == 0 ? mDao.getItems() : mDao.getItems(integers[0]);
        }

        @Override
        protected void onPostExecute(List<PricelistItem> pricelistItems) {
            super.onPostExecute(pricelistItems);
            mListener.onLoad(true);
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
