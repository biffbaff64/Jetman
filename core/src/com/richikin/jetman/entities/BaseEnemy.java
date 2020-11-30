package com.richikin.jetman.entities;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;

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
    }

    @Override
    public void preUpdate()
    {
    }

    @Override
    public void update(int spriteNum)
    {
    }

    @Override
    public void postUpdate(int spriteNum)
    {
    }

    @Override
    public void animate()
    {
    }
}
