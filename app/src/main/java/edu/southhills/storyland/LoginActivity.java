package edu.southhills.storyland;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);

        // Fragment containing Logo
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new LogoFragment()).commit();

        // get reference the EditText containing the user name
        final EditText etUser = findViewById(R.id.etUser);

        // Home button fragment, passing the username EditText during instantiation
        getSupportFragmentManager().beginTransaction()
                .add(R.id.loginActivityContainer, new ButtonFragment(etUser)).commit();

        etUser.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_UP &&
                        keyCode == KeyEvent.KEYCODE_ENTER){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("username", etUser.getText().toString());
                    startActivity(intent);
                }
                return false;
            }
        });

    }
}
