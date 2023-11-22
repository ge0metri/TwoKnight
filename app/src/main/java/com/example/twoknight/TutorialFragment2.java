package com.example.twoknight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialFragment2 extends Fragment {


    public TutorialFragment2() {
        // Required empty public constructor
    }

    public static TutorialFragment2 newInstance(String param1, String param2) {
        TutorialFragment2 fragment = new TutorialFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial2, container, false);
        Button nextButton = view.findViewById(R.id.tutorialMenuButton);
        nextButton.setOnClickListener(this::nextSlide);
        // Inflate the layout for this fragment
        return view;
    }

    private void nextSlide(View view) {
        NavHostFragment.findNavController(TutorialFragment2.this)
                .navigate(R.id.action_TutorialFragment2_to_MenuFragment);
    }
}