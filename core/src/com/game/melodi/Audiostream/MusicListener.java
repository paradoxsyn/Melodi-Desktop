package com.game.melodi.Audiostream;

public interface MusicListener {

	public void musicEnded(Music music);

	public void musicSwapped(Music music, Music newMusic);

}
