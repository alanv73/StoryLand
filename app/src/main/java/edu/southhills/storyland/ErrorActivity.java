package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ErrorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_error);
    }
}
