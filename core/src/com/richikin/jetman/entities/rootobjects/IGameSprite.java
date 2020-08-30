package com.richikin.jetman.entities.rootobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.maths.SimpleVec3F;

public interface IGameSprite
{
    void initialise(EntityDescriptor entityDescriptor);

    void create(EntityDescriptor entityDescriptor);

    void initPosition(SimpleVec3F vec3F);

    void preUpdate();

    void update(int spriteNum);

    void updateCommon();

    void postUpdate(int spriteNum);

    void postMove();

    Vector3 getPosition();

    void draw(SpriteBatch spriteBatch);

    void animate();

    void setAnimation(EntityDescriptor entityDescriptor, float frameRate);

    void setAction(Actions action);

    Actions getSpriteAction();
}
