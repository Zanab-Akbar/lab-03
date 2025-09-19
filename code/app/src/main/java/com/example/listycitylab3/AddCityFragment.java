// AddCityFragment.java
package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    // Keys for arguments
    private static final String ARG_CITY  = "arg_city";
    private static final String ARG_INDEX = "arg_index";

    interface AddCityDialogListener{
        void addCity(City city);
        void updateCity(int index, City updatedCity);   // NEW
    }

    private AddCityDialogListener listener;

    // Preferred factory methods (no custom constructors)
    public static AddCityFragment newAddInstance() {
        return new AddCityFragment();
    }

    public static AddCityFragment newEditInstance(City city, int index) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_CITY, city);
        b.putInt(ARG_INDEX, index);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener){
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName     = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        // Are we editing?
        Bundle args = getArguments();
        final boolean isEditing = args != null && args.containsKey(ARG_CITY);
        final int index = isEditing ? args.getInt(ARG_INDEX) : -1;
        final City cityToEdit = isEditing ? (City) args.getSerializable(ARG_CITY) : null;

        if (isEditing && cityToEdit != null) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(isEditing ? "Edit City" : "Add a City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEditing ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString().trim();
                    String provinceName = editProvinceName.getText().toString().trim();

                    if (isEditing) {
                        City updated = new City(cityName, provinceName);
                        listener.updateCity(index, updated);
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }
}
