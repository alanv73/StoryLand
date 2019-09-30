package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoFragment lf = new LogoFragment();
        final ButtonFragment bf = new ButtonFragment();
        ft.add(R.id.aboutContainer, lf);
        ft.add(R.id.aboutContainer, bf);
        ft.commit();

        TextView tvAbout = findViewById(R.id.tvAbout);
        tvAbout.setMovementMethod(LinkMovementMethod.getInstance());
    }


}
