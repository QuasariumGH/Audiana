package audiana;

/* Contains the Playlist object, which stores AudioTracks into user created "Playlists" that can be sorted and managed. */

public class Playlist { // Not Implamented Yet

    public AudioTrack[] files;
    public int size;
    public String name;

    public Playlist(String name, int size, AudioTrack[] files)
    {
        this.name = name;
        this.size = size;
        this.files = files;
    }
}