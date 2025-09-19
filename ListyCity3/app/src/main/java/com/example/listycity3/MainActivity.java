package com.example.listycity3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.listycity3.AddCityFragment;
import com.example.listycity3.City;
import com.example.listycity3.CityArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements AddCityFragment.AddCityDialogListener {

    private ArrayList<City> dataList;
    private CityArrayAdapter cityAdapter;
    private ListView cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Seed data
        String[] cities    = {"Edmonton", "Vancouver", "Toronto"};
        String[] provinces = {"AB", "BC", "ON"};

        dataList = new ArrayList<>();
        for (int i = 0; i < cities.length; i++) {
            dataList.add(new City(cities[i], provinces[i]));
        }

        cityList = findViewById(R.id.city_list);
        cityAdapter = new CityArrayAdapter(this, dataList);
        cityList.setAdapter(cityAdapter);

        // ADD: FAB opens dialog to add a new city
        FloatingActionButton fab = findViewById(R.id.button_add_city);
        fab.setOnClickListener(v ->
                AddCityFragment.newAddInstance()
                        .show(getSupportFragmentManager(), "Add City"));

        // EDIT: tap a row to edit that city
        cityList.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            City selected = dataList.get(position);
            AddCityFragment.newEditInstance(selected, position)
                    .show(getSupportFragmentManager(), "Edit City");
        });
    }

    // ----- Dialog callbacks -----
    @Override
    public void addCity(City city) {
        cityAdapter.add(city);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateCity(int index, City updatedCity) {
        dataList.set(index, updatedCity);
        cityAdapter.notifyDataSetChanged();
    }
}
