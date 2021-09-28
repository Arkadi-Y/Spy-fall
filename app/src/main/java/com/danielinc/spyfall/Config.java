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
    Switch switch1;
    TextView AmountTxt;
    Button Update;
    private void functionality(View view) {
        Update = view.findViewById(R.id.UpdateNOP);
        AmountTxt = view.findViewById(R.id.AmountTxt);
        spinner = view.findViewById(R.id.spinner);
        switch1 = view.findViewById(R.id.SpyCount);
        radio[0] = view.findViewById(R.id.radio_3);
        radio[1] = view.findViewById(R.id.radio_4);
        radio[2] = view.findViewById(R.id.radio_5);
        radio[3] = view.findViewById(R.id.radio_6);
        radio[4] = view.findViewById(R.id.radio_7);

        //setting up the spinner (android combobox)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),R.array.min,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //changing the state of the textview from one spy to two spies
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(switch1.isChecked()){
                    AmountTxt.setText(R.string.twospy);
                }
                else{
                    AmountTxt.setText(R.string.Onespy);
                }
            }
        });

        Update.setOnClickListener(v->{
            saveData(view);
            loadData(view);
        });


    }

    public void saveData(View view){
        Context context = view.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("NumOfPlayers",3);
        editor.putBoolean("spyCount",false);
        editor.putString("RoundTime","6:00");
        Log.d("Log",sharedPref.getAll().toString());
    }

    public void loadData(View view){
        Context context = view.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE);
        Toast.makeText(view.getContext(), getString(R.string.sharedpref),Toast.LENGTH_SHORT);
        AmountTxt.setText(sharedPref.getAll().toString());
        Log.d("Log",sharedPref.getAll().toString());
    }

}