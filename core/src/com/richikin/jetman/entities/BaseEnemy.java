package com.richikin.jetman.entities;

import com.badlogic.gdx.Gdx;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.GenericCollisionListener;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.physics.aabb.ICollisionListener;

// TODO: 03/12/2020 - Eventually all enemy entities must extend from this class
// TODO: 03/12/2020 - This class should handle all common enemy actions

public class BaseEnemy extends GdxSprite
{
    public BaseEnemy(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        setAction(ActionStates._STANDING);
    }

    @Override
    public void preUpdate()
    {
    }

    @Override
    public void update(int spriteNum)
    {
        animate();

        updateCommon();
    }

    @Override
    public void postUpdate(int spriteNum)
    {
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

    @Override
    public void addCollisionListener(ICollisionListener listener)
    {
        super.addCollisionListener(new GenericCollisionListener(this));
    }
}
