package com.jlabarca.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.jlabarca.sound.LRUCache.CacheEntryRemovedListener;
import com.jlabarca.sound.SoundManager.Efectos;

/**
 * A service that manages the sound effects.
 */
public class SoundManager
    implements
        CacheEntryRemovedListener<Efectos,Sound>,
        Disposable
{
    /**
     * The available sound files.
     */
    public enum Efectos
    {
        WIN( "sounds/win.mp3" ),
        WIN2( "sounds/win2.mp3" ),
        WIN3( "sounds/win3.mp3" ),
        WIN4( "sounds/win4.mp3" ),
        WIN5( "sounds/win5.mp3" ),
        WIN9( "sounds/win9.mp3" ),
        GO( "sounds/go.mp3" ),
        RETRY( "sounds/retry.mp3" );

        //GLASS( "sound/glassbreak.mp3" );

        private final String fileName;

        private Efectos(
            String fileName )
        {
            this.fileName = fileName;
        }

        public String getFileName()
        {
            return fileName;
        }
    }
    
    public Sound soundToPlay;

    /**
     * The volume to be set on the sound.
     */
    private float volume = 0.5f;

    /**
     * Whether the sound is enabled.
     */
    private boolean enabled = true;

    /**
     * The sound cache.
     */
    private final LRUCache<Efectos,Sound> soundCache;

    /**
     * Creates the sound manager.
     */
    public SoundManager()
    {
        soundCache = new LRUCache<SoundManager.Efectos,Sound>( 20 );
        soundCache.setEntryRemovedListener( this );
        for (Efectos d : Efectos.values()) {
        	FileHandle soundFile = Gdx.files.internal( d.getFileName() );
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add( d, soundToPlay );
        }
    }

    /**
     * Plays the specified sound.
     */
    public void play(
        Efectos sound )
    {
        // check if the sound is enabled
        if( ! enabled ) return;

        // try and get the sound from the cache
        soundToPlay = soundCache.get( sound );
        if( soundToPlay == null ) {
            FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add( sound, soundToPlay );

        }
        if(soundToPlay == null)
            Gdx.app.log("fic","not loaded sound: " + sound.name() );

        // play the sound
        Gdx.app.log("fic","Playing sound: " + sound.name() );
        soundToPlay.play( volume );
    }
    public void playsolo(
            Efectos sound )
        {
    	
    	 if( ! enabled ) return;

         // try and get the sound from the cache
         soundToPlay = soundCache.get( sound );
         if( soundToPlay == null ) {
             FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
             soundToPlay = Gdx.audio.newSound( soundFile );
             soundCache.add( sound, soundToPlay );

         }
         if(soundToPlay == null)
             Gdx.app.log("fic","not loaded sound: " + sound.name() );

         // play the sound
         Gdx.app.log("fic","Playing sound: " + sound.name() );
         
         soundToPlay.play(volume);
     }
    
    public void pitch(
            Efectos sound , float pitch,long aux)
        {
            // check if the sound is enabled
            if( ! enabled ) return;

            // try and get the sound from the cache
            Sound soundToPlay = soundCache.get( sound );
            if( soundToPlay == null ) {
                FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
                soundToPlay = Gdx.audio.newSound( soundFile );
                soundCache.add( sound, soundToPlay );
            }

            soundToPlay.setPitch(aux, pitch);
        }
    
    public long loop(
            Efectos sound , float pitch )
        {
            // check if the sound is enabled
            if( ! enabled ) return 0;

            // try and get the sound from the cache
            Sound soundToPlay = soundCache.get( sound );
            if( soundToPlay == null ) {
                FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
                soundToPlay = Gdx.audio.newSound( soundFile );
                soundCache.add( sound, soundToPlay );
            }
            Gdx.app.log("fic","Looping sound: " + sound.name() );

            long aux = soundToPlay.play(volume);
            //soundToPlay.setPitch(aux,pitch);
            soundToPlay.setLooping(aux, true);
			return aux;
        }
    public void stop(
            Efectos sound )
        {
            if( ! enabled ) return;

            Sound soundToPlay = soundCache.get( sound );
            if(soundToPlay!= null)
            	soundToPlay.stop();
            
        }
            
    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    public void setVolume(
        float volume )
    {
      //  Gdx.app.log( Tyrian.LOG, "Adjusting sound volume to: " + volume );

        // check and set the new volume
        if( volume < 0 || volume > 1f ) {
            throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
        }
        this.volume = volume;
    }

    /**
     * Enables or disabled the sound.
     */
    public void setEnabled(
        boolean enabled )
    {
        this.enabled = enabled;
    }

    // EntryRemovedListener implementation

    @Override
    public void notifyEntryRemoved(
        Efectos key,
        Sound value )
    {
       // Gdx.app.log( Tyrian.LOG, "Disposing sound: " + key.name() );
        value.dispose();
    }

    /**
     * Disposes the sound manager.
     */
    public void dispose()
    {
      //  Gdx.app.log( Tyrian.LOG, "Disposing sound manager" );
        for( Sound sound : soundCache.retrieveAll() ) {
            sound.stop();
            sound.dispose();
        }
    }
}
