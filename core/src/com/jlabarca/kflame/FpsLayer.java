
package com.jlabarca.kflame;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.director.Director;
import com.jlabarca.director.Layer;
import com.jlabarca.sound.MusicManager;
import com.jlabarca.sound.SoundManager;
import com.jlabarca.sound.SoundManager.Efectos;
import com.jlabarca.texture.TextureCache;
import com.jlabarca.texture.TextureDefinition;


public class FpsLayer extends Layer
{


	
	private static final String TEXT_FPS = "Fps:";
	static final char chars[] = new char[100];
	static final StringBuilder textStringBuilder = new StringBuilder(100);
	public static BitmapFont smallFont;

	public static int showingScore = 0;
	private Vector2 scale;
	private TextureCache textureCache;
	private TextureDefinition definition;


	HashSet<Color> colors;
	Image goal,currentColor;
	public static Color goalColor;
	DecimalFormat df;
	TextButton goButton, showButton;
	public boolean checkingSimilarity = false;
	public static boolean retry = false;
	float similarity = 0,growingSimilarity = 0;
	boolean loaded = false; 
	ScheduledExecutorService scheduleExecutor;
	private static GlyphLayout glyphPercent = new GlyphLayout();
	private static GlyphLayout glyphScore = new GlyphLayout();

	int minimumAmount = 300;
	public static int score = 0;
	int scoreLevel = 0; // punto sobre 90 que se va obteniendo en el growing
	int currentLevel = 0; 
	int scoreType = 0; //si lo muestra con suspenso o no
	public static SoundManager soundManager;
	public static MusicManager musicManager;
	
	
	public FpsLayer(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);
		buildElements();
	}

	

	private void buildElements()
	{
		musicManager = new MusicManager();
		musicManager.setVolume(0.3f);
		musicManager.setEnabled(true);
	    soundManager = new SoundManager();
	    soundManager.setVolume(0.1f);
	    soundManager.setEnabled(true);
	    
		colors = new HashSet<Color>();
		df = new DecimalFormat("#0.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		scale = new Vector2(getWidth()/320f,getHeight()/480f);
		textureCache = TextureCache.instance();
		definition = textureCache.getDefinition(AppActorTextures.TEXTURE_PARTICLE);
		TextureRegion sbase = textureCache.getTexture(definition);
		goal = new Image(sbase);
		currentColor = new Image(sbase);

		goal.setScale(10*(scale.y+scale.x)/2);
		goal.setPosition(320*scale.x, 240*scale.y);
		//addActor(goal);		
		currentColor.setScale(10*(scale.y+scale.x)/2);
		currentColor.setPosition(-100*scale.x, 240*scale.y);
		//addActor(currentColor);
		
		scheduleExecutor = Executors.newSingleThreadScheduledExecutor();
		reset();

		
		Timer timer = new Timer();

	
		timer.scheduleAtFixedRate(new TimerTask()   
				{  
					public void run() {
						if(goButton!=null && !checkingSimilarity)
							if(numberOfParticles()>minimumAmount )
								goButton.setText("GO");
							else
								goButton.setText("");

					}   
				}, 0,1000); 
		
		
		
		
		
	}
	


	@Override
	public void draw(Batch batch, float parentAlpha) {
		
		batch.setShader(null);
		super.draw(batch, parentAlpha);
		if (checkingSimilarity) {
			if(growingSimilarity!=similarity){
				if(similarity - growingSimilarity < 0.1f)
					growingSimilarity = lerp(growingSimilarity,similarity,0.9f);
				else if(scoreType >5 && growingSimilarity>87&& growingSimilarity<90)
					growingSimilarity = lerp(growingSimilarity,similarity,0.01f);
				else
					growingSimilarity = lerp(growingSimilarity,similarity,0.1f);
				
				if(growingSimilarity>=90){
					scoreLevel = (int) -(89-growingSimilarity+0.01f);
					if(currentLevel < scoreLevel){
						score++;
						currentLevel++;
						if(scoreLevel<5)
							soundManager.play(Efectos.WIN3);
						else if(scoreLevel<8)
							soundManager.play(Efectos.WIN4);
						else if(scoreLevel<11)
							soundManager.play(Efectos.WIN5);
						else 
							soundManager.play(Efectos.WIN9);

					}
				}

			}else{
				allFadeOut();

				if(!retry&&growingSimilarity<90){
					soundManager.loop(Efectos.RETRY,0);
					soundManager.stop(Efectos.GO);
					checkingSimilarity = false;
					retry = true;
					((GameScene)(Director.instance().getScene())).menuLayer.show();
					//goButton.addAction(fadeIn( 0.75f ));
					//goButton.setDisabled(false);
				}else
					((GameScene)(Director.instance().getScene())).gameLayer.reset();
				
				checkingSimilarity = false;
			}
			
		}
		if (checkingSimilarity||retry){
			glyphPercent.setText(MenuLayer.bigFont, df.format(growingSimilarity)+"%");
			MenuLayer.bigFont.draw(batch, glyphPercent, getWidth()/2 - glyphPercent.width/2 , getHeight()/1.7f);
		}
		
		if (MenuLayer.loaded&&!loaded) {
			
			goButton = new TextButton("", MenuLayer.skin);
			goButton.setPosition(getWidth()/2 - 65*scale.x, getHeight ()- 140*scale.y);
		 	goButton.setColor(goalColor);
			goButton.setSize(130*scale.x,60*scale.y);
			goButton.addListener(new ChangeListener() {
					@Override
				    public void changed(ChangeEvent event, Actor actor) {
						if(!retry&&!checkingSimilarity && numberOfParticles()>minimumAmount){
							goButton.setDisabled(true);
							checkSimilarity();
							allFadeOut();
							soundManager.play(Efectos.GO);
						}
					}					
			 });
			 
			loaded = true;


			
		}
		
//	    MenuLayer.font.draw(batch, TEXT_FPS+Gdx.graphics.getFramesPerSecond(), 10*scale.x, 470*scale.y);
//		Color auxColor = averageColor();
//		String aux = "Current: "+df.format(auxColor.r)+"|"+df.format(auxColor.g)+"|"+df.format(auxColor.b)+"|";
//
//		String aux2 = "Goal: "+df.format(goalColor.r)+"|"+df.format(goalColor.g)+"|"+df.format(goalColor.b)+"|";
//
//		String aux3 = "Similarity: "+ df.format(100 - compare(auxColor, goalColor))+"%";
//
//		MenuLayer.font.draw(batch, aux, 10*scale.x, 300*scale.y);
//
//		MenuLayer.font.draw(batch, aux2, 10*scale.x, 280*scale.y);
//
//		MenuLayer.font.draw(batch, aux3, 10*scale.x, 450*scale.y);
		
		if(score!=0){
			glyphScore.setText(MenuLayer.bigFont, ""+score);
			MenuLayer.bigFont.draw(batch, ""+score, getWidth()/2 - glyphScore.width/2 , getHeight()- 20*scale.y);
		}
	}

	private void allFadeOut() {
		goButton.addAction(fadeOut( 0.75f ));
		goal.addAction(fadeOut( 0.75f ));
		currentColor.addAction(fadeOut( 0.75f ));
	}

	private void allFadeIn() {
		goButton.addAction(fadeIn( 0.75f ));
		goal.addAction(fadeIn( 0.75f ));
		currentColor.addAction(fadeIn( 0.75f ));
	}

	int numberOfParticles(){
		return ((GameScene)(Director.instance().getScene())).gameLayer.particleSystem.getParticleCount();
	}
	int numberOfColors(){
		
		colors.clear();
		
		Array<Color> total = ((GameScene)(Director.instance().getScene())).gameLayer.particleSystem.getParticleColorBuffer();
		
		Iterator<Color> iterator = total.iterator();
		
	      while(iterator.hasNext()) {
	    	  Color currentColor = (Color) iterator.next();
	    	  colors.add(currentColor);
	      }
		
	
		return colors.size();
		
	}
	
	
	public Color averageColor() {
		
		try {
			Array<Color> total = ((GameScene)(Director.instance().getScene())).gameLayer.particleSystem.getParticleColorBuffer();
			float sumr = 0, sumg = 0, sumb = 0,suma = 0;

			Iterator<Color> iterator = total.iterator();
			
			  while(iterator.hasNext()) {
				  Color currentColor = (Color) iterator.next();
				  sumr += currentColor.r;
			      sumg += currentColor.g;
			      sumb += currentColor.b;
			      suma += currentColor.a;
			  }
			  
			int num = total.size;
			return new Color(sumr / num, sumg / num, sumb / num,suma);
		} catch (Exception e) {
			return new Color(1,1,1,1);
		}
	}

//	float colorSimilarity(Color a,Color b){
//	    float distance = (b.r - a.r)*(b.r - a.r) + (b.g - a.g)*(b.g - a.g) + (b.b - a.b)*(b.b - a.b);
//	    return (float) (Math.sqrt(distance));
//	}

	float colorSimilarity(Color a,Color b){
		float distancer = (float) (Math.sqrt((b.r - a.r)*(b.r - a.r) ));
		float distanceg = (float) (Math.sqrt((b.g - a.g)*(b.g - a.g) ));
		float distanceb = (float) (Math.sqrt((b.b - a.b)*(b.b - a.b) ));
		//System.out.println("Distances: "+distancer+"-"+distanceg+"-"+distanceb);
		
		
	    return (float) (distancer+distanceg+distanceb);
	}
	
	
	
	public void checkSimilarity() {
		
		Color auxColor = averageColor();
		similarity = 100 - compare(auxColor, goalColor)/1.5f;
		checkingSimilarity = true;
	    GameLayer.playing = false;
	    
	}
	
	
	float lerp(float a, float b, float f)
	{
	    return a + f * (b - a);
	}
	
   public void reset(){
	   
	   	if(!MenuLayer.firstime  && !getChildren().contains(goButton, true))
			addActor(goButton);

	   	

	   	similarity  = 0;
	    growingSimilarity = 0;
	    scoreLevel = 0;
	    currentLevel = 0;
	    
	   // currentColor.setColor(new Color(GameLayer.color1.r,GameLayer.color1.g,GameLayer.color1.b,1));
	    setGoalColor();
		
		goal.setColor(goalColor);
    	scheduleExecutor.schedule(new Callable<Object>() {
	        public Object call() throws Exception {
	        	//allFadeIn();
				return "Called!";
	        }
	    },1,TimeUnit.SECONDS);	
		retry = false;
		scoreType = (int) getRandomNumberFrom(2, 10);
	    if (loaded){
	 	   goButton.setColor(goalColor);
		   goButton.setDisabled(false);
		   goButton.setText("");
		   allFadeIn();
	    }
    }
   
   
   private void setGoalColor() {
	   
	   
	   float min = 0.5f;
	   float max = 0.8f;
	   int aux0;
	   if(GameLayer.maxColors==3)
		   aux0 = randInt(1,3);
	   else
		   aux0 = randInt(1,2);

	   System.out.println(aux0);
	   Color aux, aux2;
	   float rand;
	   switch(aux0){
		   case 1:
			    aux = GameLayer.color1.cpy();
			    rand = getRandomNumberFrom(min,max);
			    goalColor = aux.lerp(GameLayer.color2,rand);
			    
			    if(GameLayer.maxColors==3){
				    rand = getRandomNumberFrom(min,max);
				    aux2 = goalColor.cpy();
				    goalColor = aux2.lerp(GameLayer.color3,rand);
			    }
			   break;
		   case 2:
			    aux = GameLayer.color2.cpy();
			    rand = getRandomNumberFrom(min,max);
			    goalColor = aux.lerp(GameLayer.color1,rand);
			    
			    if(GameLayer.maxColors==3){
				    rand = getRandomNumberFrom(min,max);
				    aux2 = goalColor.cpy();
				    goalColor = aux2.lerp(GameLayer.color3,rand);
			    }
			   break;
		   case 3:
			    aux = GameLayer.color3.cpy();
			    rand = getRandomNumberFrom(min,max);
			    goalColor = aux.lerp(GameLayer.color1,rand);
			    
			    if(GameLayer.maxColors==3){
				    rand = getRandomNumberFrom(min,max);
				    aux2 = goalColor.cpy();
				    goalColor = aux2.lerp(GameLayer.color2,rand);
			    }
			   break;
		    
	   }
	    
	    
		goalColor.a = 0.8f;
	
   }

   public int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}

   public float getRandomNumberFrom(float x, float y) {
       Random foo = new Random();
       float randomNumber = foo.nextFloat() * (x - y) + x;
       return randomNumber;

   }
   	public void toggleCurrentColor(final int i){
   		
   		currentColor.addAction( sequence( fadeOut( 0.2f ), new Action(){
   			
   		 public boolean act( float delta ) {
	   			if(i==1){
	   	   			currentColor.setColor(new Color(GameLayer.color1.r,GameLayer.color1.g,GameLayer.color1.b,1));
	   	   		}else{
	   	   			currentColor.setColor(new Color(GameLayer.color2.r,GameLayer.color2.g,GameLayer.color2.b,1));
	   	   		}
   		    
		    	return true;
		    }
   			
   		}, fadeIn( 0.2f )));
   		
   	}
   
	@Override
	public void enter() {
		super.enter();
	}


	@Override
	public void exit() {
		super.exit();
	}
	
	static float Kl = 1.0f;
	static float K1 = .045f;
	static float K2 = .015f;
	 
    public static float compare(Color a, Color b)
    {
    	//float[] rgbA = {a.r,a.g,a.b};
    	//float[] rgbB = {b.r,b.g,b.b};
    	
    	//CIELab cieLab = CIELab.getInstance();
    	//float[] labA = cieLab.fromRGB(rgbA);
    	//float[] labB = cieLab.fromRGB(rgbB);

    	LAB labA = LAB.fromRGBr(a.r, a.g, a.b, 1d);
    	LAB labB = LAB.fromRGBr(b.r, b.g, b.b, 1d);
    	
    	
    	
    	double deltaL = labA.L - labB.L;
        double deltaA = labA.a - labB.a;
        double deltaB = labA.b - labB.b;

        double c1 = Math.sqrt(labA.a * labA.a + labA.b * labA.b);
        double c2 = Math.sqrt(labB.a * labB.a + labB.b * labB.b);
        double deltaC = c1 - c2;

        double deltaH = deltaA * deltaA + deltaB * deltaB - deltaC * deltaC;
        deltaH = deltaH < 0 ? 0 : Math.sqrt(deltaH);

        float sl = 1.0f;
        float kc = 1.0f;
        float kh = 1.0f;

        double sc = 1.0f + K1 * c1;
        double sh = 1.0f + K2 * c1;

        double deltaLKlsl = deltaL / (Kl * sl);
        double deltaCkcsc = deltaC / (kc * sc);
        double deltaHkhsh = deltaH / (kh * sh);
        double i = deltaLKlsl * deltaLKlsl + deltaCkcsc * deltaCkcsc + deltaHkhsh * deltaHkhsh;
        return (float) (i < 0 ? 0 : Math.sqrt(i));
    }
    
    
    public float compare2(Color a, Color b)
    {
    	float[] rgbA = {a.r,a.g,a.b};
    	float[] rgbB = {b.r,b.g,b.b};
    	
    	CIELab cieLab = CIELab.getInstance();
    	float[] labA = cieLab.fromRGB(rgbA);
    	float[] labB = cieLab.fromRGB(rgbB);

    	
        float deltaL = labA[0] - labB[0];
        float deltaA = labA[1] - labB[1];
        float deltaB = labA[2] - labB[2];

        double c1 = Math.sqrt(labA[1] * labA[1] + labA[2] * labA[2]);
        double c2 = Math.sqrt(labB[1] * labB[1] + labB[2] * labB[2]);
        double deltaC = c1 - c2;

        double deltaH = deltaA * deltaA + deltaB * deltaB - deltaC * deltaC;
        deltaH = deltaH < 0 ? 0 : Math.sqrt(deltaH);

        float sl = 1.0f;
        float kc = 1.0f;
        float kh = 1.0f;

        double sc = 1.0f + K1 * c1;
        double sh = 1.0f + K2 * c1;

        float deltaLKlsl = deltaL / (Kl * sl);
        double deltaCkcsc = deltaC / (kc * sc);
        double deltaHkhsh = deltaH / (kh * sh);
        double i = deltaLKlsl * deltaLKlsl + deltaCkcsc * deltaCkcsc + deltaHkhsh * deltaHkhsh;
        return (float) (i < 0 ? 0 : Math.sqrt(i));
    }
	
	
	
	
	
	
}
