package example.android.favpicts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavPictsDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "fav_picts_db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_PICTS = "CREATE TABLE picts (" +
            "_id integer primary key autoincrement, " +
            "uri text not null, " +
            "description text not null" +
            ");";

    public FavPictsDBHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PICTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
