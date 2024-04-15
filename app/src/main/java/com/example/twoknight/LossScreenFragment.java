package com.example.twoknight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LossScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LossScreenFragment extends Fragment {

    private TextView moneyTextView;
    private TextView levelTextView;
    private DataSaver dataSaver;

    public LossScreenFragment() {
        // Required empty public constructor
    }

    public static LossScreenFragment newInstance(String param1, String param2) {
        LossScreenFragment fragment = new LossScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSaver = new DataSaver(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_loss_screen, container, false);
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
        NavHostFragment.findNavController(LossScreenFragment.this)
                .navigate(R.id.action_LossScreenFragment_to_ShopFragment);
    }

    private void mainMenu(View view) {
        NavHostFragment.findNavController(LossScreenFragment.this)
                .navigate(R.id.action_LossScreenFragment_to_MenuFragment);
    }

    public void startGame(View view) {
        NavHostFragment.findNavController(LossScreenFragment.this)
                .navigate(R.id.action_LossScreenFragment_to_GameFragment);
    }
}