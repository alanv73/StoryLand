package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fragment containing Logo
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new LogoFragment()).commit();

        // get reference the EditText containing the user name
        EditText etUser = findViewById(R.id.etUser);

        // Home button fragment, passing the username EditText during instantiation
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new ButtonFragment(etUser)).commit();


    }
}
