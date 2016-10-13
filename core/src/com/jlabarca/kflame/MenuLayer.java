

package com.jlabarca.kflame;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.director.Director;
import com.jlabarca.director.Layer;
import com.jlabarca.sound.MusicManager;
import com.jlabarca.sound.SoundManager.Efectos;
import com.jlabarca.texture.TextureCache;

/**
 * Menu layer.
 * 
 */
public class MenuLayer extends Layer
{

	public static Skin skin;
	private TextureAtlas atlas;
	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);
	SpriteBatch batch;
	private Director director;
	TextButton startBtn,tutorialBtn, leaderboardBtn,exitBtn;
	private Table menuButtons,menuOptions;
	public static String Name,ip;
	public static MusicManager musicManager;
	public static CheckBox debug,fps,sound,music;
	public static boolean on = false;
	public static Matrix4 sceneMatrix;
	public static boolean firstime = true;
	public static LabelStyle ls;
	public static float hscore = -1;
	public static int myrank = -1;
	TextureCache textureCache = TextureCache.instance();
    public Image splashImage,logo,logo90;
    public static Vector2 scale;
	float hscoreaux = hscore;
	String lastgifStatus = "";
	boolean playPressed = false;
	public static AssetManager manager;
	public static BitmapFont font,bigFont;
	public static boolean loaded = false;
	boolean showing90 = true;
	
	
	
	
	/**
	 * Construct the screen.
	 * 
	 * @param stage
	 */
	public MenuLayer(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);
		this.director = Director.instance();
		//Gdx.input.setCatchBackKey(true);
		loadTextures();
//		musicManager = new MusicManager();
//	    musicManager.setVolume(0.17f);
//	    musicManager.setEnabled(director.prefs.isMusicEnabled());
//	    //create the sound manager
//	    soundManager = new SoundManager();
//	    soundManager.setVolume(0.5f);
//	    musicManager.play(Musica.MENU);
		sceneMatrix = new Matrix4().setToOrtho2D(0, 0, width, height);
		
//		timer = new Timer(); 
//		timer.scheduleAtFixedRate(timertask, 3400, 12000); 

	}
	@SuppressWarnings("deprecation")
	private void loadTextures()
	{	
		scale = new Vector2(getWidth()/320,getHeight()/480f);

		manager = new AssetManager();

		// set the loaders for the generator and the fonts themselves
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		// load to fonts via the generator (implicitely done by the FreetypeFontLoader).
		// Note: you MUST specify a FreetypeFontGenerator defining the ttf font file name and the size
		// of the font to be generated. The names of the fonts are arbitrary and are not pointing
		// to a file on disk!
		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();
		size1Params.fontFileName = "fonts/ProFont.ttf";
		size1Params.fontParameters.size = (int) (20*scale.x);
		size1Params.fontParameters.color =  Color.WHITE;
		size1Params.fontParameters.borderColor = Color.BLACK;
		size1Params.fontParameters.borderWidth = 0.5f*scale.x;
		manager.load("size10.ttf", BitmapFont.class, size1Params);

		FreeTypeFontLoaderParameter size2Params = new FreeTypeFontLoaderParameter();
		size2Params.fontFileName = "fonts/ProFont.ttf";
		size1Params.fontParameters.color =  Color.WHITE;
		size2Params.fontParameters.borderColor = Color.BLACK;
		size2Params.fontParameters.borderWidth = 1f*scale.x;
		size2Params.fontParameters.size = (int) (70*scale.x);
		manager.load("size100.ttf", BitmapFont.class, size2Params);
		
		
		// we also load a "normal" font generated via Hiero
		//manager.load("data/default.fnt", BitmapFont.class);
		
		
		splashImage = new Image( textureCache.getTexture(textureCache.getDefinition(AppActorTextures.TEXTURE_SPLASH)));
        splashImage.setFillParent( true );
        addActor(splashImage);

		
		manager.finishLoading();

		bigFont = MenuLayer.manager.get("size100.ttf", BitmapFont.class);
		font = MenuLayer.manager.get("size10.ttf", BitmapFont.class);
		skin = new Skin();
		skin.addRegions(new TextureAtlas(Gdx.files.internal("fonts/uiskin.atlas")));
		skin.add("default", font);
		skin.load(Gdx.files.internal("fonts/uiskin.json"));
    	loaded = true;
    	buildElements();
   

	}

	 public TextureAtlas getAtlas()
	    {
	        if( atlas == null ) {
	            atlas = new TextureAtlas( Gdx.files.internal( "data/fonts/uiskin.atlas" ) );
	        }
	        return atlas;
	    }

	    protected Skin getSkin()
	    {
	        if( skin == null ) {
	            FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
	            skin = new Skin( skinFile );
	        }
	        return skin;
	    }

	
	   
	private void buildElements()
	{
//		//director.actionResolver.loginGPGS();
//	
//		//if(gameProperties.isAudioOn())
//		//SoundCache.instance().get(AppActorSounds.SOUND_FILL).loop(gameProperties.getVolume());	
		debug = new CheckBox("Debug", skin);
		fps = new CheckBox("Fps", skin);
		sound = new CheckBox("Sound", skin);
		music = new CheckBox("Music", skin);
//		fixed = new CheckBox("Fixed Buttons", skin);

		logo = new Image(textureCache.getTexture(textureCache.getDefinition(AppActorTextures.TEXTURE_LOGO)));
		logo90 = new Image(textureCache.getTexture(textureCache.getDefinition(AppActorTextures.TEXTURE_LOGO90)));
		logo.setScale(scale.x*0.7f, scale.y*0.7f);
		logo90.setScale(scale.x*0.7f, scale.y*0.7f);

		logo.setPosition(getWidth()/2-112*scale.x, getHeight()-90*scale.y);
		logo90.setPosition(getWidth()/2-40*scale.x, getHeight()-135*scale.y);
		startBtn = new TextButton("Play", skin,"default");
		startBtn.addListener(new ChangeListener() {
		    
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	if(FpsLayer.retry || (on&&firstime)){
					((GameScene)(Director.instance().getScene())).gameLayer.reset();
					firstime = false;
			    	hide();
			    	FpsLayer.soundManager.stop(Efectos.RETRY);
				    FpsLayer.score = 0;
	
		    	}else if (!firstime){
		    		hide();
		    	}
		    }
		    
		});
		
		
		tutorialBtn = new TextButton("Tutorial", skin,"default");
		tutorialBtn.addListener(new ChangeListener() {
		    
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	firstime = true;
		    	hide();
		    }
  
		});
		
		
		leaderboardBtn = new TextButton("Ranking", skin,"default");
		leaderboardBtn.addListener(new ChangeListener() {
		    
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		    	
		    	hide();
		    }
		    		    
		});
		
		
		
		
		exitBtn = new TextButton("Exit", skin,"default");
		exitBtn.addListener(new ChangeListener() {
		    @Override
		    public void changed(ChangeEvent event, Actor actor) {
		      		Gdx.app.exit();
		    }  
		}); 
		menuButtons = new Table();
		//table.row().fill().expandX();
	
		menuButtons.pack();
		//table.size((int) getWidth()*4, (int) getHeight()*4);
		//table.setBackground("asdasdasdasdsd");

		menuButtons.setX((320*scale.x)/2);
		menuButtons.setY(480*scale.y/2-30*scale.y);
		menuButtons.row().height(70*scale.y);
		menuButtons.add(startBtn).expandX().align(Align.center).width(80*scale.x);
//		table.add(lobbyButton).expandX().align(Align.center).width(80*scale.x);
		menuButtons.row().height(70*scale.y);
		menuButtons.add(leaderboardBtn).expandX().align(Align.center).width(80*scale.x);
		menuButtons.row().height(70*scale.y);
		menuButtons.add(exitBtn).expandX().align(Align.center).width(80*scale.x);
//		table.row().height(30*scale.y);
//		table.add(searchBtn).expandX().align(Align.center);
//		table.row().height(30*scale.y);
//		table.add(inviteBtn).expandX().align(Align.center);
//		table.row().height(30*scale.y);
//		table.add(invitationBtn).expandX().align(Align.center);
//		table.row().height(30*scale.y);
		menuButtons.row().height(40*scale.y);

		//table.row();
		//table.add(leaveButton).expandX().align(Align.center);

		menuOptions = new Table();
		//table.row().fill().expandX();
		fps.getCells().get(0).size(20*scale.x, 20*scale.y);
		sound.getCells().get(0).size(20*scale.x, 20*scale.y);
		music.getCells().get(0).size(20*scale.x, 20*scale.y);
		debug.getCells().get(0).size(20*scale.x, 20*scale.y);

		menuOptions.pack();
		//table.size((int) getWidth()*4, (int) getHeight()*4);
		//table.setBackground("asdasdasdasdsd");
		menuOptions.setX(10*scale.x);
		menuOptions.setY(320*scale.y/2);
		
		menuOptions.add(fps).expandX().align(Align.left);
		menuOptions.row();
		menuOptions.add(sound).expandX().align(Align.left);
		menuOptions.row();
		menuOptions.add(music).expandX().align(Align.left);
		menuOptions.row();

		debug.setChecked(true);
		fps.setChecked(false);
		music.setChecked(director.prefs.isMusicEnabled());
		sound.setChecked(director.prefs.isSoundEffectsEnabled());


		//table.pack();
	
	     splashImage.addAction( sequence(delay(2f),fadeOut( 0.75f ),
	        		new Action() {
			            @Override
			            public boolean act(float parentAlpha )
			            {
			            	removeActor(splashImage);
			                return true;
			            }           
			        } ) );
		
		
        addActor(logo);
        addActor(logo90);
        addActor(menuButtons);
        addActor(splashImage);
        on = true;
        
	}
	

	public static int getRandomNumberFrom(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt((max + 1) - min) + min;
        return randomNumber;

    }
	
	
	public void hide(){
		 on = false;
		//MenuLayer.soundManager.play(Efectos.ENTER);
		 logo.addAction(fadeOut(0.5f));	
		 logo90.addAction(fadeOut(0.5f));	

		 menuButtons.addAction(sequence(fadeOut(0.5f),new Action() {
             @Override
             public boolean act(float parentAlpha )
             {
         		((GameScene)Director.instance().getScene()).onControl();
                 return true;
             }           
         } ) );
     
		 menuOptions.addAction(sequence(fadeOut(0.5f)));
	     FpsLayer.soundManager.play(Efectos.WIN5);
	     showing90 = false;
	}
	
	public void show(){
	
        on = true;
    	//director.actionResolver.getLeaderboardIntent();
		//soundManager.play(Efectos.QUIT);
	    //musicManager.play(Musica.MENU);
		((GameScene)Director.instance().getScene()).offControl();
	    menuButtons.addAction(sequence(fadeIn(0.5f)));
	    menuOptions.addAction(sequence(fadeIn(0.5f)));
		logo.addAction(fadeIn(0.5f));	
		logo90.addAction(fadeIn(0.5f));
    	FpsLayer.soundManager.play(Efectos.WIN2);
    	showing90 = true;
	}
	public void showOnly90(){
		if(!showing90){
			showing90 = true;
			logo90.addAction(fadeIn(0.5f));
	    	FpsLayer.soundManager.play(Efectos.WIN3);
		}
	}




	@Override
	public void draw(Batch batch, float parentAlpha)
	{
	

		super.draw(batch, parentAlpha);
		//soundManager.setEnabled(sound.isChecked());
		//musicManager.setEnabled(music.isChecked());
		if(on && FpsLayer.goalColor!=null)
			logo90.setColor(FpsLayer.goalColor);

		
	}
	
	
	@Override
	public void enter()
	{

	}

	@Override
	public boolean keyDown(int keycode)
	{
		boolean handled = false;
		if (keycode == Keys.ENTER)
		{
			
			
		}
	
		if (keycode == Keys.ESCAPE||keycode == Keys.BACK)
		{
//			if(on){
//				Gdx.app.exit();
//				System.exit(0);
//			}

		}
		return handled;
	}
	
	public void makeMessage(){

	   	addActor(menuButtons);
    	addActor(menuOptions);

	}
	
	 @Override
	 public void exit(){

		 //director.prefs.setMusicEnabled(music.isChecked());
		 //director.prefs.setSoundEffectsEnabled(sound.isChecked());


	 }

}
