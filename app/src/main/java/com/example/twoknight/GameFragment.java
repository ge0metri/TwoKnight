package com.example.twoknight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.Game;
import com.example.twoknight.framework.GameListener;
import com.example.twoknight.standard.StandardGame;

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
    private Game standardGame = new StandardGame(new StandardGameFactory(1));
    private StandardView gameView;
    private DataSaver dataSaver;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        // Initialize and add your GameView here
        StandardView gameView = rootView.findViewById(R.id.gameView);
        // Customize and configure your GameView as needed
        standardGame = new StandardGame(new StandardGameFactory(dataSaver.loadCurrentLevel()));
        standardGame.setGameListener(this);
        gameView.addGame(standardGame);
        return rootView;
    }

    private void saveGame(Game game){
        dataSaver.saveCurrentLevel(game.getLevel()+1);
        int currentMoney = dataSaver.loadMoney();
        dataSaver.saveMoney(currentMoney+1);
    }

    @Override
    public void onLevelCleared(Game game) {
        saveGame(game);
    }
}