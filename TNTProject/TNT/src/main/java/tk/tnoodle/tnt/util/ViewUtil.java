package tk.tnoodle.tnt.util;

import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;

import tk.tnoodle.tnt.R;

public class ViewUtil {
    public static boolean isEventInView(View view, MotionEvent event) {
        int rx = Math.round(event.getRawX());
        int ry = Math.round(event.getRawY());
        int[] l = new int[2];
        view.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = view.getWidth();
        int h = view.getHeight();

        return !(rx < x || rx > x + w || ry < y || ry > y + h);
    }

    public static boolean shouldCancel(Resources res, MotionEvent event, float startX, float startY) {
        float endX = event.getRawX();
        float endY = event.getRawY();
        double dist = Math.sqrt((endX - startX) * (endX - startX) + (endY - startY) * (endY - startY));

        return dist > res.getDimensionPixelSize(R.dimen.cancel_dist);
    }
}
