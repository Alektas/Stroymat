package alektas.stroymat.ui.pricelist.provider;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import alektas.stroymat.R;
import alektas.stroymat.data.db.AppDatabase;
import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.utils.StringUtils;

public class ItemsProvider extends ContentProvider {
    private static final String TAG = "ItemsProvider";

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        String query = uri.getLastPathSegment();
        MatrixCursor cursor = new MatrixCursor(ItemsContract.SEARCH_COLUMNS);
        List<PricelistItem> items;
        PricelistDao dao = AppDatabase.getInstance(getContext()).getDao();

        if (SearchManager.SUGGEST_URI_PATH_QUERY.equals(query)) {
            // user hasn't entered anything
            // thus return a default cursor
            return cursor;
        } else {
            // query contains the users search
            // return a cursor with appropriate data
            try {
                items = new searchAsync(dao).execute(query).get();
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                items = new ArrayList<>();
            }

            return fillCursor(cursor, items);
        }
    }

    private Cursor fillCursor(MatrixCursor cursor, List<PricelistItem> items) {
        int i = 0;
        for (PricelistItem item : items) {
            // Cursor fields assigned in the ItemsContract.
            String price = StringUtils.formatPrice(
                    item.getPrice(),
                    getContext().getString(R.string.currency_slash), item.getUnit());

            Uri searchIconUri = loadIcon(item.getImgResName());
            cursor.addRow(new Object[] {i,                      // ID
                    item.getName(),                             // visible text
                    price,                                      // visible price
                    searchIconUri,                              // image resource ID
                    item.getArticle()});                        // data(key = article)
            i++;
        }
        return cursor;
    }

    Uri loadIcon(String url) {
        if (TextUtils.isEmpty(url)) return null;

        FutureTarget<File> futureTarget  = Glide
                .with(getContext().getApplicationContext())
                .downloadOnly()
                .load(url)
                .submit();

        File cacheFile;
        try {
            cacheFile = futureTarget.get();
            return Uri.fromFile(cacheFile);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Glide.with(getContext().getApplicationContext()).clear(futureTarget);
            return null;
        }
    }

    private static class searchAsync extends AsyncTask<String, Void, List<PricelistItem>> {
        private PricelistDao mDao;

        searchAsync(PricelistDao dao) { mDao = dao; }

        @Override
        protected List<PricelistItem> doInBackground(String... query) {
            return mDao.search("%"+query[0]+"%");
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}