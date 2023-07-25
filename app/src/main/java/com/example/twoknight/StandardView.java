package com.example.twoknight;

import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.twoknight.androidGUI.GUIConstants;
import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.*;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.StandardGame;

public class StandardView extends View {
    private final Game game;
    private float viewHeight;
    private float viewWidth;
    private float spacing;
    private float startX = -1; // Default initial touch coordinates
    private float startY = -1;
    private float boardSize;
    private float margin;
    private float tileSize;


    private final Paint textPaint;
    private float corner;

    public StandardView(Context context) {
        super(context);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = new Paint();
        textPaint.setColor(Color.GRAY);
    }
    public StandardView(Context context, AttributeSet attrs) {
        super(context);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = getWidth();
        viewHeight = getHeight();
        boardSize = (float) (Math.min(viewHeight, viewWidth)*0.90);
        margin = (float) ((Math.min(viewHeight, viewWidth)-boardSize)*0.5);
        tileSize = boardSize/5;
        spacing = tileSize/5;
        corner = boardSize/50;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw your game elements on the canvas
        //canvas.drawCircle(200, 200, 200, pincelAmerelo);
        DrawBoard(canvas);
        Log.d("MainActivity", "This is a debug message" + margin + "");
        Log.d("MainActivity", "numbers" + margin + " " + viewHeight + " " + viewWidth);

    }

    private void DrawBoard(Canvas canvas) {
        Tile[][] field = game.getField();
        canvas.drawRoundRect(margin, margin, margin+boardSize, margin+boardSize, corner, corner, GUIConstants.boardPaint);
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                drawTile(field, i,j, canvas);
            }
        }
    }

    private void drawTile(Tile[][] field, int i, int j, Canvas canvas) {
        if (field[i][j] != null) {
            Tile tile = field[i][j];
            int value = tile.getValue();
            if (value == 0) {
                GUIConstants.TilePaint.setColor(Color.BLACK);
            } else {
                GUIConstants.TilePaint.setColor(
                        Color.parseColor(
                                GUIConstants.colorTable[(int) (Math.log(Math.abs(value)) / Math.log(2))]));
                drawTileRect(i, j, canvas, value); //draws tile with value
            }
        }
        else {
            GUIConstants.TilePaint.setColor(Color.GRAY);
            drawTileRect(i, j, canvas); //draws tile without value. Color is determined before fcn is called
        }

    }

    private void drawTileRect(int i, int j, Canvas canvas, int value) {
        float xu = margin + (i +1)*spacing + i *tileSize;
        float yu = margin + (j +1)*spacing + j *tileSize;
        float xd = xu + tileSize;
        float yd = yu + tileSize;
        canvas.drawRoundRect(xu, yu, xd, yd,corner/4,corner/4, GUIConstants.TilePaint);
        if (value > 0){
            textPaint.setTextSize(tileSize / 2);
            Rect textBounds = new Rect();
            textPaint.getTextBounds(String.valueOf(value), 0, String.valueOf(value).length(), textBounds);
            // Calculate the position to draw the number in the center of the tile
            float textX = xu + tileSize/2 - textBounds.width() / 2;
            float textY = yu + tileSize/2 + textBounds.height() / 2;
            canvas.drawText(""+value, textX, textY, textPaint);
        }
    }

    private void drawTileRect(int i, int j, Canvas canvas) {
        drawTileRect(i,j,canvas,-1);
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
                if (Math.abs(deltaY) > Math.abs(deltaX)) {
                    // Horizontal swipe (left or right)
                    if (deltaY > 0) {
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
                    if (deltaX > 0) {
                        // Down swipe
                        // Update game logic to move tiles down
                        game.endTurn(KeyEvent.VK_DOWN);
                    } else {
                        // Up swipe
                        // Update game logic to move tiles up
                        game.endTurn(KeyEvent.VK_UP);
                    }
                }
                invalidate();
                break;
        }

        // Return true to indicate that the touch event is handled
        return true;
    }
}
