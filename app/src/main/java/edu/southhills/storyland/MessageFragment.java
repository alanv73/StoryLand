package edu.southhills.storyland;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MessageFragment extends Fragment {

    private String myString;

    public MessageFragment(String myInput) {
        // Required empty public constructor
        this.myString = myInput;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        TextView tvMessage = v.findViewById(R.id.tvfMessage);
        tvMessage.setText(myString);


        return v;
    }

}
