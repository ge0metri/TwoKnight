package com.example.twoknight;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.twoknight.databinding.FragmentLevelSelectBinding;
import com.example.twoknight.strategy.DifficultyHandler;
import com.example.twoknight.strategy.StandardDifficultyHandler;
import com.google.android.material.snackbar.Snackbar;

import java.text.CollationElementIterator;

public class LevelSelectFragment extends Fragment {


    private FragmentLevelSelectBinding binding;
    private DataSaver dataSaver;
    private TextView levelDescription;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        dataSaver = new DataSaver(requireContext());
        binding = FragmentLevelSelectBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final EditText levelSelectEditText = binding.editTextNumber;
        String LevelStart = "" + dataSaver.loadCurrentLevel();
        levelSelectEditText.setText(LevelStart);
        final Button loginButton = binding.acceptbtn;
        Button plusButton = binding.plusButton;
        Button minusButton = binding.minusButton;
        TextView selectTitle = binding.selecttitle;
        levelDescription = binding.leveldiscripton;
        updateText(levelSelectEditText);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = "" + (Integer.parseInt(levelSelectEditText.getText().toString())+1);
                levelSelectEditText.setText(level);
                checkLevel(levelSelectEditText, loginButton);
            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String level = "" + (Integer.parseInt(levelSelectEditText.getText().toString())-1);
                levelSelectEditText.setText(level);
                checkLevel(levelSelectEditText, loginButton);
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkLevel(levelSelectEditText, loginButton);
            }
        };
        levelSelectEditText.addTextChangedListener(afterTextChangedListener);
        levelSelectEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    // User clicked "Done" or pressed Enter
                    // Add your code here to handle the action

                    // Unfocus the EditText (remove focus)
                    levelSelectEditText.clearFocus();
                    return false; // Consume the event
                }
                return false; // Return false to allow further processing
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGame(levelSelectEditText);
                NavHostFragment.findNavController(LevelSelectFragment.this)
                        .navigate(R.id.action_levelSelectFragment_to_MenuFragment);
            }
        });
    }


    private void addGame(EditText levelSelectEditText) {
        int level;
        try {
            level = Integer.parseInt(levelSelectEditText.getText().toString());
        } catch (NumberFormatException e) {
            return;
        }
        GameManager.getInstance().setGame(level, dataSaver.loadBoughtItems());
    }

    private void checkLevel(EditText levelSelectEditText, Button loginButton) {
        if (levelSelectEditText.getText().length() < 1){
            loginButton.setEnabled(false);
            return;
        }
        loginButton.setEnabled(true);
        int inputLevel = Integer.parseInt(levelSelectEditText.getText().toString());
        int maxLevel = dataSaver.loadCurrentLevel();
        if (inputLevel < 1){
            String LevelStart = "" + 1;
            levelSelectEditText.setText(LevelStart);
            showSnackbar(levelSelectEditText, "The lowest level is level 1");
        }
        if (inputLevel > maxLevel){
            String LevelStart = "" + maxLevel;
            levelSelectEditText.setText(LevelStart);
            showSnackbar(levelSelectEditText, "You have only reached level " + maxLevel);
        }
        updateText(levelSelectEditText);
    }

    private void updateText(EditText levelSelectEditText) {
        int level = Integer.parseInt(levelSelectEditText.getText().toString());
        DifficultyHandler difficultyHandler = new StandardDifficultyHandler(level);
        int shield = difficultyHandler.getMaxShield();
        String s = "\n\n";
        if (difficultyHandler.isBlocksNotLaser()){
            s = "\n And blocks will spawn that can be \n destroyed by smashing them with a tile ";
        }else {
            s = "\n And a laser will target \n the largest tile ";
        }
        levelDescription.setText(getString(R.string.leveldesc1, shield, s));
    }

    private void showSnackbar(View button, String message) {
        Snackbar snackbar = Snackbar.make(button, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}