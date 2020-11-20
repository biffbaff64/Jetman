package com.richikin.jetman;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.richikin.jetman.core.MainGame;

public class AndroidLauncher extends AndroidApplication
{
    private GoogleServices googleServices;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

        config.useImmersiveMode = true;
        config.useWakelock      = true;
        config.hideStatusBar    = true;
        config.useAccelerometer = false;

        // lock the current device orientation
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        googleServices = new GoogleServices(this);

        MainGame mainGame = new MainGame(googleServices);

        initialize(mainGame, config);

        Gdx.app.log("AndroidLauncher", "-------------------- APP START --------------------");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        googleServices.onActivityResult(requestCode, data);
    }
}
