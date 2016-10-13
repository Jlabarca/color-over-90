package com.jlabarca.director;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
 

public class MyPreferences
{
    private static final String level = "maxlevel";
    private static final String DEATH_NUM = "deaths";
    private static final String PREF_MUSIC_ENABLED = "musicOn";
    private static final String PREF_SOUND_ENABLED = "soundOn";
    private static final String PREFS_NAME = "WCPreferences";
    private static final String RESET_SAVE = "resetsave";

    Preferences prefs;


    protected Preferences getPrefs()
    {
    	if(prefs==null)
    		prefs = Gdx.app.getPreferences(PREFS_NAME);
    	
        return prefs;
    }
 
    public boolean isSoundEffectsEnabled()
    {
        return getPrefs().getBoolean( PREF_SOUND_ENABLED, true );
    }
 
    public void setSoundEffectsEnabled(
        boolean soundEffectsEnabled )
    {
        getPrefs().putBoolean( PREF_SOUND_ENABLED, soundEffectsEnabled );
        getPrefs().flush();
    }
 
    public boolean isMusicEnabled()
    {
        return getPrefs().getBoolean( PREF_MUSIC_ENABLED, true );
    }
 
    public void setMusicEnabled(
        boolean musicEnabled )
    {
        getPrefs().putBoolean( PREF_MUSIC_ENABLED, musicEnabled );
        getPrefs().flush();
    }
 
    public int getMaxLevel()
    {
        return getPrefs().getInteger( level, 1 );
    }
 
    public void setMaxLevel(
        int volume )
    {
        getPrefs().putInteger( level, volume );
        getPrefs().flush();
    }
    
    public int getDeaths()
    {
        return getPrefs().getInteger(DEATH_NUM, 0);
    }
 
    public void addDeaths(int n){
        getPrefs().putInteger(DEATH_NUM, getDeaths()+n);
        getPrefs().flush();
    }
    
    
    public void setLevelTime(int tlevel, float time ){
	    getPrefs().putFloat(tlevel+"time", time );
	    getPrefs().flush();
	}
    
    public float getLevelTime(int tlevel){
        return getPrefs().getFloat(tlevel+"time", 0 );
	}
    
    public void setLeveldeaths(int tlevel, int deaths){
	    getPrefs().putInteger(tlevel+"deaths", deaths );
	    getPrefs().flush();
	    System.out.println(tlevel +"---"+deaths);
	}
    
    public int getLevelDeaths(int tlevel)
    {
        return getPrefs().getInteger(tlevel+"deaths", 0);
    }
 
    public int getLevelScore(int tlevel){
    	return calculateLevelScore(tlevel, getLevelTime(tlevel), getLevelDeaths(tlevel));
	}
    public int calculateLevelScore(int tlevel,float time,int deaths){
    	int aux=0;
    	if(time!=0){//lo hace muchas veces por segundo
    		aux = (int) (tlevel*1000-deaths*10-10*TimeUnit.MILLISECONDS.toSeconds((long)time));
    	    //System.out.println("Score "+time+"-"+TimeUnit.MILLISECONDS.toSeconds((long)time));

    	}
    	return aux;
	}
    public int getGlobalScore(){
    	int aux = 0;
    	for(int i=0;i<getMaxLevel();i++)
    		aux+=getLevelScore(i);
        return aux;
	}
    
    
    public int getReset()
    {
        return getPrefs().getInteger( RESET_SAVE, 0 );
    }
    
    public void setReset(int reset ){
	    getPrefs().putInteger(RESET_SAVE, reset);
	    getPrefs().flush();
	}
}