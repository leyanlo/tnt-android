package tk.tnoodle.tnt;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.scrambles.PuzzlePlugins;
import net.gnehzr.tnoodle.utils.BadLazyClassDescriptionException;
import net.gnehzr.tnoodle.utils.LazyInstantiator;
import net.gnehzr.tnoodle.utils.LazyInstantiatorException;

import java.io.IOException;
import java.util.SortedMap;

public enum Puzzles {
    SQ1 ("sq1"),
    SKEWB ("skewb"),
    PYRAM ("pyram"),
    MINX ("minx"),
    CLOCK ("clock"),
    SEVEN ("777"),
    SIX ("666"),
    FIVE ("555"),
    FOUR ("444fast"),
    THREE ("333"),
    TWO ("222");

    private String id;
    private static SortedMap<String, LazyInstantiator<Puzzle>> scramblers;

    Puzzles(String id) {
        this.id = id;
    }

    private static SortedMap<String, LazyInstantiator<Puzzle>> getScramblers() {
        if (scramblers == null) {
            try {
                scramblers = PuzzlePlugins.getScramblers();
            } catch (BadLazyClassDescriptionException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return scramblers;
    }

    public Puzzle getPuzzle() {
        LazyInstantiator<Puzzle> puzzle = getScramblers().get(id);
        try {
            return puzzle.cachedInstance();
        } catch (LazyInstantiatorException e) {
            e.printStackTrace();
            return null;
        }
    }
}
