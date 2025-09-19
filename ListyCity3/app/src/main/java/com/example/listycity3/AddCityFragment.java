package com.example.listycity3;

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

    public interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int index, City updatedCity);
    }

    private static final String ARG_CITY  = "arg_city";
    private static final String ARG_INDEX = "arg_index";

    private AddCityDialogListener listener;

    // Factory method for ADD mode
    public static AddCityFragment newAddInstance() {
        return new AddCityFragment();
    }

    // Factory method for EDIT mode
    public static AddCityFragment newEditInstance(City city, int index) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_CITY, city);   // City implements Serializable
        b.putInt(ARG_INDEX, index);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);

        // IMPORTANT: these IDs must exist in fragment_add_city.xml
        EditText cityEdit = view.findViewById(R.id.edit_text_city_text);
        EditText provinceEdit = view.findViewById(R.id.edit_text_province_text);

        Bundle args = getArguments();
        final boolean isEditing = args != null && args.containsKey(ARG_CITY);
        final int index = isEditing ? args.getInt(ARG_INDEX) : -1;
        final City cityToEdit = isEditing ? (City) args.getSerializable(ARG_CITY) : null;

        if (isEditing && cityToEdit != null) {
            cityEdit.setText(cityToEdit.getName());
            provinceEdit.setText(cityToEdit.getProvince());
        }

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(isEditing ? "Edit City" : "Add a City")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEditing ? "Save" : "Add", (dialog, which) -> {
                    City result = new City(
                            cityEdit.getText().toString().trim(),
                            provinceEdit.getText().toString().trim()
                    );
                    if (isEditing) {
                        listener.updateCity(index, result);
                    } else {
                        listener.addCity(result);
                    }
                })
                .create();
    }
}
