package com.jlabarca.kflame;

import com.jlabarca.director.Layer;
import com.jlabarca.sound.MusicManager;
import com.jlabarca.sound.SoundManager;

public class SoundLayer extends Layer{
	MusicManager musicManager;
	SoundManager soundManager;
	public SoundLayer() {
		super();
	    // create the music manager
	    musicManager = new MusicManager();
	    //musicManager.setVolume( preferencesManager.getVolume() );
	    //musicManager.setEnabled( preferencesManager.isMusicEnabled() );

	    // create the sound manager
	    soundManager = new SoundManager();
	    //soundManager.setVolume( preferencesManager.getVolume() );
	    //soundManager.setEnabled( preferencesManager.isSoundEnabled() );
	}

	@Override
	public void enter() {
		// TODO Auto-generated method stub
		super.enter();
	}

	@Override
	public void exit() {
		// TODO Auto-generated method stub
		super.exit();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchDown(x, y, pointer, button);
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return super.touchUp(x, y, pointer, button);
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		// TODO Auto-generated method stub
		return super.touchDragged(x, y, pointer);
	}

	@Override
	public boolean mouseMoved(int x, int y) {
		// TODO Auto-generated method stub
		return super.mouseMoved(x, y);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return super.keyDown(keycode);
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return super.keyUp(keycode);
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return super.keyTyped(character);
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return super.scrolled(amount);
	}
	



}
