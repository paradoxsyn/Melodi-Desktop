package com.game.melodi.Audiostream;

import javazoom2.jl.decoder.Bitstream;
import javazoom2.jl.decoder.Decoder;
import javazoom2.jl.decoder.Header;

public abstract class AudioDevicePlayer {

	public abstract void setAudioDeviceListener(AudioDeviceListener audioDeviceListener);

	public boolean setPosition(float f) {
		return false;
	}

	public void loop() {
		
	}

	public void play() {
		
	}

	public boolean playing() {
		return false;
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void setVolume(float volume) {
		
	}

	public void stop() {
		
	}

	public float getPosition() {
		return 0;
	}

	public void dispose() {
		
	}

	public String getName() {
		return null;
	}

	public void setPitch(float pitch) {

	}

	public String getRate() {
		return null;
	}

	public abstract Header getHeader();

	/**
	 * Get the header of the file
	 *
	 * @return the header
	 */

	public abstract Decoder getDecoder();

	public abstract Bitstream getBitStream();

    public abstract boolean getEnd();

	public abstract int fullFeed();
}
