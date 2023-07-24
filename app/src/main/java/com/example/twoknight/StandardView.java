package com.example.twoknight;

import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.example.twoknight.androidGUI.GUIConstants;
import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.*;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.StandardGame;

public class StandardView extends View {
    private final Game game;
    private final float viewHeight;
    private final float viewWidth;
    private final float spacing;
    private float startX = -1; // Default initial touch coordinates
    private float startY = -1;
    private float boardSize;
    private float margin;
    private float tileSize;


    public StandardView(Context context) {
        super(context);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        viewWidth = getWidth();
        viewHeight = getHeight();
        boardSize = (float) (Math.min(viewHeight, viewWidth)*0.90);
        margin = (float) ((Math.min(viewHeight, viewWidth)-boardSize)*0.5);
        tileSize = boardSize/5;
        spacing = tileSize/5;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw your game elements on the canvas
        DrawBoard(canvas);
    }

    private void DrawBoard(Canvas canvas) {
        Tile[][] field = game.getField();
        canvas.drawRoundRect(margin, margin, margin+boardSize, margin+boardSize, 5,5, GUIConstants.boardPaint);
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                if (field[i][j] != null){
                drawTile(field, i,j, canvas);}
            }
        }
    }

    private void drawTile(Tile[][] field, int i, int j, Canvas canvas) {
        Tile tile = field[i][j];
        int value = tile.getValue();
        if (value == 0){
            GUIConstants.TilePaint.setColor(Color.BLACK);
        }
        else {
            GUIConstants.TilePaint.setColor(Color.parseColor(GUIConstants.colorTable[(int) (Math.log(Math.abs(value)) / Math.log(2))]));
        }
        float xu = margin + (i+1)*spacing + i*tileSize;
        float yu = margin + (j+1)*spacing + j*tileSize;
        float xd = xu + tileSize;
        float yd = yu + tileSize;
        canvas.drawRoundRect(xu, yu, xd, yd,2,2, GUIConstants.TilePaint);
    }

    // Override onTouchEvent method to handle user input
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Store the initial touch coordinates
                startX = event.getX();
                startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                // Store the final touch coordinates
                float endX = event.getX();
                float endY = event.getY();

                // Calculate the distance in the X and Y direction
                float deltaX = endX - startX;
                float deltaY = endY - startY;

                // Determine the direction of the swipe based on the distance
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    // Horizontal swipe (left or right)
                    if (deltaX > 0) {
                        // Right swipe
                        // Update game logic to move tiles to the right
                        game.endTurn(KeyEvent.VK_RIGHT);
                    } else {
                        // Left swipe
                        // Update game logic to move tiles to the left
                        game.endTurn(KeyEvent.VK_LEFT);
                    }
                } else {
                    // Vertical swipe (up or down)
                    if (deltaY > 0) {
                        // Down swipe
                        // Update game logic to move tiles down
                        game.endTurn(KeyEvent.VK_DOWN);
                    } else {
                        // Up swipe
                        // Update game logic to move tiles up
                        game.endTurn(KeyEvent.VK_UP);
                    }
                }
                break;
        }
        invalidate();
        // Return true to indicate that the touch event is handled
        return true;
    }
}
