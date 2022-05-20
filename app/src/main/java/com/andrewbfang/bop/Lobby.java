package com.andrewbfang.bop;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.andrewbfang.bop.R;

import java.util.ArrayList;

/**
 * Created by Dan on 6/11/13.
 */
public class Lobby extends ListActivity {

    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    int clickCounter = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2,
                //R.layout.activity_lobby,
                //android.R.id.empty,
                listItems);
        setListAdapter(adapter);

        addPlayer("Host");
        addPlayer("Alice");
        addPlayer("Bob");
    }

    public void addPlayer(String name) {
        listItems.add(name);
        adapter.notifyDataSetChanged();
    }
}