package tk.tnoodle.tnt;

import android.os.AsyncTask;
import android.util.Log;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.svglite.Svg;

public abstract class ScrambleTask extends AsyncTask<Puzzle, Void, ScrambleAndSvg> {
    private static final String TAG = ScrambleTask.class.getName();

    public ScrambleTask() { }

    private Exception exception;
    protected ScrambleAndSvg doInBackground(Puzzle... puzzles) {
        try {
            assert puzzles.length == 1;
            Puzzle puzzle = puzzles[0];
            String scramble = puzzle.generateScramble();
            Svg svg = puzzle.drawScramble(scramble, null);
            return new ScrambleAndSvg(scramble, svg);
        } catch(Exception e) {
            this.exception = e;
            return null;
        }
    }

    protected void onCancelled(ScrambleAndSvg scrambleAndSvg) {
        if(exception != null) {
            Log.w(TAG, exception);
        }
    }

    protected abstract void onPostExecute(ScrambleAndSvg scrambleAndSvg);
}
