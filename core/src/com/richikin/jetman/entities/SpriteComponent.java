package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.maths.SimpleVec3;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public interface SpriteComponent
{
    void initialise(SpriteDescriptor descriptor);

    void create(SpriteDescriptor descriptor);

    void initPosition(SimpleVec3 vec3F);

    void preUpdate();

    void update(int spriteNum);

    void updateCommon();

    void postUpdate(int spriteNum);

    void postMove();

    Vector3 getPosition();

    void preDraw();

    void draw(SpriteBatch spriteBatch);

    void animate();

    void setAnimation(SpriteDescriptor descriptor);

    void setAnimation(SpriteDescriptor descriptor, float frameRate);

    void addDynamicPhysicsBody();

    void addKinematicPhysicsBody();

    void setPositionfromBody();

    void addCollisionListener(ICollisionListener listener);

    void updateCollisionCheck();

    void updateCollisionBox();

    void wrap();
}
