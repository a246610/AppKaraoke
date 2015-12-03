package com.nguyen.appkaraoke.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nguyen.appkaraoke.Model.Song;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "karaoke";
    public static int DATABASE_VERSION = 1;

    public static final String ALBUM_TABLE_MANE = "albums";
    public static final String ALBUM_ID_SONG = "id";
    public static final String ALBUM_NAME_SONG = "name";
    public static final String ALBUM_LYRICS_SONG = "lyrics";
    public static final String ALBUM_AUTHOR_SONG = "author";
    public static final String ALBUM_NAME_2_SONG = "name_";
    public static final String ALBUM_LYRICS_2_SONG = "lyrics_";
    public static final String ALBUM_AUTHOR_2_SONG = "author_";
    public static final String ALBUM_KEY_SONG = "key";
    public static final String ALBUM_FAVORITE_SONG = "favorite";

    public static final String FAVORITE_TABLE_NAME = "favorites";

    private Context context;

    public DbHelper(Context context_) {
        super(context_, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context_;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " + ALBUM_TABLE_MANE + " ("
                + ALBUM_ID_SONG + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ALBUM_NAME_SONG + " NVARCHAR(150) NOT NULL, "
                + ALBUM_KEY_SONG + " VARCHAR(10) NOT NULL,"
                + ALBUM_LYRICS_SONG + " NVARCHAR(250) NOT NULL, "
                + ALBUM_AUTHOR_SONG + " NVARCHAR(100) NOT NULL, "
                + ALBUM_NAME_2_SONG + " VARCHAR(150) NOT NULL, "
                + ALBUM_LYRICS_2_SONG + " VARCHAR(250) NOT NULL, "
                + ALBUM_AUTHOR_2_SONG + " VARCHAR(100) NOT NULL,"
                + ALBUM_FAVORITE_SONG + " INT )";

        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ALBUM_TABLE_MANE);
        onCreate(db);
    }

    public void initialize()
    {
        AssetManager asset = context.getAssets();
        try {
            InputStream in = asset.open("karaoke.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            StringBuilder content = new StringBuilder();

            int count = 0;

            while ((line = br.readLine()) != null){
                line = "(" + line + ", 0),";
                content.append( line);
                count++;
                if ((count % 100) == 0 ){
                    process(content.toString());
                    content.delete(0, content.length());
                }
            }

            if ((count % 100) != 0) process(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(String content){

        SQLiteDatabase db = getWritableDatabase();
        String tmp = "INSERT INTO " + ALBUM_TABLE_MANE + "("
                + ALBUM_NAME_SONG + ","
                + ALBUM_KEY_SONG + ","
                + ALBUM_LYRICS_SONG + ","
                + ALBUM_AUTHOR_SONG + ","
                + ALBUM_NAME_2_SONG + ","
                + ALBUM_LYRICS_2_SONG + ","
                + ALBUM_AUTHOR_2_SONG + ","
                + ALBUM_FAVORITE_SONG
                + ")" + " VALUES ";

        tmp += content;

        StringBuffer stringQuery = new StringBuffer(tmp);
        int len = stringQuery.length() - 1;
        stringQuery.replace(len ,len,";");

        db.execSQL(stringQuery.toString());
    }

    public List<Song> getAlbum(String keySearch){

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT "
                + ALBUM_ID_SONG + " ,"
                + ALBUM_KEY_SONG + " ,"
                + ALBUM_NAME_SONG + " ,"
                + ALBUM_LYRICS_SONG + " ,"
                + ALBUM_AUTHOR_SONG + " ,"
                + ALBUM_NAME_2_SONG + " ,"
                + ALBUM_LYRICS_2_SONG + ", "
                + ALBUM_FAVORITE_SONG
                + " FROM " + ALBUM_TABLE_MANE;

        if (keySearch == "") {

        }
        else {
            selectQuery += " WHERE (" + ALBUM_NAME_2_SONG + " LIKE '" + keySearch + "%')";
        }

        Cursor cursor = db.rawQuery(selectQuery,null);
        List<Song> albums = new ArrayList<Song>();

        if (!(cursor.moveToFirst() && cursor!= null)) return albums;
        while(true){
            Song tmp = new Song();
            tmp.setId(cursor.getString(cursor.getColumnIndex(DbHelper.ALBUM_ID_SONG)));
            tmp.setKey(cursor.getString(cursor.getColumnIndex(DbHelper.ALBUM_KEY_SONG)));
            tmp.setName(cursor.getString(cursor.getColumnIndex(DbHelper.ALBUM_NAME_SONG)));
            tmp.setLyrics(cursor.getString(cursor.getColumnIndex(DbHelper.ALBUM_LYRICS_SONG)));
            tmp.setAuthor(cursor.getString(cursor.getColumnIndex(DbHelper.ALBUM_AUTHOR_SONG)));
            boolean tmpFavorite = cursor.getInt(cursor.getColumnIndex(DbHelper.ALBUM_FAVORITE_SONG)) == 1;
            tmp.setFavorite(tmpFavorite);
            albums.add(tmp);
            if (!cursor.moveToNext()) break;
        }

        return albums;
    }

    public boolean checkExistsDatabase(){
        File db = context.getDatabasePath(DATABASE_NAME);
        return (db.exists() ? true : false);
    }

    public void updateAlbumSong(String id, boolean value){
        int tmp = value ? 1 : 0;
        String queryString = "UPDATE " + ALBUM_TABLE_MANE
                + " SET " + ALBUM_FAVORITE_SONG + " = " + tmp
                + " WHERE " + ALBUM_ID_SONG + " = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(queryString);
    }
}
