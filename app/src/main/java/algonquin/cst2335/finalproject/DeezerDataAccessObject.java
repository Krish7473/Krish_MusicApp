package algonquin.cst2335.finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeezerDataAccessObject {

    // Insert a song into the database
    @Insert
    long insertSong(SongsEntity song);

    // Retrieve all songs from the database
    @Query("SELECT * FROM SongsEntity")
    List<SongsEntity> getAllSongs();

    // Delete a song from the playlist
    @Delete
    void deleteSongFromPlayList(SongsEntity song);

    // Search for a song by title
    @Query("SELECT * FROM SongsEntity WHERE title = :title")
    List<SongsEntity> searchSong(String title);
}
