package tk.tnoodle.tnt.util;

import android.support.v4.app.Fragment;

import tk.tnoodle.tnt.R;

public class TimeUtil {
    public static String getString(Fragment fragment, long millis) {
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        millis = millis % 1000;

        if (minutes == 0) {
            return fragment.getString(R.string.timer_in_seconds, seconds, millis / 10);
        } else {
            return fragment.getString(R.string.timer_in_minutes, minutes, seconds, millis / 10);
        }
    }
}
