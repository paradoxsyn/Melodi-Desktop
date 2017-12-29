package com.game.melodi.Maps;

/**
 * Created by Paradox on 5/2/2017.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Indexed, expanding, doubly-linked list data type for song groups.
 */

public class MapSetList {
    /** Song group structure (each group contains a list of beatmaps). */
    private static MapSetList list;

    /** Search pattern for conditional expressions. */
    private static final Pattern SEARCH_CONDITION_PATTERN = Pattern.compile(
            "(ar|cs|od|hp|bpm|length|stars?)(==?|>=?|<=?)((\\d*\\.)?\\d+)"
    );

    /** List containing all parsed nodes. */
    private ArrayList<MapNode> parsedNodes;

    /** Total number of beatmaps (i.e. Beatmap objects). */
    private int mapCount = 0;

    /** List containing all nodes in the current group. */
    private ArrayList<MapNode> groupNodes;

    /** Current list of nodes (subset of parsedNodes, used for searches). */
    private ArrayList<MapNode> nodes;

    /** Set of all beatmap set IDs for the parsed beatmaps. */
    private HashSet<Integer> MSIDdb;

    /** Map of all MD5 hashes to beatmaps. */
    private HashMap<String, LevelMap> mapHashDB;

    /** Index of current expanded node (-1 if no node is expanded). */
    private int expandedIndex;

    /** Start and end nodes of expanded group. */
    private MapNode expandedStartNode, expandedEndNode;

    /** The last search query. */
    private String lastQuery;

    /**
     * Creates a new instance of this class (overwriting any previous instance).
     */
    public static void create() { list = new MapSetList(); }

    /**
     * Returns the single instance of this class.
     */
    public static MapSetList get() { return list; }

    /**
     * Constructor.
     */
    private MapSetList() {
        parsedNodes = new ArrayList<MapNode>();
        MSIDdb = new HashSet<Integer>();
        mapHashDB = new HashMap<String, LevelMap>();
        reset();
    }

    /**
     * Resets the list's fields.
     * This does not erase any parsed nodes.
     */
    public void reset() {
        //nodes = groupNodes = BeatmapGroup.current().filter(parsedNodes);
        expandedIndex = -1;
        expandedStartNode = expandedEndNode = null;
        lastQuery = "";
    }

    /**
     * Returns the number of elements.
     */
    public int size() { return nodes.size(); }

    /**
     * Adds a song group.
     * @param maps the list of beatmaps in the group
     * @return the new BeatmapSetNode
     */
    public MapNode addSongGroup(ArrayList<LevelMap> maps) {
        MapSet mapSet = new MapSet(maps);
        MapNode node = new MapNode(mapSet);
        parsedNodes.add(node);
        mapCount += maps.size();

        // add beatmap set ID to set
        int msid = maps.get(0).mapSetID;
        if (msid > 0)
            MSIDdb.add(msid);

        // add MD5 hashes to table
        //for (LevelMap map : maps) {
          //  if (map.md5Hash != null)
            //    mapHashDB.put(map.md5Hash, map);
        //}

        return node;
    }

    /**
     * Returns the index of the expanded node (or -1 if nothing is expanded).
     */
    public int getExpandedIndex() { return expandedIndex; }






}
