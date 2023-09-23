package com.example.twoknight;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.twoknight.strategy.DifficultyHandler;
import com.example.twoknight.strategy.StandardDifficultyHandler;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void tileValueIsCorrect() {
        DifficultyHandler difficultyHandler = new StandardDifficultyHandler(1, new int[]{1});
        int sum = 0;
        for (int i = 0; i < 100; i++){
            int val = difficultyHandler.getTileValue(); //expected = 2*0.9 + 4*0.1 = 2.2
            sum += val;
        }
        boolean isBetween = 100 < sum && sum < 340;
        assertTrue(isBetween);
    }
    @Test
    public void tileValueIsCorrectLater() {
        DifficultyHandler difficultyHandler = new StandardDifficultyHandler(1, new int[]{9});
        int sum = 0;
        for (int i = 0; i < 100; i++){
            int val = difficultyHandler.getTileValue(); //expected = 4*0.9 + 8*0.1 = 4.4
            sum += val;
        }
        boolean isBetween = 320 < sum && sum < 580;
        //System.out.println(sum);
        assertTrue(isBetween);
    }
}