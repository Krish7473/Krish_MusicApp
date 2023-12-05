    package algonquin.cst2335.finalproject;

    import androidx.lifecycle.LiveData;
    import androidx.room.Dao;
    import androidx.room.Delete;
    import androidx.room.Insert;
    import androidx.room.Query;

    import java.util.List;

    @Dao
    public interface DezrSongDAO {

        @Insert
        void insertSong(KpSongs song);

        @Delete
        void deleteSong(KpSongs song);

        @Query("SELECT * FROM songs")
        LiveData<List<KpSongs>> getAllSongs();
    }
