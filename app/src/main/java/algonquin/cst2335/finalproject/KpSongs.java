package algonquin.cst2335.finalproject;

public class KpSongs {
    private int id;
    private String title;
    private String duration;
    private String album;

    public KpSongs() {
        // Empty constructor
    }

    public KpSongs(int id, String title, String duration, String album) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
