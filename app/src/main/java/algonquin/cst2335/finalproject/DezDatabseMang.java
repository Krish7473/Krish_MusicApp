package algonquin.cst2335.finalproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {KpSongs.class}, version = 1, exportSchema = false)
public abstract class DezDatabseMang extends RoomDatabase {

    private static DezDatabseMang instance;

    public abstract DezrSongDAO songDAO();

    public static synchronized DezDatabseMang getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DezDatabseMang.class, "deezer_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
