package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DezrSongDAO {

    @Insert
    void insertSong(KpSongs song);

    @Query("SELECT * FROM songs")
    List<KpSongs> getAllSongs();

    // Add more queries as needed (delete, update, etc.)
}
