package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

/**
 * ViewModel class responsible for managing and providing data related to songs in the Deezer application.
 *
 * This class extends the Android ViewModel class and holds MutableLiveData objects for the list of songs
 * and the selected song. It is designed to handle the communication between the UI components and the underlying
 * data source, providing a convenient way to observe and update song-related data.
 */
public class SongsViewModel extends ViewModel {

    /**
     * MutableLiveData object holding a list of songs.
     * Observers can be notified of changes to this list.
     */
    public MutableLiveData<List<SongsEntity>> songs = new MutableLiveData<>();

    /**
     * MutableLiveData object holding the currently selected song.
     * Observers can be notified when the selected song changes.
     */
    public MutableLiveData<SongsEntity> selectedSongs = new MutableLiveData<>();
}
