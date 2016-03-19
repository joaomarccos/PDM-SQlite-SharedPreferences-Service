package joaomarccos.github.io.appwebservice.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joaomarcos on 26/02/16.
 */
public class DataStorer {

    private SQLiteDatabase db;
    private SharedPreferences preferences;
    private DBHelper dbHelper;

    public DataStorer(Context context) {
        this.dbHelper = new DBHelper(context);
        this.preferences = context.getSharedPreferences("ho", Context.MODE_PRIVATE);
    }

    public void appendNewName(String value) {
        this.db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", value);
        this.db.insert(DBHelper.TABLE_NAME, null, values);
    }

    public List<String> getAllNames() {
        this.db = this.dbHelper.getReadableDatabase();
        Cursor cursor = this.db.rawQuery("SELECT * FROM " + DBHelper.TABLE_NAME, null);
        cursor.moveToFirst();
        List<String> list = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NAME)));
            cursor.moveToNext();
        }
        return list;
    }

    public void reset() {
        this.db = this.dbHelper.getWritableDatabase();
        SharedPreferences.Editor edit = this.preferences.edit();
        edit.putInt("offset", 0);
        edit.putString("hash", "vazio");
        edit.commit();
        this.db.execSQL("DELETE FROM "+DBHelper.TABLE_NAME);
    }

    public int getCurrentLine() {
        return this.preferences.getInt("offset", 0);
    }

    public void incrementCurrentLine() {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt("offset", this.preferences.getInt("offset", 0) + 1);
        editor.commit();
    }

    public void updateHash(String hash) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString("hash", hash);
        editor.commit();
    }

    public String getLocalHash() {
        return preferences.getString("hash", "empty");
    }

    private class DBHelper extends SQLiteOpenHelper {

        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "names.db";
        public static final String TABLE_NAME = "NAMES";
        public static final String COLUMN_NAME = "name";


        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_NAME + " VARCHAR[50])");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS NAMES");
            onCreate(db);
        }
    }
}
