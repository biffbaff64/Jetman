package com.richikin.jetman.entities.rootobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.maths.SimpleVec3F;
import com.richikin.jetman.physics.ICollisionListener;

public interface IGameSprite
{
    void initialise(SpriteDescriptor entityDescriptor);

    void create(SpriteDescriptor entityDescriptor);

    void initPosition(SimpleVec3F vec3F);

    void preUpdate();

    void update(int spriteNum);

    void updateCommon();

    void postUpdate(int spriteNum);

    void postMove();

    Vector3 getPosition();

    void draw(SpriteBatch spriteBatch);

    void animate();

    void setAnimation(SpriteDescriptor entityDescriptor);

    void setAnimation(SpriteDescriptor entityDescriptor, float frameRate);

    Actions getSpriteAction();

    Rectangle getCollisionRectangle();

    void addCollisionListener(ICollisionListener listener);

    void updateCollisionCheck();

    void updateCollisionBox();
}
