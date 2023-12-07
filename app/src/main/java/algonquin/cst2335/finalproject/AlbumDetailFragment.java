package algonquin.cst2335.finalproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.AlbumlistLayoutBinding;
import algonquin.cst2335.finalproject.databinding.SongViewHolderBinding;

/**
 * Fragment representing the details of an album, including its songs.
 */
public class AlbumDetailFragment extends Fragment {

    private AlbumlistLayoutBinding albumlistLayoutBinding;
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private List<SongsEntity> songsList;
    private List<SongsEntity> favsList;
    private RequestQueue queue;
    private MediaPlayer mediaPlayer;

    private SongsDatabase songsDatabase;
    private SongsViewModel songModel;
    private AlbumEntity album;
    private SongsEntity track;

    /**
     * Constructor for AlbumDetailFragment.
     *
     * @param songsList List of songs for the album
     * @param album     AlbumEntity representing the album
     * @param queue     RequestQueue for handling network requests
     */
    public AlbumDetailFragment(List<SongsEntity> songsList, AlbumEntity album, RequestQueue queue) {
        this.songsList = songsList;
        this.album = album;
        this.queue = queue;
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate views
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state
     * @return The inflated view for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        albumlistLayoutBinding = AlbumlistLayoutBinding.inflate(inflater, container, false);
        songModel = new ViewModelProvider(this).get(SongsViewModel.class);

        albumlistLayoutBinding.albumNameF.setText(album.getTitle());
        albumlistLayoutBinding.artistNameF.setText(album.getArtistName());

        favsList = new ArrayList<>();

        String pathname = getActivity().getFilesDir() + "/" + album.getCoverUrl();
        File file = new File(pathname);

        if (file.exists()) {
            Bitmap albumCover = BitmapFactory.decodeFile(pathname);
            albumlistLayoutBinding.albumCoverF.setImageBitmap(albumCover);
            albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
        } else {
            ImageRequest imgReq = new ImageRequest(album.getCoverUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    albumlistLayoutBinding.albumCoverF.setImageBitmap(bitmap);
                    albumlistLayoutBinding.albumCoverF.setVisibility(View.VISIBLE);
                    try {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                getActivity().openFileOutput(album.getArtistName() + ".png", getActivity().MODE_PRIVATE));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                error.printStackTrace();
            });
            queue.add(imgReq);
        }

        setHasOptionsMenu(true);
        recyclerView = albumlistLayoutBinding.albumsSongsL;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        songsAdapter = new SongsAdapter(songsList);
        recyclerView.setAdapter(songsAdapter);

        return albumlistLayoutBinding.getRoot();
    }

    /**
     * Inner class representing ViewHolder for songs in the RecyclerView.
     */
    public class SongsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView durationTextView;
        androidx.appcompat.widget.Toolbar songmenu;

        /**
         * Constructor for SongsViewHolder.
         *
         * @param itemView The view for each item in the RecyclerView
         */
        SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songName);
            artistTextView = itemView.findViewById(R.id.artistName);
            durationTextView = itemView.findViewById(R.id.duration);
            songmenu = itemView.findViewById(R.id.songTools);
        }
    }

    /**
     * Inner class representing the Adapter for the RecyclerView.
     */
    class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {

        private List<SongsEntity> songsList;

        /**
         * Constructor for SongsAdapter.
         *
         * @param songsList List of songs to be displayed
         */
        SongsAdapter(List<SongsEntity> songsList) {
            this.songsList = songsList;
        }

        /**
         * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         *
         * @param parent   The ViewGroup into which the new View will be added
         * @param viewType The view type of the new View
         * @return A new ViewHolder that holds a View of the given view type
         */
        @NonNull
        @Override
        public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SongViewHolderBinding songBinding = SongViewHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SongsViewHolder(songBinding.getRoot());
        }

        /**
         * Called by RecyclerView to display the data at the specified position.
         *
         * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position
         * @param position The position of the item within the adapter's data set
         */
        @Override
        public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
            SongsDatabase songsDatabase = Room.databaseBuilder(requireContext(),SongsDatabase.class, "deezerDataBase").build();
            DeezerDataAccessObject dDao = songsDatabase.deezerDao();

            SongsEntity song = songsList.get(position);
            holder.titleTextView.setText(song.getTitle());
            holder.artistTextView.setText(song.getArtistName());
            holder.durationTextView.setText(formatDuration(song.getDuration()));

            androidx.appcompat.widget.Toolbar toolbar = holder.songmenu;
            toolbar.inflateMenu(R.menu.songmenu);

            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.addToPlaylist:
                        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                        builder.setMessage("Do you want to add this song to your playlist?")
                                .setTitle("Add")
                                .setNegativeButton("No", (dialog, which) -> {
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {

                                    SongsEntity songToAdd = songsList.get(position);
                                    favsList.add(songToAdd);

                                    if (songToAdd != null) {
                                        songsAdapter.notifyItemChanged(position);
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            try {
                                                long result = dDao.insertSong(songToAdd);
                                                Log.d("InsertResult", "Rows affected: " + result);
                                            } catch (Exception e) {
                                                Log.e("InsertError", "Error inserting song", e);
                                            }
                                        });

                                        Log.d("SongToAdd", "Song to add: " + songToAdd.toString());

                                        Snackbar.make(requireView(), "Song added to playlist", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", (btn) -> {
                                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                                    thread2.execute(() -> {
                                                        dDao.deleteSongFromPlayList(songToAdd);
                                                    });

                                                    songsList.remove(songToAdd);
                                                    songsAdapter.notifyItemChanged(position);
                                                })
                                                .show();
                                    }
                                });
                        builder.create().show();
                        break;
                    case R.id.songInfo:
                        try {
                            String tracksURL = "https://api.deezer.com/album/" + album.getAlbumId() + "/tracks";

                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, tracksURL, null,
                                    (response) -> {
                                        try {
                                            JSONArray songsArray = response.getJSONArray("data");
                                            songsList.clear();

                                            for (int i = 0; i < songsArray.length(); i++) {
                                                JSONObject trackObject = songsArray.getJSONObject(i);
                                                long trackId = trackObject.getLong("id");
                                                String trackTitle = trackObject.getString("title");
                                                String albumCoverUrl = trackObject.getString("cover_xl");
                                                String artistName = trackObject.getJSONObject("artist").getString("name");
                                                int duration = trackObject.getInt("duration");

                                                SongsEntity track = new SongsEntity(trackId, trackTitle, duration, album.getTitle(), albumCoverUrl, artistName);
                                                songsList.add(track);
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        SongFragment songFragment = new SongFragment(holder.titleTextView.getText().toString(),holder.artistTextView.getText().toString(),holder.durationTextView.getText().toString(), album.getCoverUrl());
                                        FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
                                        FragmentTransaction transaction = fragmentManager1.beginTransaction();

                                        transaction.addToBackStack("");
                                        transaction.replace(R.id.songFragment, songFragment);
                                        transaction.commit();
                                    },
                                    error -> {
                                        error.printStackTrace();
                                    });
                            queue.add(request);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }

                return false;
            });
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter
         */
        @Override
        public int getItemCount() {
            return songsList.size();
        }

        /**
         * Format duration in minutes and seconds.
         *
         * @param duration Duration in seconds
         * @return Formatted duration (mm:ss)
         */
        private String formatDuration(int duration) {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }
}
