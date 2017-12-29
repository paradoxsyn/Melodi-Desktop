package com.game.melodi.Audiostream;

public interface AudioDeviceListener {
	public void complete(AudioDevicePlayer thisAudioDevicePlayer);
	public void requestSync(AudioDevicePlayer thisAudioDevicePlayer);
}
