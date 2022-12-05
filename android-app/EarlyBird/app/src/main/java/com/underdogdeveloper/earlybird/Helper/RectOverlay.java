package com.underdogdeveloper.earlybird.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int rectColor= Color.GREEN;
    private float rectStrokeWidth=4.0f;
    private Paint paint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;

    public RectOverlay(GraphicOverlay overlay, Rect rect) {
        super(overlay);
        paint=new Paint();
        paint.setColor(rectColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(rectStrokeWidth);
        this.graphicOverlay=overlay;
        this.rect=rect;
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        RectF rectF=new RectF(rect);
        rectF.left=translateX(rectF.left);
        rectF.right=translateX(rectF.right);
        rectF.top=translateX(rectF.top);
        rectF.bottom=translateX(rectF.bottom);
        canvas.drawRect(rectF,paint);
    }
}
