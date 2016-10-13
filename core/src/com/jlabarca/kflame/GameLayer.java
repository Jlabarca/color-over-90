package com.jlabarca.kflame;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.jlabarca.definition.AppActorTextures;
import com.jlabarca.director.Director;
import com.jlabarca.director.Layer;
import com.jlabarca.sound.MusicManager.Musica;
import com.jlabarca.texture.TextureCache;
import com.jlabarca.texture.TextureDefinition;

import finnstr.libgdx.liquidfun.ParticleDef.ParticleType;
import finnstr.libgdx.liquidfun.ParticleGroupDef;
import finnstr.libgdx.liquidfun.ParticleSystem;
import finnstr.libgdx.liquidfun.ParticleSystemDef;

public class GameLayer extends Layer {

    private final static float BOX_TO_WORLD = 120.0f;
    private final static float WORLD_TO_BOX = 1f / BOX_TO_WORLD;

    private OrthographicCamera camera;
    private World world;
    public ParticleSystem particleSystem;
    private ParticleDebugRenderer2 mParticleDebugRenderer;
    private Box2DDebugRenderer mDebugRenderer;
    private ColorParticleRenderer mColorParticleRenderer;
    private ParticleGroupDef mParticleGroupDef1,mParticleGroupDef2,mParticleGroupDef3;
	float prevAccelX=0,prevAccelY=0;
    private long diff, start = System.currentTimeMillis();
    public static Vector2 scale;
    public static boolean Switch = false;
    private Texture backgroundTexture;
    private Sprite bottleSprite;
	public static Color color1, color2, color3;
	ScheduledExecutorService scheduleExecutor;
	public static boolean playing = true;
	Body bottleModel;
	Vector2 bottleModelOrigin;
	Vector2 bottleScale;
	int levelUP = 50; // puntaje al cual empiezan a salir 3 colores
	int toggleColor = 1;
	static int maxColors = 2;
	
    public GameLayer(float width, float height)
	{
		this.setWidth(width);
		this.setHeight(height);
     
		scale = new Vector2(width/320f,height/480f);
		scheduleExecutor = Executors.newSingleThreadScheduledExecutor();
        camera = new OrthographicCamera(width, height);
        camera.position.set(width / 2, height / 2, 0);
        //camera.zoom = 2.5f;
        camera.update();
        Gdx.input.setInputProcessor(this);
    	TextureCache textureCache = TextureCache.instance();
		TextureDefinition definition = textureCache.getDefinition(AppActorTextures.TEXTURE_GLASS);
		backgroundTexture = textureCache.getTexture(definition).getTexture();
		bottleSprite =new Sprite(backgroundTexture);
	    bottleSprite.setSize(2f*scale.x*BOX_TO_WORLD,2.6f*scale.y*BOX_TO_WORLD);
        world = new World(new Vector2(0, -15f), false);
        createBottle();
        setRandomColors();  
        createParticleStuff();

        /* Render stuff */
        mDebugRenderer = new Box2DDebugRenderer();
        mParticleDebugRenderer = new ParticleDebugRenderer2(particleSystem.getParticleColorBuffer().get(0), 2000);
        mColorParticleRenderer = new ColorParticleRenderer(2000);

        /* Version */
        Gdx.app.log("Running LiquidFun version", particleSystem.getVersionString());
        updateLog();
        

    }
    private void createBottle() {
        // 0. Create a loader for the file saved from the editor.
        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("glass.json"));
     
        // 1. Create a BodyDef, as usual.
        BodyDef bd = new BodyDef();
        bd.position.set(0.3333f*scale.x, 0.01f*scale.y);
        bd.type = BodyType.StaticBody;
     
        // 2. Create a FixtureDef, as usual.
        FixtureDef fd = new FixtureDef();
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;
     
        // 3. Create a Body, as usual.
        bottleModel = world.createBody(bd);
        // 4. Create the body fixture automatically by using the loader.
        loader.attachFixture(bottleModel, "glass", fd,1,2f*scale.x,1.2f*scale.y);
       // bottleModel.setTransform(0, 0, 0);
        bottleModelOrigin = loader.getOrigin("glass", scale.x).cpy();
    }
    

    private void createParticleStuff() {
        //First we create a new particlesystem and 
        //set the radius of each particle to 6 / 120 m (5 cm)
        ParticleSystemDef systemDef = new ParticleSystemDef();
        systemDef.colorMixingStrength = 1f;
        systemDef.radius = 3*scale.x * WORLD_TO_BOX ;
        //systemDef.dampingStrength = 5f;
        systemDef.density = scale.y*1;
        particleSystem = new ParticleSystem(world, systemDef);
        particleSystem.setParticleDensity(.3f);
        particleSystem.setParticleMaxCount(1000);
    
        
        //Create a new particlegroupdefinition and set some properties
        //For the flags you can set more than only one
        mParticleGroupDef1 = new ParticleGroupDef();
        mParticleGroupDef1.color.set(color1);
        mParticleGroupDef1.flags.add(ParticleType.b2_colorMixingParticle);
        mParticleGroupDef1.position.set(getWidth() * (30f / 100f) * WORLD_TO_BOX, getHeight() * WORLD_TO_BOX);

        
        CircleShape parShape = new CircleShape();
        parShape.setRadius(0.2f);
        mParticleGroupDef1.shape = parShape;


        mParticleGroupDef2 = new ParticleGroupDef();
        mParticleGroupDef2.shape = mParticleGroupDef1.shape;
        mParticleGroupDef2.flags.add(ParticleType.b2_colorMixingParticle);
        mParticleGroupDef2.position.set(getWidth() * (70f / 100f) * WORLD_TO_BOX, getHeight()  * WORLD_TO_BOX);
        mParticleGroupDef2.color.set(color2);
        mParticleGroupDef2.shape = parShape;
        
        mParticleGroupDef3 = new ParticleGroupDef();
        mParticleGroupDef3.shape = mParticleGroupDef1.shape;
        mParticleGroupDef3.flags.add(ParticleType.b2_colorMixingParticle);
        mParticleGroupDef3.position.set(getWidth() * (50 / 100f) * WORLD_TO_BOX, getHeight()  * WORLD_TO_BOX);
        mParticleGroupDef3.color.set(color3);
        mParticleGroupDef3.shape = parShape;

    	world.setGravity(new Vector2(0,-15));

        if(MenuLayer.firstime){
        	for(int i=0;i<50;i++){
	        	 particleSystem.createParticleGroup(mParticleGroupDef1);
	             particleSystem.createParticleGroup(mParticleGroupDef2);
        	}
        	
        }else{
        	 particleSystem.createParticleGroup(mParticleGroupDef1);
             particleSystem.createParticleGroup(mParticleGroupDef2);
             if(maxColors==3)
                 particleSystem.createParticleGroup(mParticleGroupDef3);
        }
        
        
        
         mParticleGroupDef1.linearVelocity.set(new Vector2(0, -1f));
         mParticleGroupDef2.linearVelocity.set(new Vector2(0, -1f));
    }

    public void sleep(int fps) {
        if(fps>0){
          diff = System.currentTimeMillis() - start;
          long targetDelay = 1000/fps;
          if (diff < targetDelay) {
            try{
                Thread.sleep(targetDelay - diff);
              } catch (InterruptedException e) {}
            }   
          start = System.currentTimeMillis();
        }
    }


    
    private void processAccelerometer() {
        float y = Gdx.input.getAccelerometerY();
        float x = Gdx.input.getAccelerometerX();
        if ((prevAccelX != x) || prevAccelY != y) {
                world.setGravity(new Vector2(-10*x,-10*y));//Negative on the x, but not on the Y. Somewhat geocentric view.
                prevAccelX = x;
                prevAccelY = y;
        }
    }
    
    @Override
	public void exit()
	{
        mParticleGroupDef1.shape.dispose();
        mParticleGroupDef2.shape.dispose();
        if(mParticleGroupDef3!=null)
        	mParticleGroupDef3.shape.dispose();
        world.dispose();
        mDebugRenderer.dispose();
    }

  

    private void createParticles(float pX, float pY, ParticleGroupDef mParticleGroupDef,Musica sound) {
    	mParticleGroupDef.position.set(pX * WORLD_TO_BOX, pY * WORLD_TO_BOX);
        particleSystem.createParticleGroup(mParticleGroupDef);
        updateParticleCount();
        updateLog();
        FpsLayer.musicManager.play(sound);

    }
    


    private void updateParticleCount() {
        if(particleSystem.getParticleCount() > mParticleDebugRenderer.getMaxParticleNumber()) {
            mParticleDebugRenderer.setMaxParticleNumber(particleSystem.getParticleCount() + 1000);
        }  
    }

    public void updateLog() {
        //Here we log the total particle count and the f/s
        Gdx.app.log("", "Total particles: " + particleSystem.getParticleCount() + " FPS: " + Gdx.graphics.getFramesPerSecond());
    }

    public void createCircleBody(float pX, float pY, float pRadius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(pX * WORLD_TO_BOX, pY * WORLD_TO_BOX);
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(pRadius * WORLD_TO_BOX);

        FixtureDef fixDef = new FixtureDef();
        fixDef.density = 5.5f;
        fixDef.friction = 0.2f;
        fixDef.shape = shape;
        fixDef.restitution = 0.3f;

        body.createFixture(fixDef);
    }

    /* +++ Input +++ */ 

    private final float CREATE_PARTICLE_FREQUENCY = 30f;
    private float mTotDelta = 0;
    private boolean mCreateParticles = false;
    private float mPointerPosX = 0;
    private float mPointerPosY = 0;
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT && button != Input.Buttons.RIGHT && button != Input.Buttons.MIDDLE) return false;

        if(button == Input.Buttons.MIDDLE) {
            this.createCircleBody(screenX, Gdx.graphics.getHeight() - screenY, MathUtils.random(10, 80));
            return true;
        }
        if(playing){
	        mCreateParticles = true;
	        mTotDelta = 0;
	        mPointerPosX = screenX;
	        mPointerPosY = screenY;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT && button != Input.Buttons.RIGHT && button != Input.Buttons.MIDDLE) return false;
        toggleColor++;
        if(toggleColor>=maxColors+1)
        	toggleColor = 1;
        mCreateParticles = false;
        FpsLayer.musicManager.stop();
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(!mCreateParticles) return false;

        mPointerPosX = screenX;
        mPointerPosY = screenY;

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public void inputUpdate(float pDelta) {
        if(!mCreateParticles) return;
        mTotDelta += pDelta;
        
        if(mTotDelta >= 1f / CREATE_PARTICLE_FREQUENCY) {
            mTotDelta -= 1 / CREATE_PARTICLE_FREQUENCY;
        } else return;

        float x = mPointerPosX + MathUtils.random(-Gdx.graphics.getWidth() * (1.5f/100f), Gdx.graphics.getWidth() * (1.5f/100f));
        float y = mPointerPosY + MathUtils.random(-Gdx.graphics.getHeight() * (1.5f/100f), Gdx.graphics.getHeight() * (1.5f/100f));
        
        
        switch(toggleColor){
	        case 1:
	        	createParticles(x, Gdx.graphics.getHeight() - y ,mParticleGroupDef1 , Musica.LIQUID1);
	    		break;
	        case 2:
	        	createParticles(x, Gdx.graphics.getHeight() - y ,mParticleGroupDef2 , Musica.LIQUID2);
	    		break;
	        case 3:
	        	createParticles(x, Gdx.graphics.getHeight() - y ,mParticleGroupDef3 , Musica.LIQUID3);
	    		break;
        
        }
    	
    }

    
    void destroyFarParticles(){
    	
    	Array<Vector2> positions = particleSystem.getParticlePositionBuffer();
    	
    	for(int i = 0; i < positions.size; i++)
    		if(positions.get(i).y >10 || positions.get(i).y < -1)
    	        particleSystem.destroyParticle(i);

    }
    
    public Color getRandomColor(){
    	Random generator = new Random();
    	return new Color(generator.nextFloat() *0.99f,generator.nextFloat() *0.99f,generator.nextFloat() *0.99f,0.4f);
    }
    
	public int getRandomNumberFrom(int min, int max) {
        Random foo = new Random();
        int randomNumber = foo.nextInt((max + 1) - min) + min;
        return randomNumber;
    }
	
	private void setRandomColors() {
		
		float aux;
        do{
    		color1 = getRandomColor();
    		color2 = getRandomColor();
    		aux = 100 - FpsLayer.compare(color1, color2);
        }while(aux > 10);
		
        if(FpsLayer.score < levelUP)
        	color3 = new Color(1,1,1,1);
        else{
        	float aux2;
        	 do{
             	color3 = getRandomColor();
         		aux = 100 - FpsLayer.compare(color3, color1);
         		aux2 = 100 - FpsLayer.compare(color3, color2);
             }while(aux > 10 && aux2 > 10);
        }
	}
	
	
    public void reset(){
    	
    	toggleColor = 1;
    	playing = false;
    	world.setGravity(new Vector2(0,80));
    	
    	scheduleExecutor.schedule(new Callable<Object>() {
	        public Object call() throws Exception {
	        	world.setGravity(new Vector2(0,-15));
	        	playing = true;
	        	particleSystem.destroyParticleSystem();
	        	setRandomColors();
	            createParticleStuff();	
	        	((GameScene)(Director.instance().getScene())).fpsLayer.reset();
				return "Called!";
	        }
		
	    },1500,TimeUnit.MILLISECONDS);	
    	
    }
    
    @Override
	public void draw(Batch batch, float parentAlpha)
	{
        //First update our InputProcessor
        //backgroundSprite.draw(batch);

    	if(FpsLayer.score>levelUP)
    		maxColors = 3;
    	else
    		maxColors = 2;	
    	
    	
   	 	Vector2 bottlePos = bottleModel.getPosition();
	 
	    bottleSprite.setPosition(bottlePos.x*BOX_TO_WORLD, bottlePos.y*BOX_TO_WORLD);
	    //bottleSprite.setOrigin(bottleModelOrigin.x/WORLD_TO_BOX, bottleModelOrigin.y/WORLD_TO_BOX);
	    bottleSprite.setRotation(bottleModel.getAngle() * MathUtils.radiansToDegrees);
	
	    bottleSprite.draw(batch);
    	
    	
        this.inputUpdate(Gdx.graphics.getDeltaTime()/2.5f);
        //sleep(30);
        //Now do the same as every year...
        Gdx.gl.glClearColor(1f, 1f, 1f, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        world.step(Gdx.graphics.getDeltaTime(), 2, 6, particleSystem.calculateReasonableParticleIterations(Gdx.graphics.getDeltaTime()));
        batch.setProjectionMatrix(camera.combined);



        Matrix4 cameraCombined = camera.combined.cpy();
        cameraCombined.scale(BOX_TO_WORLD, BOX_TO_WORLD, 1);

        //mDebugRenderer.render(world, cameraCombined);
        mColorParticleRenderer.render(particleSystem, BOX_TO_WORLD, cameraCombined);
        mParticleDebugRenderer.render(particleSystem, BOX_TO_WORLD, cameraCombined);

        destroyFarParticles();
        if(playing)
	        switch (Gdx.app.getType()) {
				case Android:
					processAccelerometer();	
					break;
				case Desktop:
					//if(Gdx.input.getDeltaX()!=0&&Gdx.input.getDeltaY()!=0)
					//	mWorld.setGravity(new Vector2(Gdx.input.getDeltaX()/7,-Gdx.input.getDeltaY()/7));
					break;
				default:
					break;
	        }
    }
    
    
    @Override
	public boolean keyDown(int keycode)
	{
		boolean handled = false;
	
		switch(keycode) {
	
		 case Keys.BACK: case Keys.ESCAPE: 
			((GameScene)Director.instance().getScene()).menuLayer.show();
			 handled = true;
		     break;


		 }
		return handled;
	}

}