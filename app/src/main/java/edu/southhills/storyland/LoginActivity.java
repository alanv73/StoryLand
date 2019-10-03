package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fragment containing Logo
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new LogoFragment()).commit();

        // read the user name from the EditText on the Login activity
        TextView etUser = findViewById(R.id.etUser);

        // Home button fragment, passing the username during instantiation
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new ButtonFragment(etUser)).commit();


    }
}
