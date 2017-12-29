package com.game.melodi.Maps;

import com.game.melodi.Audiostream.File;
import com.game.melodi.Audiostream.FileInputStream;
import com.game.melodi.Audiostream.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Paradox on 5/2/2017.
 */

public class MapParser {
    /** The current file being parsed. */
    private static File currentFile;

    /** The expected pattern for beatmap directories, used to find beatmap set IDs. */
    private static final String DIR_MSID_PATTERN = "^\\d+ .*";

    /** The current directory number while parsing. */
    private static int currentDirectoryIndex = -1;

    /** The total number of directories to parse. */
    private static int totalDirectories = -1;

    /** Parser statuses. */
    public enum Status { NONE, PARSING, CACHE, INSERTING };

    /** The current status. */
    private static Status status = Status.NONE;

    // This class should not be instantiated.
    private MapParser() {}

    /**
     * Invokes parser for each OSU file in a root directory and
     * adds the beatmaps to a new BeatmapSetList.
     * @param root the root directory (search has depth 1)
     */
    public static void parseAllFiles(File root) {
        // create a new BeatmapSetList
        MapSetList.create();

        // create a new watch service
        //if (Options.isWatchServiceEnabled())
            //BeatmapWatchService.create();

        // parse all directories
        parseDirectories(root.listFiles());
    }

    /**
     * Invokes parser for each directory in the given array and
     * adds the beatmaps to the existing BeatmapSetList.
     * @param dirs the array of directories to parse
     * @return the last BeatmapSetNode parsed, or null if none
     */
    public static MapNode parseDirectories(File[] dirs) {
        if (dirs == null)
            return null;

        // progress tracking
        status = Status.PARSING;
        currentDirectoryIndex = 0;
        totalDirectories = dirs.length;

        // get last modified map from database
        //Map<String, BeatmapDB.LastModifiedMapEntry> lastModifiedMap = BeatmapDB.getLastModifiedMap();

        // beatmap lists
        List<ArrayList<LevelMap>> allmaps = new LinkedList<ArrayList<LevelMap>>();
        //List<LevelMap> cachedmaps = new LinkedList<LevelMap>();  // loaded from database
        List<LevelMap> parsedmaps = new LinkedList<LevelMap>();  // loaded from parser

        // watch service
        //BeatmapWatchService ws = (Options.isWatchServiceEnabled()) ? BeatmapWatchService.get() : null;

        // parse directories
        MapNode lastNode = null;
        long timestamp = System.currentTimeMillis();
        for (File dir : dirs) {
            currentDirectoryIndex++;
            if (!dir.isDirectory())
                continue;

            // find all OSU files
            File[] files = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(java.io.File dir, String name) {
                    return name.toLowerCase().endsWith(".mp3");
                }
            });
            if (files == null || files.length < 1)
                continue;

            // create a new group entry
            ArrayList<LevelMap> maps = new ArrayList<LevelMap>(files.length);
            for (File file : files) {
                currentFile = file;

                // check if beatmap is cached
                /*String mapPath = String.format("%s/%s", dir.getName(), file.getName());
                if (lastModifiedMap != null) {
                    BeatmapDB.LastModifiedMapEntry entry = lastModifiedMap.get(beatmapPath);
                    if (entry != null) {
                        // check last modified times
                        if (entry.getLastModified() == file.lastModified()) {
                            if (entry.getMode() == Beatmap.MODE_OSU) {  // only support standard mode
                                // add to cached beatmap list
                                Beatmap beatmap = new Beatmap(file);
                                beatmaps.add(beatmap);
                                cachedBeatmaps.add(beatmap);
                            }
                            continue;
                        } else  // out of sync, delete cache entry and re-parse
                            BeatmapDB.delete(dir.getName(), file.getName());
                    }
                }*/

                // parse map
                LevelMap map = null;
                try {
                    // Parse hit objects only when needed to save time/memory.
                    // Change boolean to 'true' to parse them immediately.
                    map = parseFile(file, dir, maps, false);
                } catch (Exception e) {
                    //ErrorHandler.error(String.format("Failed to parse beatmap file '%s'.",
                      //      file.getAbsolutePath()), e, true);
                }

                // add to parsed beatmap list
                if (map != null) {
                    map.dateAdded = timestamp;
                    //if (map.mode == map.MODE_OSU)  // only support standard mode
                    maps.add(map);
                    parsedmaps.add(map);
                }
            }

            // add group entry if non-empty
            if (!maps.isEmpty()) {
                maps.trimToSize();
                allmaps.add(maps);
				/*
				if (ws != null)
					ws.registerAll(dir.toPath());
				*/
            }

            // stop parsing files (interrupted)
            if (Thread.interrupted())
                break;
        }

        // load cached entries from database
        //if (!cachedBeatmaps.isEmpty()) {
          //  status = Status.CACHE;

            // Load array fields only when needed to save time/memory.
            // Change flag to 'LOAD_ALL' to load them immediately.
            //BeatmapDB.load(cachedBeatmaps, BeatmapDB.LOAD_NONARRAY);
        //}

        // add group entries to BeatmapSetList
        for (ArrayList<LevelMap> maps : allmaps) {
            Collections.sort(maps);
            lastNode = MapSetList.get().addSongGroup(maps);
        }

        // clear string DB
        //stringdb = new HashMap<String, String>();

        // add beatmap entries to database
        if (!parsedmaps.isEmpty()) {
            status = Status.INSERTING;
            //BeatmapDB.insert(parsedBeatmaps);
        }

        status = Status.NONE;
        currentFile = null;
        currentDirectoryIndex = -1;
        totalDirectories = -1;
        return lastNode;
    }

    /**
     * Returns false if the line is too short or commented.
     */
    private static boolean isValidLine(String line) {
        return (line.length() > 1 && !line.startsWith("//"));
    }

    /**
     * Parses a beatmap.
     * @param file the file to parse
     * @param dir the directory containing the beatmap
     * @param maps the song group
     * @param parseObjects if true, hit objects will be fully parsed now
     * @return the new beatmap
     */

    /**
     * Splits line into two strings: tag, value.
     * If no ':' character is present, null will be returned.
     */
    private static String[] tokenize(String line) {
        int index = line.indexOf(':');
        if (index == -1) {
            Log.debug(String.format("Failed to tokenize line: '%s'.", line));
            return null;
        }

        String[] tokens = new String[2];
        tokens[0] = line.substring(0, index).trim();
        tokens[1] = line.substring(index + 1).trim();
        return tokens;
    }

    /**
     * Returns the file extension of a file.
     * @param file the file name
     */
    public static String getExtension(String file) {
        int i = file.lastIndexOf('.');
        return (i != -1) ? file.substring(i + 1).toLowerCase() : "";
    }

    /**
     * Returns the name of the current file being parsed, or null if none.
     */
    public static String getCurrentFileName() {
        if (status == Status.PARSING)
            return (currentFile != null) ? currentFile.getName() : null;
        else
            return (status == Status.NONE) ? null : "";
    }

    /**
     * Returns the progress of file parsing, or -1 if not parsing.
     * @return the completion percent [0, 100] or -1
     */
    public static int getParserProgress() {
        if (currentDirectoryIndex == -1 || totalDirectories == -1)
            return -1;

        return currentDirectoryIndex * 100 / totalDirectories;
    }

    /**
     * Returns the current parser status.
     */
    public static Status getStatus() { return status; }

    /**
     * Returns the String object in the database for the given String.
     * If none, insert the String into the database and return the original String.
     * //@param s the string to retrieve
     * @return the string object
     */


    private static LevelMap parseFile(File file, File dir, ArrayList<LevelMap> maps, boolean parseObjects) {
        LevelMap map = new LevelMap(file);
        LevelMap.timingPoints = new ArrayList<TimingPoint>();

        try {
            InputStream bis = new BufferedInputStream(new FileInputStream(file));
            BufferedReader in = new BufferedReader(new InputStreamReader(bis));
            System.out.println(in+ "" + bis);
        }catch(Exception e) {
            Log.warn(String.format("Failed to read hit object end time '%s' for file '%s'.",
                    file.getAbsolutePath()), e);
        }

        return map;
    }

}
