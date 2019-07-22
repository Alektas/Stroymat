package alektas.stroymat.data.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Photo;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.db.entities.StoveBrick;

@Database(entities = {
        PricelistItem.class, Category.class, Photo.class,
        Size.class, Profnastil.class, Siding.class, StoveBrick.class, Plita.class, Bordur.class},
        version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "AppDatabase";
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "stroymat_db";

    public abstract PricelistDao getDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    setDatabaseIfNotExists(context.getApplicationContext(), DATABASE_NAME);
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Check if database not exists and copy prepopulated file from assets
    private static void setDatabaseIfNotExists(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, return
        if (dbPath.exists()) {
            return;
        }

        loadDatabase(context, databaseName, dbPath);
    }

    private static void deleteDatabase(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);

        // If the database already exists, delete it
        if (dbPath.exists()) {
            dbPath.delete();
        }
    }

    private static void loadDatabase(Context context, String databaseName, File dbPath) {
        // Make sure we have a path to the file
        dbPath.getParentFile().mkdirs();

        // Try to copy database file
        try {
            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream output = new FileOutputStream(dbPath);

            byte[] buffer = new byte[8192];
            int length;

            while ((length = inputStream.read(buffer, 0, 8192)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d(TAG, "Failed to open file", e);
            e.printStackTrace();
        }
    }

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Just handle loading database from assets, so the migration is not necessary
        }
    };

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Just handle loading database from assets, so the migration is not necessary
        }
    };

}
