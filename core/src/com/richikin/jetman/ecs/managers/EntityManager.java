package com.richikin.jetman.ecs.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
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
import com.richikin.jetman.entities.managers.PlayerManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.IEntityManager;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.jetman.entities.systems.RenderSystem;
import com.richikin.jetman.maps.RoomManager;

public class EntityManager implements IEntityManager
{
    // --------------------------------------------------
    //
    public static final Array<GraphicID> enemies;

    static
    {
        enemies = new Array<>();

        enemies.add(GraphicID.G_3BALLS_UFO);
        enemies.add(GraphicID.G_3LEGS_ALIEN);
        enemies.add(GraphicID.G_ASTEROID);
        enemies.add(GraphicID.G_ALIEN_WHEEL);
        enemies.add(GraphicID.G_BLOB);
        enemies.add(GraphicID.G_DOG);
        enemies.add(GraphicID.G_GREEN_BLOCK);
        enemies.add(GraphicID.G_SPINNING_BALL);
        enemies.add(GraphicID.G_STAIR_CLIMBER);
        enemies.add(GraphicID.G_STAR_SPINNER);
        enemies.add(GraphicID.G_TOPSPIN);
        enemies.add(GraphicID.G_TWINKLES);
    }

    // --------------------------------------------------
    // Indexes into manager list
    public int _bombManagerIndex;
    public int _alienManagerIndex;

    // --------------------------------------------------
    // Indexes into entity list
    public int   _playerIndex;
    public int[] _teleportIndex;

    public TeleportBeam  teleportBeam;
    public PlayerManager playerManager;

    public boolean _playerReady;

    public Engine engine;

    public EntityManager()
    {
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

    @Override
    public void initialise()
    {
        engine = new PooledEngine();

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new CollisionSystem(null));
        engine.addSystem(new PhysicsDebugSystem(null));
        engine.addSystem(new PhysicsSystem(null));
        engine.addSystem(new PlayerControlSystem(null));
        engine.addSystem(new RenderingSystem());

        this._teleportIndex = new int[RoomManager._MAX_TELEPORTERS];
    }

    @Override
    public void updateSprites()
    {
    }

    @Override
    public void drawSprites()
    {
    }

    @Override
    public void tidySprites()
    {
    }

    @Override
    public void releaseEntity(GdxSprite entity)
    {
    }

    @Override
    public void updateIndexes()
    {
    }

    @Override
    public boolean isEntityUpdateAllowed()
    {
        return false;
    }

    public void initialiseForLevel()
    {
    }

    /**
     * Background entities which are essentially just
     * decorations, such as ufos and twinkling stars.
     */
    private void addBackgroundEntities()
    {
    }

    @Override
    public void dispose()
    {
    }
}
