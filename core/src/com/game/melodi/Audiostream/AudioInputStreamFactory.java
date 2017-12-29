package com.game.melodi.Audiostream;

import java.io.IOException;


public interface AudioInputStreamFactory {
	public AudioInputStream2 getNewAudioInputStream() throws IOException;
}
