package alektas.stroymat.data.db.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "photos",
        indices = {
                @Index(value = "_id", unique = true)})
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @NonNull
    @ColumnInfo(name = "url")
    private String mUrl;
    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @Ignore
    public Photo(@NonNull String url, @NonNull String name) {
        mUrl = url;
        mName = name;
    }

    public Photo(int _id, @NonNull String url, @NonNull String name) {
        this._id = _id;
        mUrl = url;
        mName = name;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    @NonNull
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(@NonNull String url) {
        mUrl = url;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    public void setName(@NonNull String name) {
        mName = name;
    }
}
