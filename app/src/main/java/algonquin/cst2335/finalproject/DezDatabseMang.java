package algonquin.cst2335.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DezDatabseMang extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DeezerSongsDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Songs";
    private static final String COL_ID = "ID";
    private static final String COL_TITLE = "Title";
    private static final String COL_DURATION = "Duration";
    private static final String COL_ALBUM = "Album";

    public DezDatabseMang(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_TITLE + " TEXT," +
                COL_DURATION + " TEXT," +
                COL_ALBUM + " TEXT" + ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addSong(KpSongs song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, song.getTitle());
        values.put(COL_DURATION, song.getDuration());
        values.put(COL_ALBUM, song.getAlbum());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteSong(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<KpSongs> getAllSongs() {
        List<KpSongs> songList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID)); // Retrieve ID from database
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String duration = cursor.getString(cursor.getColumnIndex(COL_DURATION));
                String album = cursor.getString(cursor.getColumnIndex(COL_ALBUM));

                KpSongs song = new KpSongs(id, title, duration, album); // Create KpSongs object with ID
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }
}
