package com.game.melodi.Maps;

/**
 * Created by Paradox on 5/2/2017.
 */

/**
 * Node in an MapSetList representing a map set.
 */

public class MapNode {
    /** The associated beatmap set. */
    private final MapSet mapSet;

    /** Index of the selected beatmap (-1 if not focused). */
    public int mapIndex = -1;

    /** Index of this node. */
    public int index = 0;

    /** Links to other nodes. */
    public MapNode prev, next;

    /**
     * Constructor.
     * @param mapSet the map set
     */
    public MapNode(MapSet mapSet) {
        this.mapSet = mapSet;
    }

    /**
     * Returns the associated map set.
     * @return the map set
     */
    public MapSet getMapSet() { return mapSet; }

    /**
     * Returns the selected beatmap (based on {@link #mapIndex}).
     * @return the beatmap, or null if the index is invalid
     */
    public LevelMap getSelectedLevelMap() {
        return (mapIndex < 0 || mapIndex >= mapSet.size()) ? null : mapSet.get(mapIndex);
    }

    //Draw command

    /**
     * Returns an array of strings containing beatmap information for the
     * selected beatmap, or null if none selected.
     * @see MapSet#getInfo(int)
     */
    public String[] getInfo() { return (mapIndex < 0) ? null : mapSet.getInfo(mapIndex); }

    /**
     * Returns a formatted string for the beatmap at {@code beatmapIndex}:
     * "Artist - Title [Version]" (version omitted if {@code beatmapIndex} is invalid)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (mapIndex == -1)
            return mapSet.toString();
        else
            return mapSet.get(mapIndex).toString();
    }
}
