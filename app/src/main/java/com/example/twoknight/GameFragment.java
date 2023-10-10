package com.example.twoknight;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;

import com.example.twoknight.framework.Game;
import com.example.twoknight.framework.GameListener;
import com.example.twoknight.framework.GameState;
import com.example.twoknight.standard.GameConstants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment implements GameListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Game standardGame;
    private StandardView gameView;
    private DataSaver dataSaver;
    AnimatorSet animatorSet;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataSaver = new DataSaver(requireContext());
/*        OnBackPressedCallback callback = new OnBackPressedCallback(true *//* enabled by default *//*) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        gameView = rootView.findViewById(R.id.gameView);

        readyScreenShake(gameView);

        FloatingActionButton clearFieldPower = rootView.findViewById(R.id.clearPowerBtn);
        unFocus(clearFieldPower);
        clearFieldPower.setOnClickListener(this::clearPower);
        FloatingActionButton spawnLuckPower = rootView.findViewById(R.id.spawnLuckBtn);
        unFocus(spawnLuckPower);
        spawnLuckPower.setOnClickListener(this::spawnLuck);
        FloatingActionButton pausePower = rootView.findViewById(R.id.pausePower);
        unFocus(pausePower);
        pausePower.setOnClickListener(this::pausePower);
/*
        FloatingActionButton btn4 = rootView.findViewById(R.id.fab4);
        unFocus(btn4);
        FloatingActionButton btn6 = rootView.findViewById(R.id.fab6);
        unFocus(btn6);
        FloatingActionButton btn8 = rootView.findViewById(R.id.fab8);
        unFocus(btn8);
*/
        // Customize and configure your GameView as needed
        if (GameManager.getInstance().getGame() == null){
            GameManager.getInstance().setGame(dataSaver.loadCurrentLevel(), dataSaver.loadBoughtItems());
        }
        GameManager.getInstance().getGame().setGameListener(this);
        gameView.addGame(GameManager.getInstance().getGame());
        return rootView;
    }

    private void pausePower(View view) {
        gameView.usePower(GameConstants.PAUSE_POWER);
    }

    private void readyScreenShake(View view) {
        ObjectAnimator shakeAnimatorX = ObjectAnimator.ofFloat(view, "translationX", 0f, 10f);
        ObjectAnimator shakeAnimatorY = ObjectAnimator.ofFloat(view, "translationY", 0f, 10f);

// Set the duration and interpolator for the animation
        shakeAnimatorX.setDuration(90);  // Adjust the duration as needed
        shakeAnimatorY.setDuration(90);  // Adjust the duration as needed
        shakeAnimatorX.setInterpolator(new CycleInterpolator(4));  // Number of shakes
        shakeAnimatorY.setInterpolator(new CycleInterpolator(5));  // Number of shakes

// Combine the X and Y animations

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(shakeAnimatorX, shakeAnimatorY);
// Optional: Add a listener to reset the translation when the animation ends
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setTranslationX(0f);
                view.setTranslationY(0f);
            }
        });
    }

    private void spawnLuck(View view) {
        gameView.usePower(GameConstants.SPAWN_LUCK);
    }

    private void clearPower(View view) {
        gameView.usePower(GameConstants.CLEAR_POWER);
    }

    private static void unFocus(FloatingActionButton btn) {
        btn.setFocusable(false);
        btn.setFocusableInTouchMode(false);
    }

    private void saveGame(Game game, GameState status){
        int currentMoney = dataSaver.loadMoney();
        if (status == GameState.LOSER){
            return;
        }
        dataSaver.saveMoney(currentMoney+1);
        if (game.getLevel() < dataSaver.loadCurrentLevel()){return;}
        dataSaver.saveCurrentLevel(game.getLevel()+1);
    }

    @Override
    public void onLevelCleared(Game game, GameState status) {
        saveGame(game, status);
        int destination = R.id.action_GameFragment_to_EndScreenFragment;
        if (status == GameState.LOSER){
            destination = R.id.action_GameFragment_to_LossScreenFragment;
        }
        GameManager.getInstance().deleteGame();
        NavHostFragment.findNavController(GameFragment.this).navigate(destination);
    }

    @Override
    public void onPowerUse(boolean powerUsed){
        if (!powerUsed){
            showSnackbar(gameView, "How do not have any more charges");
        }
    }

    @Override
    public void onHighDamage() {
        animatorSet.start();
    }

    @Override
    public void addTile(int[] out) {
        gameView.addTile(out);
    }

    @Override
    public void onMove(int[] start, int[] end, int startValue) {
        gameView.onMove(start, end, startValue);
    }

    @Override
    public void onBeginLaser(int i, int j) {
        gameView.onBeginLaser(i,j);
    }

    @Override
    public void onFireLaser(int i, int j, boolean skipLaser) {
        gameView.onFireLaser(i,j, skipLaser);
    }

    private void showSnackbar(View button, String message) {
        Snackbar snackbar = Snackbar.make(button, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}