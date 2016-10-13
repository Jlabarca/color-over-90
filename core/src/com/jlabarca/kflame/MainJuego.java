package com.jlabarca.kflame;


import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.jlabarca.definition.AppActorEvents;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.director.ActorEvent;
import com.jlabarca.director.ActorEventObserver;
import com.jlabarca.director.Director;
import com.jlabarca.director.Scene;
import com.jlabarca.texture.TextureCache;



public class MainJuego extends InputAdapter implements ApplicationListener, ActorEventObserver
{
	private static final float DEFAULT_CLEAR_COLOUR_RED = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_BLUE = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_GREEN = 0.5f;
	private static final float DEFAULT_CLEAR_COLOUR_ALPHA = 1.0f;

	public static final String APPLICATION_NAME = "Tramp";
	
	public static final int DEFAULT_WIDTH = 480;
	public static final int DEFAULT_HEIGHT = 320;

	// Our 'stages' for groups of actors.
	private GameScene gameScene;


	// The one and only director.
	private Director director;
	

	@Override
	public void create()
	{
		// Requires Graphics context.
		this.director = Director.instance();
	
		
		TextureCache.instance().load(AppActorTextures.TEXTURES);

		// Load/Re-load textures
		
		// Load/Re-load sounds.
		//SoundCache.instance().load(AppActorSounds.SOUNDS);
		
		director.setClearColourA(DEFAULT_CLEAR_COLOUR_ALPHA);
		director.setClearColourR(DEFAULT_CLEAR_COLOUR_RED);
		director.setClearColourG(DEFAULT_CLEAR_COLOUR_GREEN);
		director.setClearColourB(DEFAULT_CLEAR_COLOUR_BLUE);

		// Set initial width and height.
//		this.director.setWidth(Gdx.graphics.getWidth());
//		this.director.setHeight(Gdx.graphics.getWidth());
		this.director.setWidthHeight(DEFAULT_WIDTH, DEFAULT_HEIGHT);

		// Add this as an event observer.
	
		this.director.registerEventHandler(this);
		this.director.setScene(getGameScene());
		//count++;


	}

	/**
	 * Resize.
	 * 
	 */
	@Override
	public void resize(int width, int height)
	{
		// Recalculate scale factors for touch events.
		director.recalcScaleFactors(width, height);
	    director.getScene().getViewport().update(width, height, true);
	}

	/**
	 * Render view.
	 * 
	 */
	@Override
	public void render()
	{
		// Update director
		director.update();
	
	}

	@Override
	public void pause()
	{
		//TextureCache.instance().dispose();
		//resize(480,320);

		//transitionToMenuScene();
		//if(SimulationLayer.gameProperties.isAudioOn())
		//	SoundCache.instance().get(AppActorSounds.SOUND_INTRO).stop();

	}

	@Override
	public void resume()
	{

		//resize(480,320);

		this.director.registerEventHandler(this);
		
		// Load/Re-load textures
	//	 TextureCache.instance().load(AppActorTextures.TEXTURES);

		// Load/Re-load sounds.
		//SoundCache.instance().load(AppActorSounds.SOUNDS);
		//this.director.setScene(getMenuScene());
		//if(SimulationLayer.gameProperties.isAudioOn())
			//SoundCache.instance().get(AppActorSounds.SOUND_LOOP).play();
	}

	@Override
	public void dispose()
	{
		this.pause();
	

		// Sounds.
		//SoundCache.instance().dispose();

		// Views.
		Director.instance().setScene(null);
		
		// Cleanup application view.
		if (gameScene != null)
		{
		 	gameScene.cleanup();
		}
		
		// Graphics.
		TextureCache.instance().dispose();
		if(!Gdx.app.getType().equals(ApplicationType.Android));
			System.exit(1);	
	}

	/**
	 * Handle screen events.
	 * 
	 */
	@Override
	public boolean handleEvent(ActorEvent event)
	{
		boolean handled = false;

		switch (event.getId())
		{
		
	
		case AppActorEvents.EVENT_TRANSITION_TO_GAME_SCENE:
			transitionToGameScene();
			handled = true;
			break;
		case AppActorEvents.EVENT_TRANSITION_TO_MENU_SCENE:
			handled = true;
			break;	
	
		default:
			break;
		}

		return handled;
	}
     
	/**
	 * Run transition to menu.
	 * 
	 */
	
	private void transitionToGameScene()
	{
		Scene inScene = getGameScene();
		//gameScene.menuLayer.show();

		//Scene outScene = this.director.getScene();
		//TransitionScene transitionScene = MoveInBLTransitionScene.$(inScene, outScene, DURATION_MENU_TRANSITION, Bounce.INOUT);

		this.director.setScene(inScene);
	}



	
	public GameScene getGameScene()
	{
		if (gameScene == null)
		{
			gameScene = new GameScene();
		}

		return gameScene;
	}

	
	
	





}
