package com.game.melodi.Audiostream;

import java.io.*;

import com.badlogic.gdx.files.FileHandle;


public class MusicJL3 extends AbsMusic implements AudioDeviceListener{

	private FileHandle file;
	AudioDevicePlayer player;
	final MusicJL3 thisMusicJL = this;

	public MusicJL3(String path, final AbsMusicCompleteListener lis) throws IOException {
		super(lis);
		file = ResourceLoader.getFileHandle(path);
		path = path.toLowerCase();
		//*
		if(path.endsWith(".mp3")){
			try {
				player = new com.game.melodi.Audiostream.AudioDevicePlayer2(
					new com.game.melodi.Audiostream.MP3InputStreamFactory(
						new FileHandleInputStreamFactory(file)
					), path);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					player = new com.game.melodi.Audiostream.AudioDevicePlayer2(
						new OggInputStreamFactory(
							new FileHandleInputStreamFactory(file)
						), path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if(path.endsWith(".ogg")){
			try {
				player = new com.game.melodi.Audiostream.AudioDevicePlayer2(
					new OggInputStreamFactory(
						new FileHandleInputStreamFactory(file)
					), path);
			} catch (IOException e) {
				e.printStackTrace();
				try {
					player = new com.game.melodi.Audiostream.AudioDevicePlayer2(
						new com.game.melodi.Audiostream.MP3InputStreamFactory(
							new FileHandleInputStreamFactory(file)
						), path);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		/*/
		player = new AudioDevicePlayer3(
				new FileHandleInputStreamFactory(file)
				, path
				);
		//*/
		if(player == null)
			throw new IOException("Could not find player to play "+file);
		player.setAudioDeviceListener(this);
	}

	@Override //AudioDeviceListener
	public void complete(AudioDevicePlayer thisAudioDevicePlayer) {
		lis.complete(thisMusicJL);
	}
	

	@Override //AudioDeviceListener
	public void requestSync(AudioDevicePlayer thisAudioDevicePlayer) {
		lis.requestSync(thisMusicJL);
	}
	
	@Override
	public boolean setPosition(float f) {
		return player.setPosition(f);
	}

	@Override
	public void loop() {
		player.loop();
	}

	@Override
	public void play() {
		player.play();
	}

	@Override
	public boolean playing() {
		return player.playing();
	}

	@Override
	public void pause() {
		player.pause();
	}

	@Override
	public void resume() {
		player.resume();
	}

	@Override
	public void setVolume(float volume) {
		player.setVolume(volume);
	}

	@Override
	public void stop() {
		player.stop();
	}

	@Override
	public float getPosition() {
		return player.getPosition();
	}

	@Override
	public void dispose() {
		player.dispose();
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public void setPitch(float pitch) {
		player.setPitch(pitch);
	}

	@Override
	public String getRate() {
		return player.getRate();
	}

	@Override
	public boolean getEnd(){
		return player.getEnd();
	}

	@Override
	public float getTotalLength() {
		float tn = 0;
		try{
			InputStream f = file.read();
			tn = (float) f.available()/100;
			System.out.println("Channel size: " + tn);
			//int min = header.min_number_of_frames(500);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException er){
			er.printStackTrace();
		}



		float frameRate = player.getHeader().frequency();
		float frameSize = player.getHeader().framesize;
		float duration = player.getHeader().total_ms((int)tn)/1000;


		return tn;
	}

	@Override
	public int fullFeed(){
		return player.fullFeed();
	}

}
