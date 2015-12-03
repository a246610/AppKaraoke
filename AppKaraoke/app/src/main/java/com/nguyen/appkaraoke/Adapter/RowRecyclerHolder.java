package com.nguyen.appkaraoke.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.nguyen.appkaraoke.R;

public class RowRecyclerHolder extends RecyclerView.ViewHolder {
    public TextView tvName;
    public TextView tvKey;
    public TextView tvLyrics;
    public CheckBox checkFavorite;

    public RowRecyclerHolder(View view) {
        super(view);
        this.tvName = (TextView) view.findViewById(R.id.tvName);
        this.tvKey = (TextView) view.findViewById(R.id.tvKey);
        this.tvLyrics = (TextView) view.findViewById(R.id.tvLyrics);
        this.checkFavorite = (CheckBox) view.findViewById(R.id.checkFavorite);
    }
}
