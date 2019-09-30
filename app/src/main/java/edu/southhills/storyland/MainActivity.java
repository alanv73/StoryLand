package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoFragment lf = new LogoFragment();
        ft.add(R.id.mainActivityContainer, lf);

        if (getIntent().hasExtra("favRide")) {
            // returning from the 'ride survey' activity
            String message = readFavRide();
            makeToast(String.format("We're glad you liked %s", message));

            message = String.format("Favorite Ride:\n%s", message);
            MessageFragment mf = new MessageFragment(message);
            ft.add(R.id.mainActivityContainer, mf);
        } else if (getIntent().hasExtra("aboutReturn")) {
            // returning from the 'about' activity
        } else {
            // not returning from an activity
            checkOsVersion();
        }

        ft.commit();
        getOrientation();
    }

    // start About activity
    public void aboutPage(View v){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    // start ride survey activity
    public void surveyPage(View v) {
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
    }

    // Android OS version check
    public void checkOsVersion(){
        String toastText;
        String osVersion = Build.VERSION.RELEASE;

        // is OS version at least 8.0 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            toastText = String.format("Thank You for using Android version %s", osVersion);
        } else {
            Intent intent = new Intent(this, ErrorActivity.class);
            startActivity(intent);

            toastText = String.format("Android version %s is out of date,\n"
                    + "Please update your Android Operating System", osVersion);
        }

        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    // displays device orientation in a text view
    public void getOrientation(){
        String orientation;

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            orientation = "Landscape Orientation"; //R.string.txt_landscape;
        } else {
            orientation = "Portrait Orientation";
        }

        TextView tvOrientation = findViewById(R.id.tvOrientation);
        tvOrientation.setText(orientation);
    }

    // reads content of 'favRide' Extra and displays it in a toast and a text view
    public String readFavRide(){
        Intent currentIntent = getIntent();
        String favRide = currentIntent.getStringExtra("favRide");

        return favRide;
    }

    public void makeToast(String toastText){
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

}
