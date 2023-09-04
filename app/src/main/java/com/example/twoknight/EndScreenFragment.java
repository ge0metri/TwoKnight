package com.example.twoknight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EndScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EndScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView moneyTextView;
    private TextView levelTextView;
    private DataSaver dataSaver;

    public EndScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EndScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EndScreenFragment newInstance(String param1, String param2) {
        EndScreenFragment fragment = new EndScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
         dataSaver = new DataSaver(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_end_screen, container, false);
        // Find the buttons by their IDs and set click listeners
        Button playButton = rootView.findViewById(R.id.btnNextLevel);
        Button mainMenuButton = rootView.findViewById(R.id.btnMenu);
        Button shopButton = rootView.findViewById(R.id.btnShop);
        playButton.setOnClickListener(this::startGame);
        mainMenuButton.setOnClickListener(this::mainMenu);
        shopButton.setOnClickListener(this::toShop);

        moneyTextView = rootView.findViewById(R.id.moneyText);
        levelTextView = rootView.findViewById(R.id.levelText);
        updateDisplay();
        return rootView;
    }

    private void updateDisplay() {
        String levelText = "Current level: " + dataSaver.loadCurrentLevel();
        levelTextView.setText(levelText);
        String moneyText = "$ " + dataSaver.loadMoney() + " $";
        moneyTextView.setText(moneyText);
    }

    private void toShop(View view) {
    }

    private void mainMenu(View view) {
        NavHostFragment.findNavController(EndScreenFragment.this)
                .navigate(R.id.action_EndScreenFragment_to_MenuFragment);
    }

    public void startGame(View view) {
        NavHostFragment.findNavController(EndScreenFragment.this)
                .navigate(R.id.action_EndScreenFragment_to_GameFragment);
    }
}