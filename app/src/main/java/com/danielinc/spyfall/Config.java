package com.danielinc.spyfall;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Config#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Config extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Config() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Config.
     */
    public static Config newInstance(String param1, String param2) {
        Config fragment = new Config();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        functionality(view);
        return view;
    }

    RadioButton[] radio = new RadioButton[5];
    Spinner spinner;
    Button Update;
    private void functionality(View view) {
        Update = view.findViewById(R.id.UpdateNOP);

        spinner = view.findViewById(R.id.spinner);

        radio[0] = view.findViewById(R.id.radio_3);
        radio[1] = view.findViewById(R.id.radio_4);
        radio[2] = view.findViewById(R.id.radio_5);
        radio[3] = view.findViewById(R.id.radio_6);
        radio[4] = view.findViewById(R.id.radio_7);

        //setting up the spinner (android combobox)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.min,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Update.setOnClickListener(v->{
            saveData(view);

        });

        loadData(view);
    }

    public void saveData(View view){
        Context context = view.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.sharedpref),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        for(RadioButton x : radio){
            if(x.isChecked()){
                switch (x.getText().toString()){
                    case "3":
                        editor.putInt("NumOfPlayers",3).commit();
                        break;
                    case "4":
                        editor.putInt("NumOfPlayers",4).commit();
                        break;
                    case "5":
                        editor.putInt("NumOfPlayers",5).commit();
                        break;
                    case "6":
                        editor.putInt("NumOfPlayers",6).commit();
                        break;
                    case "7":
                        editor.putInt("NumOfPlayers",7).commit();
                        break;
                }
            }
        }
        switch (spinner.getSelectedItem().toString()){
            case "6:00":
                editor.putInt("RoundTime",6).commit();
                break;
            case "7:00":
                editor.putInt("RoundTime",7).commit();
                break;
            case "8:00":
                editor.putInt("RoundTime",8).commit();
                break;
            case "9:00":
                editor.putInt("RoundTime",9).commit();
                break;
            case "10:00":
                editor.putInt("RoundTime",10).commit();
                break;
        }
    }

    public void loadData(View view){
        Context context = view.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.sharedpref),Context.MODE_PRIVATE);
        switch (sharedPref.getInt("RoundTime",1)){
            case 6:
                spinner.setSelection(0);
                break;
            case 7:
                spinner.setSelection(1);
                break;
            case 8:
                spinner.setSelection(2);
                break;
            case 9:
                spinner.setSelection(3);
                break;
            case 10:
                spinner.setSelection(4);
                break;
        }
        switch (sharedPref.getInt("NumOfPlayers",1)){
            case 3:
                radio[0].setChecked(true);
                break;
            case 4:
                radio[1].setChecked(true);
                break;
            case 5:
                radio[2].setChecked(true);
                break;
            case 6:
                radio[3].setChecked(true);
                break;
            case 7:
                radio[4].setChecked(true);
                break;
        }
    }

}