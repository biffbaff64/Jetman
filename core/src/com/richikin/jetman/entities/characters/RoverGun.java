package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateManager;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.maths.SimpleVec2F;

public class RoverGun extends GdxSprite
{
    public boolean     isAttachedToPlayer;
    public SimpleVec2F releaseXY;

    public RoverGun(App _app)
    {
        super(_app);
    }

    public void startShooting()
    {
    }
}
