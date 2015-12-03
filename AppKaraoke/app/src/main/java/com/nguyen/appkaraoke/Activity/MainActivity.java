package com.nguyen.appkaraoke.Activity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.nguyen.appkaraoke.Adapter.RecyclerAdapter;
import com.nguyen.appkaraoke.Database.DbHelper;
import com.nguyen.appkaraoke.Model.Song;
import com.nguyen.appkaraoke.MyLib.StringHander;
import com.nguyen.appkaraoke.R;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Song> albums;
    private EditText editSearch;
    private DbHelper db;
    private RecyclerAdapter adapter;

    private static final int CODE_SPEECH_INPUT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initializeUI();

    }

    private void initializeUI(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DbHelper(this);
        if (!db.checkExistsDatabase())
            db.initialize();

        displayData("");

        editSearch = (EditText)findViewById(R.id.edit_search);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayData(editSearch.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    /* REFERENCE FOR SPEECH INPUT
        ACTION_RECOGNIZE_SPEECH – Simply takes user’s speech input and returns it to same activity
        LANGUAGE_MODEL_FREE_FORM – Considers input in free form English
        EXTRA_PROMPT – Text prompt to show to the user when asking them to speak
     */
    private void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        startActivityForResult(i, CODE_SPEECH_INPUT);
    }

    //receiving speech input
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_SPEECH_INPUT && resultCode == RESULT_OK)
        {
            List<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editSearch.setText(result.get(0));
            displayData(result.get(0));
        }
    }

    private void displayData(String stringSearch){
        StringHander stringHander = new StringHander(stringSearch);
        List<Song> albums = db.getAlbum(stringHander.removeAccent());
        adapter = new RecyclerAdapter(this, albums);
        mRecyclerView.setAdapter(adapter);
    }
}
