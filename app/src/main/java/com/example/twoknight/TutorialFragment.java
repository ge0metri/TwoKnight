package com.example.twoknight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorialFragment extends Fragment {

    public TutorialFragment() {
        // Required empty public constructor
    }


    public static TutorialFragment newInstance(String param1, String param2) {
        TutorialFragment fragment = new TutorialFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        Button nextButton = view.findViewById(R.id.nextTutorialButton);
        nextButton.setOnClickListener(this::nextSlide);
        // Inflate the layout for this fragment
        return view;
    }

    private void nextSlide(View view) {
        NavHostFragment.findNavController(TutorialFragment.this)
                .navigate(R.id.action_TutorialFragment_to_TutorialFragment2);
    }
}