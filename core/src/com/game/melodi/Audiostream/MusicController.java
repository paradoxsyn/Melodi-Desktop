package com.game.melodi.Audiostream;

import com.game.melodi.Maps.LevelMap;

/**
 * Created by Paradox on 4/29/2017.
 */

public class MusicController {

    /** The current music track. */
    private static Music player;

    /** The last map passed to play(). */
    private static LevelMap lastLevelmap;

    /** The track duration. */
    private static int duration = 0;

    /** Thread for loading tracks. */
    private static Thread trackLoader;

    /** Whether or not the current track has ended. */
    private static boolean trackEnded;

    /** Whether the theme song is currently playing. */
    private static boolean themePlaying = false;

    /** Track pause time. */
    private static float pauseTime = 0f;

    /** Whether the current track volume is dimmed. */
    private static boolean trackDimmed = false;

    /** The track dim level, if dimmed. */
    private static float dimLevel = 1f;


    // This class should not be instantiated.
    private MusicController() {}

    /**
     * Plays an audio file at the preview position.
     * If the audio file is already playing, then nothing will happen.
     * @param map the map to play
     * @param loop whether or not to loop the track
     * @param preview whether to start at the preview time (true) or beginning (false)
     */
    public static void play(final LevelMap map, final boolean loop, final boolean preview) {
        // new track: load and play
        if (lastLevelmap == null || !map.audioFilename.equals(lastLevelmap.audioFilename)) {
            final File audioFile = map.audioFilename;
            if (!audioFile.isFile() && !ResourceLoader.resourceExists(audioFile.getPath())) {
                //UI.getNotificationManager().sendBarNotification(String.format("Could not find track '%s'.", audioFile.getName()));
                System.out.println("Null");
                return;
            }

            //reset();
            System.gc();

            //switch (BeatmapParser.getExtension(beatmap.audioFilename.getName())) {
                //case "ogg":
                //case "mp3":
                    //	trackLoader = new Thread() {
                    //		@Override
                    //		public void run() {
                    loadTrack(audioFile, (preview) ? map.previewTime : 0, loop);
                    //		}
                    //	};
                    //	trackLoader.start();
                    //break;
                //default:
                    //break;
            }
        //}

        // new track position: play at position
        else if (map.previewTime != lastLevelmap.previewTime)
            playAt(map.previewTime, loop);

        lastLevelmap = map;
    }


    /**
     * Loads a track and plays it.
     * @param file the audio file
     * @param position the track position (in ms)
     * @param loop whether or not to loop the track
     */
    public static void loadTrack(File file, int position, boolean loop) {
        try {
            player = new Music(file.getPath(), true);
            player.addListener(new MusicListener() {
                @Override
                public void musicEnded(Music music) {
                    if (music == player) {  // don't fire if music swapped
                        trackEnded = true;
                    }
                }

                @Override
                public void musicSwapped(Music music, Music newMusic) {}
            });
            playAt(position, loop);
        } catch (Exception e) {
            //ErrorHandler.error(String.format("Could not play track '%s'.", file.getName()), e, false);
        }
    }

    /**
     * Plays the current track at the given position.
     * @param position the track position (in ms)
     * @param loop whether or not to loop the track
     */
    public static void playAt(final int position, final boolean loop) {
        if (trackExists()) {
            //setVolume(Options.getMusicVolume() * Options.getMasterVolume());
            setVolume(50f);
            trackEnded = false;
            pauseTime = 0f;
            if (loop)
                player.loop();
            else
                player.play();
            if (position >= 0)
                player.setPosition(position / 1000f);
        }
    }

    /**
     * Plays the current track.
     * @param loop whether or not to loop the track
     */
    public static void play(boolean loop) {
        if (trackExists()) {
            trackEnded = false;
            if (loop)
                player.loop();
            else
                player.play();
        }
    }

    /**
     * Returns true if a track is loaded.
     */
    public static boolean trackExists() { return (player != null); }

    /**
     * Returns whether or not the volume of the current track, if any,
     * has been dimmed.
     */
    public static boolean isTrackDimmed() { return trackDimmed; }

    /**
     * Sets the music volume.
     * @param volume the new volume [0, 1]
     */
    public static void setVolume(float volume) {
        SoundStore.get().setMusicVolume((isTrackDimmed()) ? volume * dimLevel : volume);
    }

    public static String getPosition() {
        return player.music.getRate();
    }

    public static float getTotalLength() {
        return player.music.getTotalLength();
    }

    public static boolean getEnd(){
        return player.music.getEnd();
    }

    public void pause(){
        player.pause();
    }

    public void resume(){
        player.music.resume();
    }

    public void dispose(){
        player.music.dispose();
    }

    public static int fullFeed(){
        return player.music.fullFeed();
    }

}
