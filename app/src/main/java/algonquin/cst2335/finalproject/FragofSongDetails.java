package algonquin.cst2335.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragofSongDetails extends Fragment {

    private KpSongs selectedSong;
    private DezDatabseMang database;

    public FragofSongDetails() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.song_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView durationTextView = view.findViewById(R.id.durationTextView);
        TextView albumTextView = view.findViewById(R.id.albumTextView);
        ImageView albumCoverImageView = view.findViewById(R.id.albumCoverImageView);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Check if selectedSong is not null and display its details
        if (selectedSong != null) {
            titleTextView.setText(selectedSong.getTitle());
            durationTextView.setText(selectedSong.getDuration());
            albumTextView.setText(selectedSong.getAlbum());
            // Set album cover image if available
            // albumCoverImageView.setImageResource(selectedSong.getAlbumCover()); // You need to implement this
        }

        // Implement functionality for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSong != null) {
                    // Save the song data to the database
                    saveSongToDatabase(selectedSong);
                } else {
                    Toast.makeText(requireContext(), "No song selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to set selected song
    public void setSelectedSong(KpSongs song) {
        selectedSong = song;
    }

    // Method to save song to the database
    private void saveSongToDatabase(KpSongs song) {
        if (database == null) {
            database = DezDatabseMang.getInstance(requireContext()); // Assuming you have a static getInstance method
        }

        DezrSongDAO songDAO = database.dezrSongDAO();
        songDAO.insertSong(song);

        Toast.makeText(requireContext(), "Song saved to favorites", Toast.LENGTH_SHORT).show();
    }
}