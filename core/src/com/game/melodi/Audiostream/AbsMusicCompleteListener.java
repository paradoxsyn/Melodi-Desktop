package com.game.melodi.Audiostream;

interface AbsMusicCompleteListener {

	public abstract void complete(AbsMusic mus);

	public void requestSync(AbsMusic mus);
	
}