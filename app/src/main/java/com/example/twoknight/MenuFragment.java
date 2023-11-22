package com.example.twoknight;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    private TextView levelTextView;
    private TextView moneyTextView;

    private DataSaver dataSaver;
    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSaver = new DataSaver(requireContext());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        // Find the buttons by their IDs and set click listeners
        Button playButton = rootView.findViewById(R.id.btnPlay);
        Button howToPlayButton = rootView.findViewById(R.id.btnHowToPlay);
        Button shopButton = rootView.findViewById(R.id.btnShop);
        Button levelSelectButton = rootView.findViewById(R.id.btnLevelSelect);
        playButton.setOnClickListener(this::startGame);
        howToPlayButton.setOnClickListener(this::howToPlay);
        shopButton.setOnClickListener(this::toShop);
        levelSelectButton.setOnClickListener(this::selectLevel);

        moneyTextView = rootView.findViewById(R.id.moneyText);
        levelTextView = rootView.findViewById(R.id.levelText);
        if (GameManager.getInstance().getGame() != null){
            playButton.setText(R.string.contin);
        }
        updateDisplay();

        //mainTitleAnimation(rootView);

        return rootView;
    }

    private void selectLevel(View view) {
        NavHostFragment.findNavController(MenuFragment.this)
                .navigate(R.id.action_MenuFragment_to_levelSelectFragment);
    }

    private static void mainTitleAnimation(View rootView) {
        View menuTitle = rootView.findViewById(R.id.menuTitle);

        // Create a scaling animation
        Animation scaleAnimation = new ScaleAnimation(
                1.0f, 1.2f,  // Scale from 100% to 120%
                1.0f, 1.2f,  // Scale from 100% to 120%
                Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point at the center horizontally
                Animation.RELATIVE_TO_SELF, 1.0f); // Pivot point at the bottom vertically

        scaleAnimation.setDuration(1000); // Animation duration in milliseconds
        scaleAnimation.setRepeatCount(Animation.INFINITE); // Repeat the animation infinitely
        scaleAnimation.setRepeatMode(Animation.REVERSE); // Reverse the animation when it repeats

        menuTitle.startAnimation(scaleAnimation);
    }

    private void updateDisplay() {
        int level = dataSaver.loadCurrentLevel();
        if (GameManager.getInstance().getGame() != null){
            level = GameManager.getInstance().getGame().getLevel();
        }
        String levelText = "Current level: " + level;
        levelTextView.setText(levelText);
        String moneyText = "$ " + dataSaver.loadMoney() + " $";
        moneyTextView.setText(moneyText);
    }

    private void toShop(View view) {
        NavHostFragment.findNavController(MenuFragment.this)
                .navigate(R.id.action_MenuFragment_to_ShopFragment);
    }

    private void howToPlay(View view) {
        NavHostFragment.findNavController(MenuFragment.this)
                .navigate(R.id.action_MenuFragment_to_TutorialFragment);
    }

    public void startGame(View view) {
        NavHostFragment.findNavController(MenuFragment.this)
                .navigate(R.id.action_MenuFragment_to_GameFragment);
    }
}