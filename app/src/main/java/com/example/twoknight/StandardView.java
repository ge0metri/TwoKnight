package com.example.twoknight;

import static java.lang.Math.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.twoknight.androidGUI.GUIConstants;
import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.*;
import com.example.twoknight.standard.GameConstants;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.StandardGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StandardView extends View {
    private Game game;
    private float viewHeight;
    private float viewWidth;
    private float spacing;
    private float startX = -1; // Default initial touch coordinates
    private float startY = -1;
    private float boardSize;
    private float margin;
    private float tileSize;

    Drawable heroSmug = AppCompatResources.getDrawable(getContext(), R.drawable.ic_hero_smug);
    private final Paint textPaint;
    private float corner;
    private Drawable heroScard = AppCompatResources.getDrawable(getContext(), R.drawable.ic_hero_scard);
    private Drawable heroShield = AppCompatResources.getDrawable(getContext(), R.drawable.ic_shield);
    private Drawable criticalImage = AppCompatResources.getDrawable(getContext(), R.drawable.ic_x2);
    int heroColor = ContextCompat.getColor(getContext(), R.color.hero_col);
    int hurtColor = ContextCompat.getColor(getContext(), R.color.hero_stcolor);
    int emptyColor = ContextCompat.getColor(getContext(), R.color.empty_col);
    private float healthBarHeight;
    private float yOffSet;

    public StandardView(Context context) {
        super(context);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }
    public StandardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }
    public void addGame(Game game){
        this.game = game;
        textPaint.setAntiAlias(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardSize = (float) (Math.min(h, w)*0.90);
        margin = (float) ((Math.min(h, w)-boardSize)*0.5);
        tileSize = boardSize/5;
        spacing = tileSize/5;
        corner = boardSize/50;
        healthBarHeight = tileSize/2;
        yOffSet = 2*margin + healthBarHeight;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int yourWidth = (int) (w *0.1);
            int yourHeightTop = (int) (h *0.2);
            int yourHeightBot = (int) (h *0.8);
            Rect leftRect = new Rect(0, yourHeightTop, yourWidth, yourHeightBot);
            Rect rightRect = new Rect(w - yourWidth, yourHeightTop, w, yourHeightBot);
            List<Rect> regions = new ArrayList<>(Arrays.asList(leftRect, rightRect));
            this.setSystemGestureExclusionRects(regions);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode){
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT:
                game.endTurn(KeyEvent.VK_LEFT);
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT:
                game.endTurn(KeyEvent.VK_RIGHT);
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN:
                game.endTurn(KeyEvent.VK_DOWN);
            case android.view.KeyEvent.KEYCODE_DPAD_UP:
                game.endTurn(KeyEvent.VK_UP);
        }
        invalidate();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw your game elements on the canvas
        //canvas.drawCircle(200, 200, 200, pincelAmerelo);
        drawBoard(canvas);
        drawHealth(canvas);
    }

    private void drawHealth(Canvas canvas) {
        GUIConstants.TilePaint.setColor(Color.RED);
        float healthBar = boardSize * game.getHero().getHealth() / game.getHero().getValue();
        if (healthBar<0){healthBar=0;}
        canvas.drawRoundRect(margin,
                (float) (1*margin),
                margin+healthBar,
                margin + healthBarHeight,
                corner,
                corner,
                GUIConstants.TilePaint);
        if (game.getHero().isVulnerable()){
            criticalImage.setBounds(new Rect((int) (margin+healthBar-0.75*tileSize),
                    (int) (0.5*margin),
                    (int) (margin+healthBar+0.25*tileSize),
                    (int) (margin + tileSize-0.5*margin)));
            criticalImage.draw(canvas);
        }
    }

    private void drawBoard(Canvas canvas) {
        Tile[][] field = game.getField();
        canvas.drawRoundRect(
                margin,
                yOffSet,
                margin + boardSize,
                yOffSet + boardSize,
                corner,
                corner,
                GUIConstants.boardPaint);
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                drawTile(field, i,j, canvas);
            }
        }
    }

    private void drawTile(Tile[][] field, int i, int j, Canvas canvas) {
        if (field[i][j] == null) {
            GUIConstants.TilePaint.setColor(emptyColor);
            drawTileRect(i, j, canvas); //draws tile without value. Color is determined before fcn is called
            return;
        }
        Tile tile = field[i][j];
        if (tile instanceof Hero) {
            Hero hero = (Hero) tile;
            drawHeroTile(hero, i,j, canvas);
            return;
        }

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

    private void drawHeroTile(Hero hero, int i, int j, Canvas canvas) {
        Drawable drawing = heroSmug;
        RectF bounds = getTileRect(i,j);
        GUIConstants.TilePaint.setColor(heroColor);
        if (hero.isVulnerable()){
            drawing = heroScard;
        }
        canvas.drawRoundRect(bounds, corner/4, corner/4, GUIConstants.TilePaint);
        drawing.setBounds(
                (int) bounds.left,
                (int) bounds.top,
                (int) bounds.right,
                (int) bounds.bottom
        );
        drawing.draw(canvas);
        if (hero.getShield() > 0){
            float scale = (float) 1.1;
            heroShield.setBounds(
                    (int) (bounds.centerX() - (bounds.width() * scale) / 2),
                    (int) (bounds.centerY() - (bounds.height() * scale) / 2),
                    (int) (bounds.centerX() + (bounds.width() * scale) / 2),
                    (int) (bounds.centerY() + (bounds.height() * scale) / 2));
            heroShield.draw(canvas);
            textPaint.setColor(Color.DKGRAY);
            drawTileValue(String.valueOf(hero.getShield()), bounds, canvas);
        }
    }

    private void drawTileRect(int i, int j, Canvas canvas, int value) {
        RectF rect = getTileRect(i, j);
        canvas.drawRoundRect(rect,corner/4,corner/4, GUIConstants.TilePaint);
        if (value > 0){
            textPaint.setTextSize(tileSize / 2);
            drawTileValue(String.valueOf(value), rect, canvas);
        }
    }

    private void drawTileValue(String value, RectF rect, Canvas canvas) {
        Rect textBounds = new Rect();
        textPaint.getTextBounds(value, 0, value.length(), textBounds);
        // Calculate the position to draw the number in the center of the tile
        float textX = rect.centerX() - textBounds.width() / 2;
        float textY = rect.centerY() + textBounds.height() / 2;
        canvas.drawText(value, textX, textY, textPaint);
    }

    @NonNull
    private RectF getTileRect(int i, int j) {
        float xu = margin + (i +1)*spacing + i *tileSize;
        float yu = yOffSet + (j +1)*spacing + j *tileSize;
        float xd = xu + tileSize;
        float yd = yu + tileSize;
        RectF rect = new RectF(xu, yu, xd, yd);
        return rect;
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
                        game.endTurn(KeyEvent.VK_DOWN);
                    } else {
                        // Left swipe
                        // Update game logic to move tiles to the left
                        game.endTurn(KeyEvent.VK_UP);
                    }
                } else {
                    // Vertical swipe (up or down)
                    if (deltaX > 0) {
                        // Down swipe
                        // Update game logic to move tiles down
                        game.endTurn(KeyEvent.VK_RIGHT);
                    } else {
                        // Up swipe
                        // Update game logic to move tiles up
                        game.endTurn(KeyEvent.VK_LEFT);
                    }
                }
                invalidate();
                break;
        }

        // Return true to indicate that the touch event is handled
        return true;
    }
}
