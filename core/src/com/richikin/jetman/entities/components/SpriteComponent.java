package com.richikin.jetman.entities.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.maths.SimpleVec3F;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public interface SpriteComponent
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

    void addPhysicsBody();

    void setPositionfromBody();

    void addCollisionListener(ICollisionListener listener);

    void updateCollisionCheck();

    void updateCollisionBox();

    void wrap();
}
