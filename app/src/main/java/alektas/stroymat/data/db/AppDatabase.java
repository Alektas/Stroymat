package alektas.stroymat.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import alektas.stroymat.data.db.dao.PricelistDao;
import alektas.stroymat.data.db.entities.Bordur;
import alektas.stroymat.data.db.entities.Category;
import alektas.stroymat.data.db.entities.Photo;
import alektas.stroymat.data.db.entities.Plita;
import alektas.stroymat.data.db.entities.PricelistItem;
import alektas.stroymat.data.db.entities.Profnastil;
import alektas.stroymat.data.db.entities.Quantity;
import alektas.stroymat.data.db.entities.Siding;
import alektas.stroymat.data.db.entities.Size;
import alektas.stroymat.data.db.entities.StoveBrick;

@Database(entities = {
        PricelistItem.class, Category.class, Photo.class,
        Size.class, Quantity.class,
        Profnastil.class, Siding.class, StoveBrick.class, Plita.class, Bordur.class},
        version = 2)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "stroymat_db";
    private static final String DATABASE_PATH = "databases/stroymat_db";

    public abstract PricelistDao getDao();

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME)
                            .createFromAsset(DATABASE_PATH)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
