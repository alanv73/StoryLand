package edu.southhills.storyland;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class ButtonFragment extends Fragment {

    private View activityView = null;

    // constructor for passing View objects into fragment
    // during instantiation
    public ButtonFragment(View v) {
        // Required empty public constructor
        this.activityView = v;
    }

    public ButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_button, container, false);

        // button to return to the Main Activity
        Button homeButton = v.findViewById(R.id.goHomeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user;
                String favRide;
                Intent intent = new Intent(getActivity(), MainActivity.class);

                // if a View object was passed, process it
                if(activityView != null){
                    // if it's an EditText, read the text
                    if(activityView instanceof EditText) {
                        EditText etUser = (EditText) activityView;
                        user = etUser.getText().toString();
                        if(!user.equalsIgnoreCase("")){
                            intent.putExtra("username", user);
                        }
                    } else {
                        // Read the RadioButton Text
                        RadioButton rb = (RadioButton) activityView;
                        favRide = rb.getText().toString();
                        if(!favRide.equalsIgnoreCase("")){
                            intent.putExtra("favRide", favRide);
                        }
                    }
                }

                // dummy value to indicate we've returned from an activity
                // mostly just for the about activity
                // also the login activity if no user was entered
                intent.putExtra("activityReturn", "");
                startActivity(intent);
            }
        });

        return v;
    }

    // method for passing View objects into already instantiated fragment
    public void setArguments(View v) {
        this.activityView = v;
    }
}
