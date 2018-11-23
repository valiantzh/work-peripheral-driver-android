package com.dcdz.drivers.cv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Vector;

public class TrackingOverlay extends OverlayView {

    private Vector<Box> tracking_object;
    private int rotation;
    private int camereWidth;
    private int cameraHeight;
    public TrackingOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setARGB(0xff, 0xff, 0, 0);
        canvas.drawText("(0, 0)", 0, - p.ascent(), p);
        Log.e("trackingOverlay", String.format("canvas width: %d, height: %d", canvas.getWidth(), canvas.getHeight()));
        float scale = (float)Math.max(canvas.getWidth() / (float)camereWidth, canvas.getWidth() / (float)cameraHeight);
        if(tracking_object != null) {
            //canvas.scale((float)( -800. / canvas.getWidth()), (float)(600. / canvas.getHeight()), 0, 0);
            canvas.scale(-1, 1, canvas.getWidth() * 0.5f, canvas.getHeight() * 0.5f);
            canvas.scale(scale, scale);
            for (Box box : tracking_object) {
                canvas.drawRect(new Rect(box.left(), box.top(), box.right(), box.bottom()), p);
            }
        }
        super.draw(canvas);
    }

    public synchronized void setTrackingResults(final Vector<Box> results, final int rotation) {
        tracking_object = results;
        this.rotation = rotation;
    }

    public synchronized void setCameraSize(final int width, final int height) {
        cameraHeight = height;
        camereWidth = width;
    }


}
