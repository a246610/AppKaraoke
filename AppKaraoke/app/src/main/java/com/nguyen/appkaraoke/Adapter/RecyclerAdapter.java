package com.nguyen.appkaraoke.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.nguyen.appkaraoke.Database.DbHelper;
import com.nguyen.appkaraoke.Model.Song;
import com.nguyen.appkaraoke.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RowRecyclerHolder> {

    private Context context;
    private List<Song> albums;

    public RecyclerAdapter(Context context_, List<Song> albums_) {
        this.context = context_;
        this.albums = albums_;
    }

    @Override
    public RowRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_recycler_view, parent, false);
        RowRecyclerHolder holder = new RowRecyclerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RowRecyclerHolder holder, int position) {
        final Song song = albums.get(position);

        holder.tvKey.setText(song.getKey());
        holder.tvName.setText(song.getName());
        holder.tvLyrics.setText(Html.fromHtml("<b>Lời:</b> " + song.getLyrics()));

        if (song.isFavorite())
            holder.checkFavorite.setChecked(true);
        else
            holder.checkFavorite.setChecked(false);

        holder.checkFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHelper db = new DbHelper(context);
                CheckBox checkBox = (CheckBox) v;

                if (checkBox.isChecked()){
                    db.updateAlbumSong(song.getId(),true);
                    song.setFavorite(true);
                    checkBox.setChecked(true);
                }

                else{
                    db.updateAlbumSong(song.getId(),false);
                    song.setFavorite(false);
                    checkBox.setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (albums != null ? albums.size() : 0);
    }
}
