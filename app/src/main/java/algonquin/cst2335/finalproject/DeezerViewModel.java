package algonquin.cst2335.finalproject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class DeezerViewModel extends ViewModel {

    private MutableLiveData<List<KpSongs>> songListLiveData = new MutableLiveData<>();
    private DeezerSongApi deezerSongApi; // Assuming a repository class for Deezer API interaction

    public DeezerViewModel() {
        deezerSongApi = new DeezerSongApi(); // Initialize repository or inject through constructor
    }

    public LiveData<List<KpSongs>> getSongListLiveData() {
        return songListLiveData;
    }

    public void searchSongsByArtist(String artistName) {
        // Call method in repository to fetch songs by artist from Deezer API
        deezerSongApi.searchSongsByArtist(artistName, new DeezerSongApi.DezApiResponseListener() {
            @Override
            public void onSuccess(List<KpSongs> songs) {
                songListLiveData.setValue(songs); // Update LiveData with fetched songs
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle failure/error scenario
            }
        });
    }
}
