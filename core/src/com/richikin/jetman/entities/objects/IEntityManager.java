package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.utils.Disposable;

public interface IEntityManager extends Disposable
{
    void initialise();

    void updateSprites();

    void drawSprites();

    void tidySprites();

    void releaseEntity(GdxSprite entity);

    void updateIndexes();

    boolean isEntityUpdateAllowed();

    @Override
    void dispose();
}
