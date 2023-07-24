package com.example.twoknight.androidGUI;


import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;


public class GUIConstants {

    public static Paint boardPaint = new Paint();
    public static Paint TilePaint = new Paint();
    public static final String[] colorTable = {
            "#701710", "#FFE4C3", "#fff4d3",
            "#ffdac3", "#e7b08e", "#e7bf8e",
            "#ffc4c3", "#E7948e", "#be7e56",
            "#be5e56", "#9c3931", "#701710",
            "#A4544D", "#6E2A23", "#420D09"};

    public GUIConstants() {
        boardPaint.setColor(Color.GRAY);

    }
}
