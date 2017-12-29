package com.game.melodi.Maps;

/**
 * Created by Paradox on 5/1/2017.
 */

import com.game.melodi.Audiostream.File;
import com.game.melodi.Audiostream.Log;

import java.util.ArrayList;

/**
 * levelmap structure storing data parsed from files.
 */



public class LevelMap implements Comparable<LevelMap>{
    /** Game modes. */
    //public static final byte MODE_OSU = 0, MODE_TAIKO = 1, MODE_CTB = 2, MODE_MANIA = 3;

    /** The File object associated with this beatmap. */
    private File file;

    /** The timestamp this map was first loaded. */
    public long dateAdded = 0;

    /** Total number of times this map has been played. */
    public int playCount = 0;

    /** The local music offset. */
    public int localMusicOffset = 0;

    /**
     * [General]
     */

    /** Audio file object. */
    public File audioFilename;

    /** Delay time before music starts (in ms). */
    public int audioLeadIn = 0;

    /** Start position of music preview (in ms). */
    public int previewTime = -1;

    /** Countdown type (0:disabled, 1:normal, 2:half, 3:double). */
    public byte countdown = 0;

    /** Sound samples ("None", "Normal", "Soft"). */
    public String sampleSet = "";

    /** How often closely placed hit objects will be stacked together. */
    //public float stackLeniency = 0.7f;

    /**
     * [Metadata]
     */

    /** Song title. */
    public String title = "", titleUnicode = "";

    /** Song artist. */
    public String artist = "", artistUnicode = "";

    /** Song source. */
    public String source = "";

    /** Song tags (for searching). */
    public String tags = "";

    /** map ID. */
    public int mapID = 0;

    /** map set ID. */
    public int mapSetID = 0;

    /**
     * [Difficulty]
     */

    /** HP: Health drain rate (0:easy ~ 10:hard) */
    public float HPDrainRate = 5f;

    /** OD: Affects timing window, spinners, and approach speed (0:easy ~ 10:hard). */
    public float overallDifficulty = 5f;

    /** AR: How long circles stay on the screen (0:long ~ 10:short). */
    //public float approachRate = -1f;

    /** Slider movement speed multiplier. */
    //public float sliderMultiplier = 1.4f;

    /** Rate at which slider ticks are placed (x per beat). */
    //public float sliderTickRate = 1f;

    /**
     * [Events]
     */

    /** All break periods (start time, end time, ...). */
    public ArrayList<Integer> breaks;

    /**
     * [TimingPoints]
     */

    /** All timing points. */
    public static ArrayList<TimingPoint> timingPoints;

    /** Song BPM range. */
    public int bpmMin = 0, bpmMax = 0;

    /**
     * [Colours]
     */

    /** Combo colors (max 8). If null, the skin value is used. */
    //public Color[] combo;

    /** Slider border color. If null, the skin value is used. */
    //public Color sliderBorder;

    /**
     * [FlyinObjects]
     */

    /** All flying objects. */
    //public HitObject[] objects;

    /** Number of individual objects. */
    public int
            flyObjectCircle = 0,
            flyObjectSquare = 0,
            flyObjectSPoly = 0;

    /** Last object end time (in ms). */
    public int endTime = -1;

    /**
     * Constructor.
     * @param file the file associated with this beatmap
     */
    public LevelMap(File file) {
        this.file = file;
    }

    /**
     * Returns the associated file object.
     * @return the File object
     */
    public File getFile() { return file; }

    /**
     * Returns the song title.
     * If configured, the Unicode string will be returned instead.
     * @return the song title
     */
    public String getTitle() {
        //return (Options.useUnicodeMetadata() && !titleUnicode.isEmpty()) ? titleUnicode : title;
        return null;
    }

    /**
     * Returns the list of combo colors (max 8).
     * If the beatmap does not provide colors, the skin colors will be returned instead.
     * @return the combo colors
     */
    //public Color[] getComboColors() {
        //return (combo != null) ? combo : Options.getSkin().getComboColors();
    //}

    /**
     * Returns the {@link #breaks} field formatted as a string,
     * or null if the field is null.
     */
    public String breaksToString() {
        if (breaks == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i : breaks) {
            sb.append(i);
            sb.append(',');
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Sets the {@link #breaks} field from a string.
     * @param s the string
     */
    public void breaksFromString(String s) {
        if (s == null)
            return;

        this.breaks = new ArrayList<Integer>();
        String[] tokens = s.split(",");
        for (int i = 0; i < tokens.length; i++)
            breaks.add(Integer.parseInt(tokens[i]));
    }

    /**
     * Returns the {@link #timingPoints} field formatted as a string,
     * or null if the field is null.
     */
    public String timingPointsToString() {
        if (timingPoints == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (TimingPoint p : timingPoints) {
            sb.append(p.toString());
            sb.append('|');
        }
        if (sb.length() > 0)
            sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Sets the {@link #timingPoints} field from a string.
     * @param s the string
     */
    public void timingPointsFromString(String s) {
        this.timingPoints = new ArrayList<TimingPoint>();
        if (s == null)
            return;

        String[] tokens = s.split("\\|");
        for (int i = 0; i < tokens.length; i++) {
            try {
                timingPoints.add(new TimingPoint(tokens[i]));
            } catch (Exception e) {
                Log.warn(String.format("Failed to read timing point '%s'.", tokens[i]), e);
            }
        }
        timingPoints.trimToSize();
    }

    /**
     * Compares two Beatmap objects first by overall difficulty, then by total objects.
     */
    @Override
    public int compareTo(LevelMap that) {
        /*int cmp = Float.compare(this.overallDifficulty, that.overallDifficulty);
        if (cmp == 0)
            cmp = Integer.compare(
                    this.hitObjectCircle + this.hitObjectSlider + this.hitObjectSpinner,
                    that.hitObjectCircle + that.hitObjectSlider + that.hitObjectSpinner
            );
        return cmp;*/
        return -1;
    }


}
