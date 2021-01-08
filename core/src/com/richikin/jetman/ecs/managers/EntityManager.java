package com.richikin.jetman.ecs.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.richikin.jetman.ecs.components.AnimationComponent;
import com.richikin.jetman.ecs.components.IDComponent;
import com.richikin.jetman.ecs.components.PhysicsComponent;
import com.richikin.jetman.ecs.components.PlayerComponent;
import com.richikin.jetman.ecs.components.SpriteComponent;
import com.richikin.jetman.ecs.components.StateComponent;
import com.richikin.jetman.ecs.components.TransformComponent;
import com.richikin.jetman.ecs.systems.AnimationSystem;
import com.richikin.jetman.ecs.systems.CollisionSystem;
import com.richikin.jetman.ecs.systems.PhysicsDebugSystem;
import com.richikin.jetman.ecs.systems.PhysicsSystem;
import com.richikin.jetman.ecs.systems.PlayerControlSystem;
import com.richikin.jetman.ecs.systems.RenderingSystem;

public class EntityManager
{
    public Engine engine;

    public EntityManager()
    {
    }

    public void initialiseECS()
    {
        engine = new PooledEngine();

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CollisionSystem(null));
        engine.addSystem(new PhysicsDebugSystem(null));
        engine.addSystem(new PhysicsSystem(null));
        engine.addSystem(new PlayerControlSystem(null));
        engine.addSystem(new RenderingSystem());
    }

    public void addPlayer()
    {
        Entity entity = engine.createEntity();

        entity.add(engine.createComponent(PlayerComponent.class));
        entity.add(engine.createComponent(IDComponent.class));
        entity.add(engine.createComponent(AnimationComponent.class));
        entity.add(engine.createComponent(PhysicsComponent.class));
        entity.add(engine.createComponent(SpriteComponent.class));
        entity.add(engine.createComponent(StateComponent.class));
        entity.add(engine.createComponent(TransformComponent.class));

        engine.addEntity(entity);
    }

    public Engine getEngine()
    {
        return engine;
    }
}
