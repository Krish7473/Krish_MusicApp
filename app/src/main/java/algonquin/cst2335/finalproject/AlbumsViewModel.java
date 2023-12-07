package algonquin.cst2335.finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;


public class AlbumsViewModel extends ViewModel {

    // MutableLiveData to hold a list of Deezer albums
    public MutableLiveData<ArrayList<AlbumEntity>> deezerAlbum = new MutableLiveData<>();

    // MutableLiveData to hold the selected Deezer album
    public MutableLiveData<AlbumEntity> selectedAlbums = new MutableLiveData<>();
}
