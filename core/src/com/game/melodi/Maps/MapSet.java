package com.game.melodi.Maps;

/**
 * Created by Paradox on 5/2/2017.
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Data type containing all beatmaps in a beatmap set.
 */

public class MapSet implements Iterable<LevelMap> {
    /** List of associated beatmaps. */
    private final ArrayList<LevelMap> maps;

    /**
     * Constructor.
     * @param maps the beatmaps in this set
     */
    public MapSet(ArrayList<LevelMap> maps) {
        this.maps = maps;
    }

    /**
     * Returns the number of elements.
     */
    public int size() { return maps.size(); }

    /**
     * Returns the beatmap at the given index.
     * @param index the beatmap index
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public LevelMap get(int index) { return maps.get(index); }

    /**
     * Removes the beatmap at the given index.
     * @param index the beatmap index
     * @return the removed beatmap
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public LevelMap remove(int index) { return maps.remove(index); }

    @Override
    public Iterator<LevelMap> iterator() { return maps.iterator(); }

    /**
            * Returns an array of strings containing beatmap information.
            * <ul>
	 * <li>0: {Artist} - {Title} [{Version}]
            * <li>1: Mapped by {Creator}
	 * <li>2: Length: {}  BPM: {}  Objects: {}
	 * <li>3: Circles: {}  Sliders: {}  Spinners: {}
	 * <li>4: CS:{} HP:{} AR:{} OD:{} Stars:{}
	 * </ul>
            * @param index the beatmap index
	 * @throws IndexOutOfBoundsException if the index is out of range
	 */
    public String[] getInfo(int index) {
        LevelMap map = maps.get(index);
        //float speedModifier = GameMod.getSpeedMultiplier();
        long endTime = (long) (map.endTime);
        int bpmMin = (int) (map.bpmMin);
        int bpmMax = (int) (map.bpmMax);
        NumberFormat nf = new DecimalFormat("##.#");
        String[] info = new String[5];
        info[0] = map.toString();
        //info[1] = String.format("Mapped by %s", map.creator);
        info[2] = String.format("Length: %d:%02d  BPM: %s  Objects: %d",
                TimeUnit.MILLISECONDS.toMinutes(endTime),
                TimeUnit.MILLISECONDS.toSeconds(endTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime)),
                (bpmMax <= 0) ? "--" : ((bpmMin == bpmMax) ? bpmMin : String.format("%d-%d", bpmMin, bpmMax)),
                (map.flyObjectCircle + map.flyObjectSPoly + map.flyObjectSquare));
        info[3] = String.format("Circles: %d  Poly: %d  Square: %d",
                map.flyObjectCircle, map.flyObjectSPoly, map.flyObjectSquare);
        /*info[4] = String.format("CS:%s HP:%s AR:%s OD:%s%s",
                nf.format(Math.min(beatmap.circleSize * multiplier, 10f)),
                nf.format(Math.min(beatmap.HPDrainRate * multiplier, 10f)),
                nf.format(Math.min(beatmap.approachRate * multiplier, 10f)),
                nf.format(Math.min(beatmap.overallDifficulty * multiplier, 10f)),
                (beatmap.starRating >= 0) ? String.format(" Stars:%.2f", beatmap.starRating) : "");*/
        return info;
    }

    /**
     * Returns a formatted string for the beatmap set:
     * "Artist - Title"
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        LevelMap map = maps.get(0);
        return String.format("%s - %s", map.getTitle());
    }

    /**
     * Checks whether the beatmap set matches a given search query.
     * @param query the search term
     * @return true if title, artist, creator, source, version, or tag matches query
     */
    public boolean matches(String query) {
        // search: title, artist, creator, source, version, tags (first beatmap)
        LevelMap map = maps.get(0);
        if (map.title.toLowerCase().contains(query) ||
                map.titleUnicode.toLowerCase().contains(query) ||
                map.artist.toLowerCase().contains(query) ||
                map.artistUnicode.toLowerCase().contains(query) ||
                //map.creator.toLowerCase().contains(query) ||
                map.source.toLowerCase().contains(query) ||
                //map.version.toLowerCase().contains(query) ||
                map.tags.contains(query))
            return true;

        return false;
    }



}
