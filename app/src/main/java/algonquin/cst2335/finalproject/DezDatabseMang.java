package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

@Database(entities = {KpSongs.class}, version = 1)
public abstract class DezDatabseMang extends RoomDatabase {

    public abstract DezrSongDAO dezrSongDAO();

    private static volatile DezDatabseMang INSTANCE;

    public static DezDatabseMang getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (DezDatabseMang.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    DezDatabseMang.class, "song_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
