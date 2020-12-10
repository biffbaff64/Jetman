package com.richikin.jetman.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;

public class GameAudio
{
    private static final GameAudio _INSTANCE;

    private GameAudio()
    {
    }

    // Instance initialiser block
    static
    {
        try
        {
            _INSTANCE = new GameAudio();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static GameAudio inst()
    {
        return _INSTANCE;
    }

    private int     currentTune;
    private int     musicVolumeSave;
    private int     fxVolumeSave;
    private boolean soundsLoaded;
    private boolean isTunePaused;

    public void setup()
    {
        soundsLoaded = false;
        isTunePaused = false;

        AudioData.sounds = new Sound[AudioData.MAX_SOUND];
        AudioData.music  = new Music[AudioData.MAX_TUNES];

        loadSounds();

        if (musicVolumeSave == 0)
        {
            musicVolumeSave = AudioData._DEFAULT_MUSIC_VOLUME;
        }

        if (fxVolumeSave == 0)
        {
            fxVolumeSave = AudioData._DEFAULT_FX_VOLUME;
        }
    }

    public void update()
    {
        if (soundsLoaded)
        {
            if (AppConfig.gamePaused)
            {
                if ((AudioData.music[currentTune] != null) && AudioData.music[currentTune].isPlaying())
                {
                    AudioData.music[currentTune].pause();
                    isTunePaused = true;
                }
            }
            else
            {
                if ((AudioData.music[currentTune] != null) && !AudioData.music[currentTune].isPlaying() && isTunePaused)
                {
                    AudioData.music[currentTune].play();
                    isTunePaused = false;
                }
            }
        }
    }

    private void loadSounds()
    {
        Trace.__FILE_FUNC();

        AudioData.sounds[AudioData.SFX_LASER]          = App.assets.loadSingleAsset("data/sounds/laser.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_PLAZMA]         = App.assets.loadSingleAsset("data/sounds/plazma.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXPLOSION_1]    = App.assets.loadSingleAsset("data/sounds/explosion_1.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXPLOSION_2]    = App.assets.loadSingleAsset("data/sounds/explosion_2.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXPLOSION_3]    = App.assets.loadSingleAsset("data/sounds/explosion_3.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXPLOSION_4]    = App.assets.loadSingleAsset("data/sounds/explosion_4.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXPLOSION_5]    = App.assets.loadSingleAsset("data/sounds/explosion_5.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_THRUST]         = App.assets.loadSingleAsset("data/sounds/thrust3.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_PICKUP]         = App.assets.loadSingleAsset("data/sounds/pickup.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_TELEPORT]       = App.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_EXTRA_LIFE]     = App.assets.loadSingleAsset("data/sounds/extra_life.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_LAUNCH_WARNING] = App.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_TEST_SOUND]     = App.assets.loadSingleAsset("data/sounds/teleport.mp3", Sound.class);
        AudioData.sounds[AudioData.SFX_BEEP]           = App.assets.loadSingleAsset("data/sounds/pickup.mp3", Sound.class);

        AudioData.music[AudioData.MUS_TITLE]   = App.assets.loadSingleAsset("data/sounds/loseme2.mp3", Music.class);
        AudioData.music[AudioData.MUS_HISCORE] = App.assets.loadSingleAsset("data/sounds/breath.mp3", Music.class);
        AudioData.music[AudioData.MUS_GAME]    = App.assets.loadSingleAsset("data/sounds/fear_mon.mp3", Music.class);

        soundsLoaded = true;
    }

    private float getUsableVolume(int volume)
    {
//        volume /= _VOLUME_MULTIPLIER;
//
//        if (volume >= _MAX_VOLUME)
//        {
//            volume = _MAX_VOLUME;
//        }
//
//        return volumes[volume];

        return volume;
    }

    public void playTune(boolean play)
    {
        if (play)
        {
            startTune(currentTune, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    /**
     * Play or Stop the Main Game tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playGameTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(AudioData.MUS_GAME, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    /**
     * Play or Stop the Main Title tune.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playTitleTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(AudioData.MUS_TITLE, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    /**
     * Play or Stop the HiScore name entry tune.
     * This tune is played on the nname entry screen only,
     * NOT when the hiscore table is displayed in
     * the titles screen sequence.
     *
     * @param playTune TRUE to play, FALSE to stop playing.
     */
    public void playHiScoreTune(boolean playTune)
    {
        if (playTune)
        {
            startTune(AudioData.MUS_HISCORE, getMusicVolume(), true);
        }
        else
        {
            tuneStop();
        }
    }

    public void startTune(int musicNumber, int volume, boolean looping)
    {
        if (soundsLoaded)
        {
            if (getMusicVolume() > 0)
            {
                if (App.settings.isEnabled(Settings._MUSIC_ENABLED)
                    && (AudioData.music != null)
                    && !AudioData.music[musicNumber].isPlaying())
                {
                    AudioData.music[musicNumber].setLooping(looping);
                    AudioData.music[musicNumber].setVolume(getUsableVolume(volume));
                    AudioData.music[musicNumber].play();

                    currentTune = musicNumber;
                }
            }
        }
    }

    public long startSound(int soundNumber)
    {
        long id = 0;

        if (App.settings.isEnabled(Settings._SOUNDS_ENABLED) && soundsLoaded)
        {
            if (getFXVolume() > 0)
            {
                if (AudioData.sounds[soundNumber] != null)
                {
                    id = AudioData.sounds[soundNumber].play(getUsableVolume(getFXVolume()));
                }
            }
        }

        return id;
    }

    public void tuneStop()
    {
        if (soundsLoaded)
        {
            if ((AudioData.music[currentTune] != null) && AudioData.music[currentTune].isPlaying())
            {
                AudioData.music[currentTune].stop();
            }
        }
    }

    public void setMusicVolume(int volume)
    {
        if (AudioData.music[currentTune] != null)
        {
            AudioData.music[currentTune].setVolume(getUsableVolume(volume));
        }

//        App.settings.getPrefs().putInteger(Settings._MUSIC_VOLUME, volume);
//        App.settings.getPrefs().flush();
    }

    public void setFXVolume(int volume)
    {
//        App.settings.getPrefs().putInteger(Settings._FX_VOLUME, volume);
//        App.settings.getPrefs().flush();
    }

    public int getMusicVolume()
    {
        return AudioData._DEFAULT_MUSIC_VOLUME;
//        return App.settings.getPrefs().getInteger(Settings._MUSIC_VOLUME);
    }

    public int getFXVolume()
    {
        return AudioData._DEFAULT_FX_VOLUME;
//        return App.settings.getPrefs().getInteger(Settings._FX_VOLUME);
    }

    public float getUsableFxVolume()
    {
        return getFXVolume();
    }

    public void saveMusicVolume()
    {
        musicVolumeSave = getMusicVolume();
    }

    public void saveFXVolume()
    {
        fxVolumeSave = getFXVolume();
    }

    public int getMusicVolumeSave()
    {
        return musicVolumeSave;
    }

    public int getFXVolumeSave()
    {
        return fxVolumeSave;
    }

    public Sound[] getSoundsTable()
    {
        return AudioData.sounds;
    }

    public Music[] getMusicTable()
    {
        return AudioData.music;
    }

    public boolean isTunePlaying()
    {
        return soundsLoaded && AudioData.music[currentTune].isPlaying();
    }

    public boolean isTunePlaying(int _tune)
    {
        return soundsLoaded && AudioData.music[_tune].isPlaying();
    }
}
