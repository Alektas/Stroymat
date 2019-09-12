package alektas.stroymat.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alektas.stroymat.App;
import alektas.stroymat.R;
import alektas.stroymat.data.db.AppDatabase;
import alektas.stroymat.data.db.FirestoreLoader;
import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.CartItem;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.db.entities.Quantity;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.model.ProfnastilItem;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.model.SizedItem;
import alektas.stroymat.data.db.entities.StoveBrick;
import alektas.stroymat.data.db.entities.Photo;
import alektas.stroymat.utils.ResourcesUtils;

public class ItemsRepository implements Repository {
    private static final String TAG = "ItemsRepository";
    private static final String CATEGORIES_VERSION_KEY = "CATEGORIES_VERSION_KEY";
    private static final String PRICELIST_VERSION_KEY = "PRICELIST_VERSION_KEY";
    private static final String GALLERY_VERSION_KEY = "GALLERY_VERSION_KEY";
    private static Repository INSTANCE;
    private final FirestoreLoader loader;
    private PricelistDao mItemsDao;
    private MutableLiveData<List<PricelistItem>> mItemsData = new MutableLiveData<>();
    private LiveData<List<CartItem>> mCartItems;
    private LiveData<List<Category>> mCategories;
    private LiveData<List<Photo>> mGalleryPhotos;
    private LiveData<List<PricelistItem>> mBricks;
    private LiveData<List<ProfnastilItem>> mProfnastil;
    private LiveData<List<SizedItem>> mSizedItems;
    private LiveData<List<SizedItem>> mSiding;
    private LiveData<List<SizedItem>> mBordury;
    private LiveData<List<SizedItem>> mPlity;

    private ItemsRepository(Context context) {
        mItemsDao = AppDatabase.getInstance(context).getDao();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseFirestore firebaseDb = FirebaseFirestore.getInstance();
        loader = new FirestoreLoader(firebaseDb, this);
        SharedPreferences prefs = context.getSharedPreferences(
                        ResourcesUtils.getString(R.string.PREFS_NAME),
                        Context.MODE_PRIVATE);
        lookForUpdate(prefs, remoteConfig, loader);

        mCategories = mItemsDao.getCategories();
        mItemsData.setValue(getItems(0));
        mGalleryPhotos = mItemsDao.getGalleryPhotos();
        mSizedItems = mItemsDao.getSizes();
        mProfnastil = mItemsDao.getProfnastil();
        mSiding = mItemsDao.getSiding();
        mBricks = mItemsDao.getStoveBricks();
        mPlity = mItemsDao.getPlity();
        mBordury = mItemsDao.getBordury();
        mCartItems = mItemsDao.getCartItems();
    }

    private void lookForUpdate(SharedPreferences prefs,
                               FirebaseRemoteConfig remoteConfig,
                               FirestoreLoader loader) {
        long pricelistVersion = prefs.getLong(PRICELIST_VERSION_KEY, 1);
        long categoriesVersion = prefs.getLong(CATEGORIES_VERSION_KEY, 1);
        long galleryVersion = prefs.getLong(GALLERY_VERSION_KEY, 0); // 0 потому что галерею нужно загрузить
        long pricelistActualVersion = remoteConfig.getLong(PRICELIST_VERSION_KEY);
        long categActualVersion = remoteConfig.getLong(CATEGORIES_VERSION_KEY);
        long galleryActualVersion = remoteConfig.getLong(GALLERY_VERSION_KEY);
        if (pricelistActualVersion > pricelistVersion) {
            loader.loadPricelist();
            prefs.edit().putLong(PRICELIST_VERSION_KEY, pricelistActualVersion).apply();
        }
        if (categActualVersion > categoriesVersion) {
            loader.loadCategories();
            prefs.edit().putLong(CATEGORIES_VERSION_KEY, categActualVersion).apply();
        }
        if (galleryActualVersion > galleryVersion) {
            loader.loadGallery();
            prefs.edit().putLong(GALLERY_VERSION_KEY, galleryActualVersion).apply();
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
    public void loadGallery() {
        loader.loadGallery();
    }

    @Override
    public LiveData<List<PricelistItem>> getItems() {
        return mItemsData;
    }

    @Override
    public LiveData<List<CartItem>> getCartItems() {
        return mCartItems;
    }

    @Override
    public LiveData<List<Category>> getCategories() {
        return mCategories;
    }

    @Override
    public LiveData<List<Photo>> getGalleryPhotos() {
        return mGalleryPhotos;
    }

    @Override
    public LiveData<List<SizedItem>> getSizedItems() {
        return mSizedItems;
    }

    @Override
    public LiveData<List<ProfnastilItem>> getProfnastil() {
        return mProfnastil;
    }

    @Override
    public LiveData<List<SizedItem>> getSiding() {
        return mSiding;
    }

    @Override
    public LiveData<List<PricelistItem>> getStoveBricks() {
        return mBricks;
    }

    @Override
    public LiveData<List<SizedItem>> getPlity() {
        return mPlity;
    }

    @Override
    public LiveData<List<SizedItem>> getBordury() {
        return mBordury;
    }

    @Override
    public List<PricelistItem> getItems(int category) {
        try {
            return new getItemsAsync(mItemsDao).execute(category).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Size> getSizesList() {
        try {
            return new getSizesAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Profnastil> getProfnastilList() {
        try {
            return new getProfnastilAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Siding> getSidingList() {
        try {
            return new getSidingAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<StoveBrick> getStoveBricksList() {
        try {
            return new getStoveBricksAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Plita> getPlityList() {
        try {
            return new getPlityAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Bordur> getBorduryList() {
        try {
            return new getBorduryAsync(mItemsDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Category getCategory(int categ) {
        try {
            return new getCategoryAsync(mItemsDao).execute(categ).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            String name = App.getComponent().getResources().getString(R.string.other);
            return new Category(0, name, null);
        }
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
    public void setCategory(int category) {
        mItemsData.setValue(getItems(category));
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

    @Override
    public void setCategories(List<Category> categories) {
        new setCategoriesAsync(mItemsDao).execute(categories);
    }

    @Override
    public void setGalleryPhotos(List<Photo> photos) {
        new setGalleryPhotosAsync(mItemsDao).execute(photos);
    }

    @Override
    public void setPricelist(List<PricelistItem> pricelist) {
        new setPricelistAsync(mItemsDao).execute(pricelist);
    }

    @Override
    public void setSizes(List<Size> sizes) {
        new setSizesAsync(mItemsDao).execute(sizes);
    }

    @Override
    public void setPlity(List<Plita> plity) {
        new setPlityAsync(mItemsDao).execute(plity);
    }

    @Override
    public void setBordury(List<Bordur> bordury) {
        new setBorduryAsync(mItemsDao).execute(bordury);
    }

    @Override
    public void setSiding(List<Siding> sidings) {
        new setSidingAsync(mItemsDao).execute(sidings);
    }

    @Override
    public void setProfnastil(List<Profnastil> profnastil) {
        new setProfnastilAsync(mItemsDao).execute(profnastil);
    }

    @Override
    public void setStoveBricks(List<StoveBrick> bricks) {
        new setStoveBricksAsync(mItemsDao).execute(bricks);
    }

    @Override
    public void addCartItem(CartItem item) {
        Quantity q = new Quantity(item.getArticle(), item.getQuantity());
        new addCartItemAsync(mItemsDao).execute(q);
    }

    @Override
    public void removeCartItem(CartItem item) {
        Quantity q = new Quantity(item.getArticle(), item.getQuantity());
        new removeCartItemAsync(mItemsDao).execute(q);
    }

    @Override
    public void clearCart() {
        new clearCartAsync(mItemsDao).execute();
    }

    @Override
    public int getCartQuantity(int article) {
        try {
            return new getCartQuantityAsync(mItemsDao).execute(article).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static class clearCartAsync extends AsyncTask<Void, Void, Void> {
        private PricelistDao mDao;

        clearCartAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDao.clearCart();
            return null;
        }
    }

    private static class getCartQuantityAsync extends AsyncTask<Integer, Void, Integer> {
        private PricelistDao mDao;

        getCartQuantityAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return mDao.getCartQuantity(integers[0]);
        }
    }

    private static class addCartItemAsync extends AsyncTask<Quantity, Void, Void> {
        private PricelistDao mDao;

        addCartItemAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Quantity... quantities) {
            mDao.add(quantities[0]);
            return null;
        }
    }

    private static class removeCartItemAsync extends AsyncTask<Quantity, Void, Void> {
        private PricelistDao mDao;

        removeCartItemAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(Quantity... quantities) {
            mDao.remove(quantities[0]);
            return null;
        }
    }

    private static class setCategoriesAsync extends AsyncTask<List<Category>, Void, Void> {
        private PricelistDao mDao;

        setCategoriesAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Category>... categories) {
            mDao.setCategories(categories[0]);
            return null;
        }
    }

    private static class setGalleryPhotosAsync extends AsyncTask<List<Photo>, Void, Void> {
        private PricelistDao mDao;

        setGalleryPhotosAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Photo>... photos) {
            mDao.setGallery(photos[0]);
            return null;
        }
    }

    private static class setPricelistAsync extends AsyncTask<List<PricelistItem>, Void, Void> {
        private PricelistDao mDao;

        setPricelistAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<PricelistItem>... items) {
            mDao.setPricelist(items[0]);
            return null;
        }
    }

    private static class setSidingAsync extends AsyncTask<List<Siding>, Void, Void> {
        private PricelistDao mDao;

        setSidingAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Siding>... items) {
            mDao.setSiding(items[0]);
            return null;
        }
    }

    private static class setProfnastilAsync extends AsyncTask<List<Profnastil>, Void, Void> {
        private PricelistDao mDao;

        setProfnastilAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Profnastil>... items) {
            mDao.setProfnastil(items[0]);
            return null;
        }
    }

    private static class setSizesAsync extends AsyncTask<List<Size>, Void, Void> {
        private PricelistDao mDao;

        setSizesAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Size>... items) {
            mDao.setSizes(items[0]);
            return null;
        }
    }

    private static class setStoveBricksAsync extends AsyncTask<List<StoveBrick>, Void, Void> {
        private PricelistDao mDao;

        setStoveBricksAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<StoveBrick>... items) {
            mDao.setStoveBricks(items[0]);
            return null;
        }
    }

    private static class setPlityAsync extends AsyncTask<List<Plita>, Void, Void> {
        private PricelistDao mDao;

        setPlityAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Plita>... items) {
            mDao.setPlity(items[0]);
            return null;
        }
    }

    private static class setBorduryAsync extends AsyncTask<List<Bordur>, Void, Void> {
        private PricelistDao mDao;

        setBorduryAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Void doInBackground(List<Bordur>... items) {
            mDao.setBordury(items[0]);
            return null;
        }
    }

    private static class getItemsAsync extends AsyncTask<Integer, Void, List<PricelistItem>> {
        private PricelistDao mDao;

        getItemsAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<PricelistItem> doInBackground(Integer... integers) {
            return integers[0] == 0 ? mDao.getItems() : mDao.getItems(integers[0]);
        }
    }

    private static class getProfnastilAsync extends AsyncTask<Void, Void, List<Profnastil>> {
        private PricelistDao mDao;

        getProfnastilAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Profnastil> doInBackground(Void... voids) {
            return mDao.getProfnastilList();
        }
    }

    private static class getSidingAsync extends AsyncTask<Void, Void, List<Siding>> {
        private PricelistDao mDao;

        getSidingAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Siding> doInBackground(Void... voids) {
            return mDao.getSidingList();
        }
    }

    private static class getStoveBricksAsync extends AsyncTask<Void, Void, List<StoveBrick>> {
        private PricelistDao mDao;

        getStoveBricksAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<StoveBrick> doInBackground(Void... voids) {
            return mDao.getStoveBricksList();
        }
    }

    private static class getPlityAsync extends AsyncTask<Void, Void, List<Plita>> {
        private PricelistDao mDao;

        getPlityAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Plita> doInBackground(Void... voids) {
            return mDao.getPlityList();
        }
    }

    private static class getBorduryAsync extends AsyncTask<Void, Void, List<Bordur>> {
        private PricelistDao mDao;

        getBorduryAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Bordur> doInBackground(Void... voids) {
            return mDao.getBorduryList();
        }
    }

    private static class getSizesAsync extends AsyncTask<Void, Void, List<Size>> {
        private PricelistDao mDao;

        getSizesAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected List<Size> doInBackground(Void... voids) {
            return mDao.getSizesList();
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

    private static class getCategoryAsync extends AsyncTask<Integer, Void, Category> {
        private PricelistDao mDao;

        getCategoryAsync(PricelistDao dao) {
            mDao = dao;
        }

        @Override
        protected Category doInBackground(Integer... integers) {
            return mDao.getCategory(integers[0]);
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
