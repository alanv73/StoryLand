package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_about);

        // logo and home button fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoFragment lf = new LogoFragment();
        final ButtonFragment bf = new ButtonFragment();
        ft.add(R.id.aboutContainer, lf);
        ft.add(R.id.aboutContainer, bf);
        ft.commit();

        // the TextView contains a hyperlink
        // this makes it clickable
        TextView tvAbout = findViewById(R.id.tvAbout);
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void mapButton(View v){
        String storyLand = "StoryLand 850 NH Route 16, Glen, NH 03838";

        gotoMaps(storyLand);
    }

    // method to launch google maps
    private void gotoMaps(String address){
        Uri location = Uri.parse("geo:?q=" + address);

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);

        Intent chooser = Intent.createChooser(mapIntent, "Open Map");

        try{
            startActivity(chooser);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Map Client not Found", Toast.LENGTH_SHORT).show();
        }

    }

    public void shareButton(View v){
        Intent shareIntent = new Intent(this, ShareActivity.class);
        startActivity(shareIntent);
    }

}
