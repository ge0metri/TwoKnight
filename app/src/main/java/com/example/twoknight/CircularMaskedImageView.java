package com.example.twoknight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


public class CircularMaskedImageView extends androidx.appcompat.widget.AppCompatImageView {

    Path path = new Path();
    private int maxIndex = 0; // Set the percentage of the circle to be visible
    private int index = 0;

    public CircularMaskedImageView(Context context) {
        super(context);
    }

    public CircularMaskedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // Get the drawable from the ImageView
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        // Save the current state of the canvas
        int saveCount = canvas.saveLayer(0, 0, getWidth(), getHeight(), null);

        // Draw the original drawable
        //super.onDraw(canvas);
        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = Math.min(centerX, centerY);
        float innerRadius = radius*0.55f;
        float size = 5*(radius-innerRadius)/(maxIndex+2);
        for (int i = 0; i < index; i++) {
            Rect imageRect = getImageRect(getPositionX(i, innerRadius, centerX), getPositionY(i, innerRadius, centerY), size);
            drawable.setBounds(imageRect);
            drawable.draw(canvas);
        }

        // Create a circular mask


        // Restore the canvas to its original state
        canvas.restoreToCount(saveCount);
    }

    private float getPositionX(int index, float radius, float centerX){
        return centerX + radius*((float)Math.cos(-Math.PI/2f + 2*index*Math.PI/maxIndex));
    }
    private float getPositionY(int index, float radius, float centerY){
        return centerY + radius*((float)Math.sin(-Math.PI/2f + 2*index*Math.PI/maxIndex));
    }

    private Rect getImageRect(float xCenter, float yCenter, float size) {
        return new Rect(
                (int) (xCenter-size),
                (int) (yCenter - size),
                (int) (xCenter + size),
                (int) (yCenter + size));
    }

    // Set the percentage of the circle to be visible
    public void setCooldown(int maxIndex, int index) {
        if (maxIndex >= 0 && index >= 0) {
            this.maxIndex = maxIndex;
            this.index = index;
            invalidate(); // Trigger a redraw
        }
    }
}