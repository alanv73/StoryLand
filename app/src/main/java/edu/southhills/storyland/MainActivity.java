package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Boolean firstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);

        int newLayout = getLayoutFromPref();
        scene_change(newLayout);

        String myRide = "";
        String username = "";

        // check for username passed from login activity
        if(getIntent().hasExtra("username")){
            username = getIntent().getStringExtra("username");
            // save it as an "app-global" pref
            saveUserToPref(username);
        } else {
            // if no username was passed, get the last one from the prefs
            username = getUserFromPref();
        }

        // display the current user
        String userText = String.format("Current User: %s", username);

        TextView tvUser = findViewById(R.id.tvUser);
        tvUser.setText(userText);

        // fragment containing the logo
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainActivityContainer, new LogoFragment()).commit();

        // get data from the intent
        if (getIntent().hasExtra("favRide")) {
            // returning from the 'ride survey' activity
            myRide = readRatingFromIntent();

            // save it to a "user" pref
            if(!username.equalsIgnoreCase("")) {
                saveRatingToPref(username, myRide);
            }

            // display it in a toast
            makeToast(String.format("We're glad you liked\n%s", myRide));
        } else if (getIntent().hasExtra("activityReturn")) {
            // returning from the 'about' activity or the 'login' activity
        } else {
            // not returning from an activity
            checkOsVersion();
        }

        // if you didn't get a rating from the intent
        // and you are logged in, load one from the "user" pref
        if(myRide.equalsIgnoreCase("") &&
                !username.equalsIgnoreCase("")){
            myRide = getRatingFromPref(username);
        }

        // send the rating to a fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainActivityContainer, new MessageFragment(myRide)).commit();
        getOrientation();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    public String readRatingFromIntent(){
        Intent currentIntent = getIntent();
        return currentIntent.getStringExtra("favRide");
    }

    // displays a toast
    public void makeToast(String toastText){
        Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
    }

    // shows the login activity
    public void showLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public int getLayoutFromPref(){
        SharedPreferences appPrefs = getSharedPreferences ("appPrefs", MODE_PRIVATE);
        return appPrefs.getInt("layout", R.layout.main_b);
    }

    // gets current user from "app-global" prefs
    public String getUserFromPref(){
        SharedPreferences appPrefs = getSharedPreferences ("appPrefs", MODE_PRIVATE);
        return appPrefs.getString("username", "");
    }

    public void saveLayoutToPref(int layout){
        SharedPreferences appPrefs = getSharedPreferences ("appPrefs", MODE_PRIVATE);
        appPrefs.edit().putInt("layout", layout).commit();
    }

    // save current user to "app-global" prefs
    public void saveUserToPref(String username){
        SharedPreferences appPrefs = getSharedPreferences ("appPrefs", MODE_PRIVATE);
        appPrefs.edit().putString("username", username).commit();
    }

    // get rating from "user" pref by user name
    public String getRatingFromPref(String user){
        SharedPreferences prefs = getSharedPreferences (user, MODE_PRIVATE);
        return prefs.getString("favRide", "");
    }

    // save rating to "user" pref
    public void saveRatingToPref(String user, String rating){
        SharedPreferences prefs = getSharedPreferences (user, MODE_PRIVATE);
        prefs.edit().putString("favRide", rating).commit();
    }

    public void imageClick(View v){
        int newLayout = R.layout.main_a;
        if(getLayoutFromPref() == R.layout.main_a) {
            newLayout = R.layout.main_b;
        }

        scene_change(newLayout);
        saveLayoutToPref(newLayout);
    }

    public void scene_change(int layout){
        ViewGroup sceneContainer = findViewById(R.id.main_root);

        Scene newScene = Scene.getSceneForLayout(sceneContainer, layout, this);

        Transition bounce = new ChangeBounds();
        bounce.setDuration(500);
        bounce.setInterpolator(new AnticipateInterpolator());

        TextView tvOrientation;
        TextView tvUser;
        String orientation = "";
        String user = "";

        if(!firstRun) {
            tvOrientation = findViewById(R.id.tvOrientation);
            tvUser = findViewById(R.id.tvUser);
            orientation = tvOrientation.getText().toString();
            user = tvUser.getText().toString();
        }

        TransitionManager.go(newScene, bounce);

        if(!firstRun) {
            tvOrientation = findViewById(R.id.tvOrientation);
            tvUser = findViewById(R.id.tvUser);
            tvOrientation.setText(orientation);
            tvUser.setText(user);
        }
        firstRun = false;
    }

}

