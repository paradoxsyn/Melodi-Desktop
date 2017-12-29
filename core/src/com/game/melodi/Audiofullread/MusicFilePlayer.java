package com.game.melodi.Audiofullread;

/**
 * 11/19/04     1.0 moved to LGPL.
 *-----------------------------------------------------------------------
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *----------------------------------------------------------------------
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.game.melodi.Audiostream.AudioInputStream2;
import com.game.melodi.Audiostream.File;
import com.game.melodi.Audiostream.FileInputStream;
import com.game.melodi.Audiostream.ResourceLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

/**
 * Play music files.
 * This class is a modified version of javazoom.jl.player.advanced.AdvancedPlayer,
 * which is part of the javazoom JLayer library.
 * The main modifications consist of:
 *     + Restriction to playing files rather than streams.
 *     + Pre-reading of the audio file to determine its length in frames.
 * These modifications permit arbitrary seek operations.
 *
 * Modifications by David J. Barnes and Michael Kölling.
 * @version 2011.07.31
 * This class is not suitable for playing streams as a file is read
 * completely before playing.
 */
public class MusicFilePlayer {
    //max samples to read
    public static final int THRESHOLD_WINDOW_SIZE = 10;
    //average sample size to get for avg. I think
    public static final float MULTIPLIER = 1.5f;

    // The MPEG audio bitstream.
    private Bitstream bitstream;
    // The MPEG audio decoder.
    private Decoder decoder;
    // The AudioDevice the audio samples are written to.
    private AudioDevice audio;
    // Whether currently playing.
    private boolean playing = false;
    // The file being played.
    private String filename;

    // The number of frames.
    public static int frameCount;
    // The current frame number.
    public static int frameNumber;
    // The position to resume, if any.
    private int resumePosition;
    // Position of feed
    private int feedpos;
    //boolean to know when to stop decoding frames
    public boolean ok;
    public boolean isLoaded;

    public static SampleBuffer output;
    private int sampleRate;
    private int channels;
    private int buflen;
    private int[] feed;

    Header h;

    File file;

    AudioInputStream2 aistream;
    /**
     * Creates a new MusicFilePlayer instance.
     *
     * @param filename The file to be played.
     */
    public MusicFilePlayer(File filename) throws JavaLayerException {
        this.file = filename;
        //file = new File(filename);
        //file = new File(Gdx.files.internal("furelise.mp3").path());

        openAudio(); //makes decoder
        openBitstream(file); //makes bitstream

        h = bitstream.readFrame();
        frameCount = getFrameCount(file);

        channels = (h.mode() == Header.SINGLE_CHANNEL) ? 1 : 2;
        sampleRate = h.frequency();

        output = new SampleBuffer(sampleRate,channels);
        //decoder.setOutputBuffer(output);

        //decoder.decodeFrame(h,bitstream);

        //buflen = output.getBufferLength();
        frameNumber = 0;
        resumePosition = -1;

        System.out.println(getFrameCount(file));
        //System.out.println(getallInfo());
        //System.out.println(Arrays.toString(output.getBuffer()));

        //bitstream.closeFrame();
        //play();

    }
    /**
     * Play the whole file.
     */
    public void play() throws JavaLayerException {
        playFrames(0, frameCount);
    }

    /**
     * Plays a number of MPEG audio frames.
     *
     * @param frames The number of frames to play.
     * @return true if the last frame was played, or false if there are
     * more frames.
     */
    public boolean play(int frames) throws JavaLayerException {
        return playFrames(frameNumber, frameNumber + frames);

    }

    /**
     * Plays a range of MPEG audio frames
     *
     * @param start The first frame to play
     * @param end   The last frame to play
     * @return true if the last frame was played, or false if there are more frames.
     */
    public boolean play(int start, int end) throws JavaLayerException {
        return playFrames(start, start + end);
    }

    /**
     * Play from the given position to the end.
     *
     * @param start The first frame to play.
     * @return true if the last frame was played, or false if there are more frames.
     */
    public boolean playFrom(int start) throws JavaLayerException {
        return playFrames(start, frameCount);
    }

    /**
     * Get the length of the file (in frames).
     *
     * @return The file length, in frames.
     */
    public int getLength() {
        return frameCount;
    }

    /**
     * Get the current playing position (in frames).
     *
     * @return The current frame number.
     */
    public int getPosition() {
        return frameNumber;
    }

    /**
     * Set the playing position (in frames).
     * Playing does not start until resume() is called.
     *
     * @param position The playing position.
     */
    public void setPosition(int position) throws JavaLayerException {
        pause();
        resumePosition = position;
    }


    /**
     * Pause the playing.
     */
    public void pause() throws JavaLayerException {
        synchronized (this) {
            playing = false;
            resumePosition = frameNumber;
        }
    }

    /**
     * Resume the playing.
     */
    public void resume() throws JavaLayerException {
        if (!playing) {
            int start;
            if (resumePosition >= 0) {
                start = resumePosition;
            } else {
                start = frameNumber;
            }
            resumePosition = -1;
            playFrames(start, frameCount);
        }
    }

    /**
     * Return the current frame number.
     *
     * @return The number of the last frame played, or -1 if nothing played yet.
     */
    public int getFrameNumber() {
        return frameNumber;
    }

    /**
     * Play the number of frames left.
     *
     * @return true If finished for any reason, false if paused.
     */
    private boolean playFrames(int start, int end) throws JavaLayerException {
        // Clear any resumption position.
        resumePosition = -1;

        if (end > frameCount) {
            end = frameCount;
        }

        // Make sure the player is in the correct position in the input.
        synchronized (this) {
            moveTo(start);
            playing = true;
        }
        feed = new int[frameCount+1];
        // Play until finished, paused, or a problem.
        ok = true;
        while (frameNumber < end && playing && ok) {
            ok = decodeFrame();
            if (ok) {
                frameNumber++;
                //System.out.println(absArray(output.getBuffer()));
                feed[frameNumber] = absArray(output.getBuffer());
                feedpos++;
            }
        }
        isLoaded=true;
        //System.out.println(Arrays.toString(feed));

        // Stopped for some reason.
        synchronized (this) {
            playing = false;
            // last frame, ensure all data flushed to the audio device.
            AudioDevice out = audio;
            if (out != null) {
                out.flush();
            }
        }
        return ok;
    }
    /**Get Abs value of each frame**/
    public int absArray(short[] buffer) {
        String wave = Arrays.toString(buffer);
        wave = wave.replaceAll("\\[", "").replaceAll("\\]", "");
        String[] wavearray = wave.split(",");

        int[] numbers = new int[wavearray.length];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = Integer.parseInt(wavearray[i].trim());
            numbers[i] = Math.abs(numbers[i]);

        }
        return maxValue(numbers);
    }

    private static int maxValue(int[] numbers){
        int max = numbers[0];
        for(int i=0;i<numbers.length;i++){
            if(numbers[i] > max){
                max = numbers[i];
            }
        }
        return max;

    }
    /**
     * Set the playing position.
     *
     * @param position (in frames)
     */
    private void moveTo(int position) throws JavaLayerException {
        if (position < frameNumber) {
            synchronized (this) {
                // Already played too far.
                if (bitstream != null) {
                    try {
                        bitstream.close();
                    } catch (BitstreamException ex) {
                    }
                }
                openAudio();
                openBitstream(file);
                frameNumber = 0;
            }
        }

        while (frameNumber < position) {
            skipFrame();
            frameNumber++;
        }
    }

    /**
     * Cloases this player. Any audio currently playing is stopped
     * immediately.
     */
    public void close() {
        synchronized (this) {
            if (audio != null) {
                AudioDevice out = audio;
                audio = null;
                // this may fail, so ensure object state is set up before
                // calling this method.
                out.close();
                try {
                    bitstream.close();
                } catch (BitstreamException ex) {
                }
                bitstream = null;
                decoder = null;
            }
        }
    }

    /**
     * Decodes a single frame.
     *
     * @return true if there are no more frames to decode, false otherwise.
     */
    protected boolean decodeFrame() throws JavaLayerException {
        try {
            synchronized (this) {
                //if (audio == null) {
                  //  return false;
                //}
                decoder.setOutputBuffer(output);
                h = bitstream.readFrame();
                decoder.decodeFrame(h,bitstream);

                if (h == null) {
                    return false;
                }
                // sample buffer set when decoder constructed
                //output = (SampleBuffer) decoder.decodeFrame(h, bitstream);

                //if (audio != null) {
                  //  audio.write(output.getBuffer(), 0, output.getBufferLength());
                //}
            }

            bitstream.closeFrame();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new JavaLayerException("Exception decoding audio frame", ex);
        }
        return true;
    }


    /**
     * skips over a single frame
     *
     * @return false    if there are no more frames to decode, true otherwise.
     */
    protected boolean skipFrame() throws JavaLayerException {
        Header h = readFrame();
        if (h == null) {
            return false;
        }
        frameNumber++;
        bitstream.closeFrame();
        return true;
    }

    /**
     * closes the player and notifies <code>PlaybackListener</code>
     */
    public void stop() {
        close();
    }

    /**
     * Count the number of frames in the file.
     * This can be used for positioning.
     *
     * @param filename The file to be measured.
     * @return The number of frames.
     */
    protected int getFrameCount(File filename) throws JavaLayerException {
        openBitstream(filename);
        int count = 0;
        while (skipFrame()) {
            count++;
        }
        bitstream.close();
        return count;
    }

    /**
     * Read a frame.
     *
     * @return The frame read.
     */
    public Header readFrame() throws JavaLayerException {
            return bitstream.readFrame();
    }

    /**
     * Open an audio device.
     */
    protected void openAudio() throws JavaLayerException {
        //audio = FactoryRegistry.systemRegistry().createAudioDevice();
        decoder = new Decoder();
        //audio.open(decoder);
    }

    /**
     * Open a BitStream for the given file.
     *
     * @param filename The file to be opened.
     * @throws IOException If the file cannot be opened.
     */
    private void openBitstream(File filename){

        bitstream = new Bitstream(new BufferedInputStream(new FileInputStream(filename)));


    }

    public int[] getInfo(){
        return feed;
    }

    public String getTitle() {return file.getName();}

    public synchronized float getFeedPosition(){
        if (ok) {
            //feedpos += ((frameNumber - feed.length) / (float)feed.length);
            return Math.min(1, feedpos / (float)feed.length);
        }
        return 0;
    }


}
