package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateManager;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.maths.SimpleVec2F;

public class Bomb extends GdxSprite
{
    public SimpleVec2F releaseXY;
    public boolean isAttachedToPlayer;
    public boolean isAttachedToRover;

    public Bomb(App _app)
    {
        super(_app);
    }

    public void explode()
    {

    }
}
