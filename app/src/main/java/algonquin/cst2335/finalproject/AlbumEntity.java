package algonquin.cst2335.finalproject;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AlbumEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long albumId;

    @ColumnInfo(name = "albumTitle")
    private String title;

    @ColumnInfo(name = "artistName")
    private String artistName;

    @ColumnInfo(name = "coverURL")
    private String coverUrl;

    /**
     * Constructor for creating an AlbumEntity.
     *
     * @param albumId    Unique identifier for the album
     * @param title      Title of the album
     * @param artistName Name of the artist associated with the album
     * @param coverUrl   URL for the album cover image
     */
    public AlbumEntity(long albumId, String title, String artistName, String coverUrl) {
        this.albumId = albumId;
        this.title = title;
        this.artistName = artistName;
        this.coverUrl = coverUrl;
    }

    /**
     * Get the unique identifier for the album.
     *
     * @return The album ID
     */
    public long getAlbumId() {
        return albumId;
    }

    /**
     * Get the title of the album.
     *
     * @return The album title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the name of the artist associated with the album.
     *
     * @return The artist name
     */
    public String getArtistName() {
        return artistName;
    }

    /**
     * Get the URL for the album cover image.
     *
     * @return The cover image URL
     */
    public String getCoverUrl() {
        return coverUrl;
    }
}