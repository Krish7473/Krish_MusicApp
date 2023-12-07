package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

import algonquin.cst2335.finalproject.databinding.SongfragmentBinding;

/**
 * A fragment to display details of a song, including its name, artist, duration, and album cover.
 *
 * This fragment is responsible for rendering information about a selected song, such as its name,
 * artist, and duration. It also dynamically loads and displays the album cover image.
 */
public class SongFragment extends Fragment {

    // UI elements
    private TextView songNameTextView, artistNameTextView, durationTextView;
    private ImageView albumCoverImageView;
    private SongfragmentBinding binding;  // View binding for efficient view access

    // Data variables for song details
    private String songsName, artistsName, durationOfSong, albumCoverURL;

    // RequestQueue for making image requests using Volley library
    private RequestQueue queue;

    /**
     * Constructor for the SongFragment class.
     *
     * @param songsName      The name of the song.
     * @param artistsName    The name of the artist.
     * @param durationOfSong The duration of the song.
     * @param albumCoverURL  The URL of the album cover image.
     */
    public SongFragment(String songsName, String artistsName, String durationOfSong, String albumCoverURL) {
        this.songsName = songsName;
        this.artistsName = artistsName;
        this.durationOfSong = durationOfSong;
        this.albumCoverURL = albumCoverURL;
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * This method initializes UI elements, sets dummy data, and loads the album cover image.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views.
     * @param container          If non-null, this is the parent view that the fragment's UI
     *                           should be attached to. The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *                           saved state as given here.
     * @return The root view of the fragment's layout.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment using View Binding
        binding = SongfragmentBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(requireContext());  // Initialize Volley RequestQueue

        // Set dummy data (replace with actual data)
        binding.songNameFragment.setText(songsName);
        binding.artistNameFragment.setText(artistsName);
        binding.durationFragment.setText(durationOfSong);  // Example duration

        // Load album cover image either from local storage or make a network request to fetch it
        loadAlbumCoverImage();

        return binding.getRoot();  // Return the root view of the fragment's layout
    }

    /**
     * Helper method to format the duration in minutes and seconds.
     *
     * @param duration The duration of the song in seconds.
     * @return A formatted string representing the duration in "mm:ss" format.
     */
    private String formatDuration(int duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Helper method to load the album cover image either from local storage or via a network request.
     */
    private void loadAlbumCoverImage() {
        String pathname = getActivity().getFilesDir() + "/" + albumCoverURL;
        File file = new File(pathname);

        if (file.exists()) {
            // Load album cover image from local storage if it exists
            Bitmap albumCover = BitmapFactory.decodeFile(pathname);
            binding.imageViewFragment.setImageBitmap(albumCover);
            binding.imageViewFragment.setVisibility(View.VISIBLE);
        } else {
            // If the album cover is not in local storage, make a network request to fetch it
            ImageRequest imgReq = new ImageRequest(albumCoverURL, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    binding.imageViewFragment.setImageBitmap(bitmap);
                    binding.imageViewFragment.setVisibility(View.VISIBLE);
                    try {
                        // Save the album cover to local storage for future use
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                getActivity().openFileOutput(artistsName + ".png", getActivity().MODE_PRIVATE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                // Handle error in fetching the image
                error.printStackTrace();
            });
            queue.add(imgReq);  // Add the image request to the Volley RequestQueue
        }
    }
}