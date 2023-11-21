package com.example.twoknight;

import static java.lang.Math.min;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.twoknight.androidGUI.GUIConstants;
import com.example.twoknight.factory.StandardGameFactory;
import com.example.twoknight.framework.*;
import com.example.twoknight.standard.KeyEvent;
import com.example.twoknight.standard.StandardGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StandardView extends View {
    private Paint laserPaint;
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
    private final Drawable heroScard = AppCompatResources.getDrawable(getContext(), R.drawable.ic_hero_scard);
    private final Drawable heroShield = AppCompatResources.getDrawable(getContext(), R.drawable.ic_shield);
    private final Drawable criticalImage = AppCompatResources.getDrawable(getContext(), R.drawable.ic_x2);
    int heroColor = ContextCompat.getColor(getContext(), R.color.hero_col);
    int hurtColor = ContextCompat.getColor(getContext(), R.color.hero_stcolor);
    int emptyColor = ContextCompat.getColor(getContext(), R.color.empty_col);
    private float healthBarHeight;
    private float yOffSet;
    private RectF pointerRect;
    private boolean pointerMode = false;
    private final ArrayList<int[]> newRects = new ArrayList<>();
    private float newRectScale = 0;
    private final String newRectHolder = "newRectScale";
    private final String moveHolder = "moveScale";
    private float moveScale = 0;
    private final ArrayList<int[]> moveRects = new ArrayList<>();
    private int[] laserPosition;

    public StandardView(Context context) {
        super(context);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = getPaint();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }
    public StandardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //game = new AlternatingGame();
        game = new StandardGame(new StandardGameFactory(1));
        textPaint = getPaint();
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @NonNull
    private Paint getPaint() {
        final Paint textPaint;
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        laserPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        laserPaint.setColor(Color.RED);
        laserPaint.setStrokeWidth(15);
        laserPaint.setStyle(Paint.Style.STROKE);
        GUIConstants.ShieldPaint.setColor(Color.rgb(248, 248, 80));
        return textPaint;
    }

    public void addGame(Game game){
        this.game = game;
        textPaint.setAntiAlias(true);
    }

    public Game getGame(){
        return game;
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
                endTurn(KeyEvent.VK_LEFT);
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT:
                endTurn(KeyEvent.VK_RIGHT);
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN:
                endTurn(KeyEvent.VK_DOWN);
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_UP:
                endTurn(KeyEvent.VK_UP);
                break;
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
        drawTiles(canvas);
        drawHealth(canvas);
        drawNewRects(canvas);
        drawTileMove(canvas);
        if (laserPosition != null){
            drawLaser(canvas);
        }
        //drawPointer(canvas);
    }

    private void drawLaser(Canvas canvas) {
        int i = laserPosition[0];
        int j = laserPosition[1];
        RectF rectF = getTileRect(i,j);
        canvas.drawCircle(rectF.centerX(), rectF.centerY(), rectF.height()/2, laserPaint);
        laserPaint.setStyle(Paint.Style.FILL);
        float stage = 1-((3-getGame().getDifficultyHandler().getChargeTime())/3f);
        canvas.drawText(stage+"", rectF.left, rectF.top, textPaint);
        canvas.drawRoundRect(
                rectF.left+tileSize*0.46f,
                rectF.top+stage*rectF.height(),
                rectF.right-tileSize*0.46f,
                rectF.bottom-stage*rectF.height(),
                corner/4,
                corner/4,
                laserPaint
        );
        canvas.drawRoundRect(
                rectF.left+stage*rectF.width(),
                rectF.top +tileSize*0.46f,
                rectF.right-stage*rectF.width(),
                rectF.bottom - tileSize*0.46f,
                corner/4,
                corner/4,
                laserPaint
        );
        laserPaint.setStyle(Paint.Style.STROKE);
    }

    private void drawTileMove(Canvas canvas) {
        for (int[] coordinates :
                moveRects) {
            int iStart = coordinates[0];
            int jStart = coordinates[1];
            int iEnd = coordinates[2];
            int jEnd = coordinates[3];
            int value = coordinates[4];
            setTileColor(value);
            drawTileRect(iStart, jStart, iEnd, jEnd, canvas, value);
        }
    }

    private void drawNewRects(Canvas canvas) {
        for (int[] out : newRects){
            RectF rectTarget = getTileRect(out[0], out[1]);
            RectF rect = new RectF(
                    rectTarget.left*newRectScale + (1-newRectScale)*rectTarget.centerX(),
                    rectTarget.top*newRectScale + (1-newRectScale)*rectTarget.centerY(),
                    rectTarget.right*newRectScale + (1-newRectScale)*rectTarget.centerX(),
                    rectTarget.bottom*newRectScale + (1-newRectScale)*rectTarget.centerY()
            );
            setTileColor(game.getField()[out[0]][out[1]].getValue());
            canvas.drawRoundRect(rect, corner/4, corner/4, GUIConstants.TilePaint);
        }
    }

    private void drawPointer(Canvas canvas) {
        if (!pointerMode){
            return;
        }
        GUIConstants.TilePaint.setColor(Color.argb(100, 230, 230, 120));
        if (pointerRect != null){
            canvas.drawRoundRect(pointerRect, corner/4, corner/4, GUIConstants.TilePaint);
        }
    }

    private void drawHealth(Canvas canvas) {
        GUIConstants.TilePaint.setColor(Color.RED);
        float healthBar = boardSize * game.getHero().getHealth() / game.getHero().getValue();
        if (healthBar<0){healthBar=0;}
        drawShieldBar(canvas, healthBar);
        canvas.drawRoundRect(margin,
                margin,
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
        if (healthBar > boardSize*0.2){
            Rect textBounds = new Rect();
            String health = game.getHero().getHealth() + "";
            textPaint.getTextBounds(health, 0, health.length(), textBounds);
            // Calculate the position to draw the number in the center of the tile
            float textX = (float) (margin + healthBar*0.5 - textBounds.width() / 2);
            float textY = (float) (margin + healthBarHeight*0.5 + textBounds.height() / 2);
            canvas.drawText(health, textX, textY, textPaint);
        }
    }

    private void drawShieldBar(Canvas canvas, float healthBar) {
        float shieldCount = game.getDifficultyHandler().getShieldCounter();
        float shieldCD = game.getDifficultyHandler().getShieldCD();
        float shieldBar = shieldCount <= 0 ? healthBar : (healthBar)*shieldCount/shieldCD;
        float addedPad = margin/4;
        canvas.drawRoundRect(margin - addedPad,
                margin - addedPad,
                margin+shieldBar + addedPad,
                margin + healthBarHeight + addedPad,
                corner,
                corner,
                GUIConstants.ShieldPaint);
    }

    private void drawBoard(Canvas canvas) {
        canvas.drawRoundRect(
                margin,
                yOffSet,
                margin + boardSize,
                yOffSet + boardSize,
                corner,
                corner,
                GUIConstants.boardPaint);
    }

    private void drawTiles(Canvas canvas) {
        Tile[][] field = game.getField();
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field[i].length; j++){
                drawTile(field, i,j, canvas);
            }
        }
    }

    private boolean isDrawingRect(int i, int j) {
        for (int[] pair : newRects) {
            if (pair[0] == i && pair[1] == j) {return true;}
        }
        for (int[] coordinates: moveRects) {
            if (coordinates[2] == i && coordinates[3] == j){
                return true;
            }
        }
        return false;
    }

    private void drawTile(Tile[][] field, int i, int j, Canvas canvas) {
        if (field[i][j] == null || isDrawingRect(i,j)) {
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
        } else if (tile instanceof ImmovableTile) {
            GUIConstants.TilePaint.setColor(Color.GRAY);
            drawTileRect(i, j, canvas, value);
        } else {
            setTileColor(value);
            drawTileRect(i, j, canvas, value); //draws tile with value
        }
    }

    private static void setTileColor(int value) {
        GUIConstants.TilePaint.setColor(
                Color.parseColor(
                        GUIConstants.colorTable[(int) (Math.log(Math.abs(value)) / Math.log(2))]));
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

    private void drawTileRect(int iStart, int jStart, int iEnd, int jEnd, Canvas canvas, int value){
        RectF rectStart = getTileRect(iStart, jStart);
        RectF rectEnd = getTileRect(iEnd, jEnd);
        RectF interpolatedRect = new RectF(
                rectEnd.left * moveScale + (1-moveScale)*rectStart.left,
                rectEnd.top * moveScale + (1-moveScale)*rectStart.top,
                rectEnd.right * moveScale + (1-moveScale)*rectStart.right,
                rectEnd.bottom * moveScale + (1-moveScale)*rectStart.bottom
                );
        canvas.drawRoundRect(interpolatedRect,corner/4,corner/4, GUIConstants.TilePaint);
        if (value > 0){
            textPaint.setTextSize(tileSize / 2);
            drawTileValue(String.valueOf(value), interpolatedRect, canvas);
        }
    }

    private void drawTileValue(String value, RectF rect, Canvas canvas) {
        Rect textBounds = new Rect();
        textPaint.getTextBounds(value, 0, value.length(), textBounds);
        // Calculate the position to draw the number in the center of the tile
        float textX = rect.centerX() - textBounds.width() / 2f;
        float textY = rect.centerY() + textBounds.height() / 2f;
        canvas.drawText(value, textX, textY, textPaint);
    }

    @NonNull
    private RectF getTileRect(int i, int j) {
        float xu = margin + (i +1)*spacing + i *tileSize;
        float yu = yOffSet + (j +1)*spacing + j *tileSize;
        float xd = xu + tileSize;
        float yd = yu + tileSize;
        return new RectF(xu, yu, xd, yd);
    }

    private void drawTileRect(int i, int j, Canvas canvas) {
        drawTileRect(i,j,canvas,-1);
    }

    // Override onTouchEvent method to handle user input
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                animateBlock(event);
                break;
            case MotionEvent.ACTION_DOWN:
                // Store the initial touch coordinates
                startX = event.getX();
                startY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                pointerRect = null;
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
                        endTurn(KeyEvent.VK_DOWN);
                    } else {
                        // Left swipe
                        // Update game logic to move tiles to the left
                        endTurn(KeyEvent.VK_UP);
                    }
                } else {
                    // Vertical swipe (up or down)
                    if (deltaX > 0) {
                        // Down swipe
                        // Update game logic to move tiles down
                        endTurn(KeyEvent.VK_RIGHT);
                    } else {
                        // Up swipe
                        // Update game logic to move tiles up
                        endTurn(KeyEvent.VK_LEFT);
                    }
                }
                invalidate();
                break;
        }

        // Return true to indicate that the touch event is handled
        return true;
    }

    private void endTurn(int direction) {
        game.endTurn(direction);
        animateEndTurn();
    }

    private void animateEndTurn() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animateMove(), animateNewRect()); // Run animations simultaneously
        animatorSet.start();
    }

    public void onMove(int[] start, int[] end, int startValue) {
        int[] coordinates = {start[0], start[1], end[0], end[1], startValue};
        moveRects.add(coordinates);
    }
    private ValueAnimator animateMove() {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat(
                moveHolder,
                0f,
                1f
        );

        // 2
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(valuesHolder);
        animator.setDuration(80);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        // 3
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 4
                moveScale = (float) animation.getAnimatedValue(moveHolder);

                // 6
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                moveRects.clear();
                invalidate();
            }
        });

        // 7
        return animator;
    }

    private void animateBlock(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int i = (int) ((x-margin)/(boardSize/4));
        int j = (int) ((y - yOffSet)/(boardSize/4));
        boolean iIsInside = 0 <= i && i <= 3;
        boolean jIsInside = 0 <= j && j <= 3;
        if (y < yOffSet || y > yOffSet + boardSize || !iIsInside || !jIsInside) {
            pointerRect = null;
            invalidate();
            return;
        }
        RectF rectF = getTileRect(i,j);
        if (rectF.equals(pointerRect)){
            return;
        }
        invalidate();
        pointerRect = getTileRect(i,j);

    }

    public void usePower(int power) {
        game.endTurn(power);
        invalidate();
    }

    public void addTile(int[] out) {
        newRects.clear();
        newRects.add(out);
    }

    private ValueAnimator animateNewRect() {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat(
                newRectHolder,
                0f,
                1f
        );

        // 2
        ValueAnimator animator = new ValueAnimator();
        animator.setValues(valuesHolder);
        animator.setDuration(120);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        // 3
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 4
                newRectScale = (float) animation.getAnimatedValue(newRectHolder);

                // 6
                invalidate();
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                newRects.clear();
            }
        });

        // 7
        return animator;
    }

    public void onBeginLaser(int i, int j) {
        laserPosition = new int[]{i, j};
    }

    public void onFireLaser(int i, int j, boolean skipLaser) {
        laserPosition = null;
    }
}
