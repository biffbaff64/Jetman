package com.richikin.jetman.ecs.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.richikin.jetman.physics.aabb.CollisionObject;

public class PhysicsComponent implements Component
{
    public Body            b2dBody;
    public CollisionObject collisionObject;
    public short           bodyCategory;
    public short           collidesWith;
}
