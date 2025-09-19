package com.example.listycity3;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

// ▼ NEW imports
import android.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;

    // ▼ NEW fields
    Button addBtn, deleteBtn;
    int selectedPosition = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

        String []cities = {"Edmonton", "Vancouver", "Moscow", "Sydney", "Berlin", "Vienna", "Beijing", "Tokyo", "Osaka", "New Delhi"};

        dataList = new ArrayList<>();
        dataList.addAll(Arrays.asList(cities));

        // (YOUR ORIGINAL LINE — kept)
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);

        // ▼ NEW: reassign adapter so it knows which TextView in content.xml to use
        // (keeps your original code, but correctly targets R.id.content_view)
        cityAdapter = new ArrayAdapter<>(this, R.layout.content, R.id.content_view, dataList);
        cityList.setAdapter(cityAdapter);

        // ▼ NEW: wire buttons (must exist in your activity_main.xml)
        addBtn = findViewById(R.id.btn_add);
        deleteBtn = findViewById(R.id.btn_delete);

        // ▼ NEW: remember which item is tapped (for deletion)
        cityList.setOnItemClickListener((parent, view, position, id) -> selectedPosition = position);

        // ▼ NEW: ADD CITY → prompt → CONFIRM
        addBtn.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setHint("City name");

            new AlertDialog.Builder(this)
                    .setTitle("Add City")
                    .setView(input)
                    .setPositiveButton("CONFIRM", (dialog, which) -> {
                        String name = input.getText().toString().trim();
                        if (name.isEmpty()) {
                            Toast.makeText(this, "City name cannot be empty.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        dataList.add(name);
                        cityAdapter.notifyDataSetChanged();
                        cityList.smoothScrollToPosition(dataList.size() - 1);
                    })
                    .setNegativeButton("CANCEL", null)
                    .show();
        });

        // ▼ NEW: DELETE CITY → remove selected item
        deleteBtn.setOnClickListener(v -> {
            if (selectedPosition >= 0 && selectedPosition < dataList.size()) {
                dataList.remove(selectedPosition);
                cityAdapter.notifyDataSetChanged();
                selectedPosition = -1; // clear selection
            } else {
                Toast.makeText(this, "Tap a city first to select it.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
