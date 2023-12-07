package algonquin.cst2335.finalproject;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;

/**
 * Entity class representing a song in the Deezer application.
 *
 * This class defines the structure of the SongsEntity table in the Deezer database.
 */
@Entity
public class SongsEntity {

    @PrimaryKey
    @ColumnInfo(name = "SongID")
    /**
     * The unique identifier for the song in the database.
     */
    private long id;

    /**
     * The title of the song.
     */
    @ColumnInfo(name = "title")
    private String title;

    /**
     * The duration of the song in seconds.
     */
    @ColumnInfo(name = "duration")
    private int duration; // in seconds

    /**
     * The name of the album to which the song belongs.
     */
    @ColumnInfo(name = "albumName")
    private String albumName;

    /**
     * The URL of the album cover image associated with the song.
     */
    @ColumnInfo(name = "albumCover")
    private String albumCover;

    /**
     * The name of the artist who performed the song.
     */
    @ColumnInfo(name = "artistName")
    private String artistName;

    /**
     * Constructs a new SongsEntity with the specified details.
     *
     * @param id         The unique identifier for the song.
     * @param title      The title of the song.
     * @param duration   The duration of the song in seconds.
     * @param albumName  The name of the album to which the song belongs.
     * @param albumCover The URL of the album cover image associated with the song.
     * @param artistName The name of the artist who performed the song.
     */
    public SongsEntity(long id, String title, int duration, String albumName, String albumCover, String artistName) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.artistName = artistName;
    }

    /**
     * Gets the unique identifier for the song.
     *
     * @return The unique identifier for the song.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the song.
     *
     * @param id The unique identifier to set for the song.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the title of the song.
     *
     * @return The title of the song.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the song.
     *
     * @param title The title to set for the song.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the duration of the song in seconds.
     *
     * @return The duration of the song in seconds.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the song in seconds.
     *
     * @param duration The duration to set for the song in seconds.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Gets the name of the album to which the song belongs.
     *
     * @return The name of the album.
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Sets the name of the album to which the song belongs.
     *
     * @param albumName The name of the album to set.
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * Gets the URL of the album cover image associated with the song.
     *
     * @return The URL of the album cover image.
     */
    public String getAlbumCover() {
        return albumCover;
    }

    /**
     * Sets the URL of the album cover image associated with the song.
     *
     * @param albumCover The URL of the album cover image to set.
     */
    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    /**
     * Gets the name of the artist who performed the song.
     *
     * @return The name of the artist.
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Sets the name of the artist who performed the song.
     *
     * @param artistName The name of the artist to set.
     */
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    /**
     * Gets the duration of the song in MM:SS format.
     *
     * @return The duration of the song in MM:SS format.
     */
    public String getDurationInMMSS() {
        int minute = duration / 60;
        int second = duration % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minute, second);
    }
}
