package com.game.melodi.Audiostream;

import java.io.IOException;


public class OggInputStreamFactory implements AudioInputStreamFactory {

	com.game.melodi.Audiostream.InputStreamFactory in;
	
	public OggInputStreamFactory(com.game.melodi.Audiostream.InputStreamFactory in) {
		this.in = in;
	}

	@Override
	public com.game.melodi.Audiostream.AudioInputStream2 getNewAudioInputStream() throws IOException {
		return new com.game.melodi.Audiostream.OggInputStream2(in.getNewInputStream());
	}

}
