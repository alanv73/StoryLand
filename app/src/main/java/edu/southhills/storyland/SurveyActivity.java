package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SurveyActivity extends AppCompatActivity {

    private ButtonFragment bf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_survey);

        // logo and button fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LogoFragment lf = new LogoFragment();
        bf = new ButtonFragment();
        ft.add(R.id.surveyContainer, lf);
        ft.add(R.id.surveyContainer, bf);
        ft.commit();

        // Radio Group Click Listener
        RadioGroup rgFavRide = findViewById(R.id.radioGroupSurvey);
        rgFavRide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedIdx) {
                // read the text of the selected radio button and store it in an Extra called 'favRide'
                RadioButton rbFavRide = findViewById(checkedIdx);

                // pass selected RadioButton into existing Button Fragment
                bf.setArguments(rbFavRide);
            }
        });
    }




}
