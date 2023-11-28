package algonquin.cst2335.finalproject;

public class KpSongs {
    private String title;
    private String duration;
    private String album;

    // Constructors
    public KpSongs() {
        // Default constructor
    }

    public KpSongs(String title, String duration, String album) {
        this.title = title;
        this.duration = duration;
        this.album = album;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}