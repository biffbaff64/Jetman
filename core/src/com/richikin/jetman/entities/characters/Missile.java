package com.richikin.jetman.entities.characters;

import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.physics.Movement;

public class Missile extends GdxSprite
{
    public Missile(App _app)
    {
        super(_app);
    }

    public void explode()
    {
    }

    @Override
    public void tidy(int _index)
    {
        if (!app.gameProgress.roverDestroyed
            && !app.gameProgress.baseDestroyed
            && (direction.getY() != Movement._DIRECTION_UP))
        {
            app.getHud().getTimeBar().setToMaximum();
            app.getHud().getFuelBar().setToMaximum();
            app.getHud().update();

            app.getBase().setAction(Actions._STANDING);
        }

        app.entityData.removeEntity(_index);
    }
}
