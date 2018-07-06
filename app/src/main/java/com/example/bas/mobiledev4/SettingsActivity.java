package com.example.bas.mobiledev4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    public static final String KEY_PREF_MAPBUTTON = "mapbutton";                                                    //BUTTON FOR THE MAP
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_1);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean sliderHandler = sharedPref.getBoolean("imageoffbutton", true);                                   //CHECK OF SLIDER IS OFF


        if (sliderHandler) {
            ImageView imageView = findViewById(R.id.androidpic);                                                          //IF SLIDER IS ON, PICTURE WILL APPEAR
            imageView.setVisibility(View.GONE);
            Toast.makeText(this, "No picture visible, slide to turn it on", Toast.LENGTH_SHORT).show();
        }
        else {
            ImageView imageView = findViewById(R.id.androidpic);                                                            //IF SLIDER IS OFF PICTURE WILL VANISH
            imageView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Picture is visible, want to turn it off?", Toast.LENGTH_SHORT).show();

        }

        Boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_MAPBUTTON, false);                                                             //MAP BUTTON
        Toast.makeText(this, switchPref.toString(), Toast.LENGTH_SHORT).show();
    }
}
