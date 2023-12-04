package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpterDeezSong extends RecyclerView.Adapter<AdpterDeezSong.SongViewHolder> {

    private List<KpSongs> songList;

    public AdpterDeezSong(List<KpSongs> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songs_itm, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        KpSongs song = songList.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    // Inside the AdpterDeezSong class
    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView textSongTitle;
        TextView textSongDuration;
        View itemView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            textSongTitle = itemView.findViewById(R.id.textSongTitle);
            textSongDuration = itemView.findViewById(R.id.textSongDuration);
        }

        public void bind(KpSongs song) {
            textSongTitle.setText(song.getTitle());
            textSongDuration.setText(song.getDuration());

            // Handle item click to show song details in FragofSongDetails fragment
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragofSongDetails fragment = new FragofSongDetails();
                    fragment.setSelectedSong(song);

                    AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.textSongTitle, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
    }
}
