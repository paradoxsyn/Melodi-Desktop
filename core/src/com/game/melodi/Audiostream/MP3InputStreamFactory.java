package com.game.melodi.Audiostream;

import java.io.IOException;


public class MP3InputStreamFactory implements AudioInputStreamFactory {

	InputStreamFactory in;
	
	public MP3InputStreamFactory(InputStreamFactory in) {
		this.in = in;
	}

	@Override
	public AudioInputStream2 getNewAudioInputStream() throws IOException {
		return new Mp3InputStream2(in.getNewInputStream());
	}

}
