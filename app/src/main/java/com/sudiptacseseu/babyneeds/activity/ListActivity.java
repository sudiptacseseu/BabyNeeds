package com.sudiptacseseu.babyneeds.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.sudiptacseseu.babyneeds.R;
import com.sudiptacseseu.babyneeds.data.DatabaseHandler;
import com.sudiptacseseu.babyneeds.adapter.RecyclerViewAdapter;
import com.sudiptacseseu.babyneeds.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText babyItemEditText;
    private EditText itemQuantityEditText;
    private EditText itemColorEditText;
    private EditText itemSizeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.recyclerview);
        fab = findViewById(R.id.fab);

        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();

        //Get items from db
        itemList = databaseHandler.getAllItems();

        for (Item item : itemList) {
            Log.d(TAG, "onCreate: " + item.getItemColor());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup, null);
        babyItemEditText = view.findViewById(R.id.babyItem);
        itemQuantityEditText = view.findViewById(R.id.itemQuantity);
        itemColorEditText = view.findViewById(R.id.itemColor);
        itemSizeEditText = view.findViewById(R.id.itemSize);
        saveButton = view.findViewById(R.id.saveButton);

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!babyItemEditText.getText().toString().isEmpty()
                        && !itemColorEditText.getText().toString().isEmpty()
                        && !itemQuantityEditText.getText().toString().isEmpty()
                        && !itemSizeEditText.getText().toString().isEmpty()) {
                    saveItem(v);
                }else {
                    Snackbar.make(v, "Empty Fields not Allowed", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

    private void saveItem(View view) {
        //Save each baby item to db
        Item item = new Item();

        String newItem = babyItemEditText.getText().toString().trim();
        String newColor = itemColorEditText.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantityEditText.getText().toString().trim());
        int size = Integer.parseInt(itemSizeEditText.getText().toString().trim());

        item.setItemName(newItem);
        item.setItemColor(newColor);
        item.setItemQuantity(quantity);
        item.setItemSize(size);

        databaseHandler.addItem(item);
        Snackbar.make(view, "Item Saved",Snackbar.LENGTH_SHORT)
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss();
                //Move to next screen - details screen
                startActivity(new Intent(ListActivity.this, ListActivity.class));
                finish();
            }
        }, 1200);// 1sec
    }
}
