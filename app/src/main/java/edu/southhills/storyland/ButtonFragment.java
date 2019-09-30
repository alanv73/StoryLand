package edu.southhills.storyland;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ButtonFragment extends Fragment {

    public ButtonFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_button, container, false);

        Button homeButton = v.findViewById(R.id.goHomeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle arguments = getArguments();
                Intent intent = new Intent(getActivity(), MainActivity.class);

                if (arguments != null && arguments.containsKey("favRide")){
                    String favRide = getArguments().getString("favRide");
                    intent.putExtra("favRide", favRide);
                } else {
                    intent.putExtra("aboutReturn", "");
                }

                startActivity(intent);
            }
        });

        return v;
    }


}
