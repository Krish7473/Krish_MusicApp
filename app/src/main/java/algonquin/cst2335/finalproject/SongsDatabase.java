package algonquin.cst2335.finalproject;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for managing the Room database that stores information about Deezer songs.
 *
 * This class is responsible for creating and managing the Room database for storing songs
 * information using the DeezerDataAccessObject (DAO). It extends RoomDatabase, providing
 * an abstraction layer over SQLite to interact with the underlying database.
 */
@Database(entities = {SongsEntity.class}, version = 1)
public abstract class SongsDatabase extends RoomDatabase {

    /**
     * Abstract method to retrieve the DeezerDataAccessObject (DAO) for database operations.
     *
     * @return An instance of the DeezerDataAccessObject interface.
     */
    public abstract DeezerDataAccessObject deezerDao();
}
