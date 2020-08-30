package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.rootobjects.GameEntity;
import com.richikin.jetman.graphics.Gfx;

public class BodyBuilder
{
    private final App app;

    public BodyBuilder(App _app)
    {
        this.app = _app;
    }

    /**
     * Creates a Dynamic Box2D body which can be assigned to a GdxSprite.
     *
     * Dynamic bodies are objects which move around and are affected by
     * forces and other dynamic, kinematic and static objects. Dynamic
     * bodies are suitable for any object which needs to move and be
     * affected by forces.
     *
     * @param entity      The GdxSprite of this entity
     * @param density     Object density
     * @param restitution The object restitution.
     */
    public void createDynamicCircle(GameEntity entity, float density, float friction, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.DynamicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.position.x + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.position.y + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius((entity.frameWidth / 2) / Gfx._PPM);

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = friction;
        fixtureDef.filter.categoryBits  = entity.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.gid, entity.type));

        shape.dispose();
    }

    public void createDynamicPolygon(GameEntity entity, float density, float friction, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.DynamicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.position.x + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.position.y + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((entity.frameWidth / 2) / Gfx._PPM),
                ((entity.frameHeight / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = friction;
        fixtureDef.filter.categoryBits  = entity.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.gid, entity.type));

        shape.dispose();
    }

    /**
     * Creates a Kinematic Box2D body which can be assigned to a GdxSprite.
     *
     * Kinematic bodies are somewhat in between static and dynamic bodies.
     * Like static bodies, they do not react to forces, but like dynamic bodies,
     * they do have the ability to move. Kinematic bodies are great for things
     * where you, the programmer, want to be in full control of a body's motion,
     * such as a moving platform in a platform game.
     * It is possible to set the position on a kinematic body directly, but it's
     * usually better to set a velocity instead, and letting Box2D take care of
     * position updates.
     *
     * @param entity      The GdxSprite of this entity
     * @param density     Object density
     * @param restitution The object restitution.
     */
    public void createKinematicBody(GameEntity entity, float density, float restitution)
    {
        entity.bodyDef = new BodyDef();
        entity.bodyDef.type = BodyDef.BodyType.KinematicBody;
        entity.bodyDef.fixedRotation = true;
        entity.bodyDef.position.set
            (
                (entity.position.x + (entity.frameWidth / 2)) / Gfx._PPM,
                (entity.position.y + (entity.frameHeight / 2)) / Gfx._PPM
            );

        entity.b2dBody = app.worldModel.box2DWorld.createBody(entity.bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((entity.frameWidth / 2) / Gfx._PPM),
                ((entity.frameHeight / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = density;
        fixtureDef.restitution          = restitution;
        fixtureDef.friction             = 0;
        fixtureDef.filter.categoryBits  = entity.bodyCategory;
        fixtureDef.filter.maskBits      = entity.collidesWith;

        entity.b2dBody.createFixture(fixtureDef);
        entity.b2dBody.setUserData(new BodyIdentity(entity, entity.gid, entity.type));

        shape.dispose();
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite.
     *
     * Static bodies are objects which do not move and are not affected by forces.
     * Dynamic bodies are affected by static bodies. Static bodies are perfect for
     * ground, walls, and any object which does not need to move. Static bodies
     * require less computing power.
     *
     * @param _entity The {@link GameEntity} to extract properties from.
     */
    public Body createStaticBody(GameEntity _entity)
    {
        return createStaticBody(_entity, 1.0f, 1.0f, 0.15f);
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite.
     *
     * Static bodies are objects which do not move and are not affected by forces.
     * Dynamic bodies are affected by static bodies. Static bodies are perfect for
     * ground, walls, and any object which does not need to move. Static bodies
     * require less computing power.
     *
     * @param _entity The {@link GameEntity} to extract properties from.
     * @param _density     Object density
     * @param _friction    Object friction
     * @param _restitution The object restitution.
     *
     * @return The newly created Body.
     */
    public Body createStaticBody(GameEntity _entity, float _density, float _friction, float _restitution)
    {
        BodyDef bodyDef = createStaticBodyDef(_entity);

        _entity.bodyDef = bodyDef;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((_entity.frameWidth / 2) / Gfx._PPM),
                ((_entity.frameHeight / 2) / Gfx._PPM)
            );

        FixtureDef fixtureDef           = new FixtureDef();
        fixtureDef.shape                = shape;
        fixtureDef.density              = _density;
        fixtureDef.friction             = _friction;
        fixtureDef.restitution          = _restitution;
        fixtureDef.filter.maskBits      = _entity.collidesWith;
        fixtureDef.filter.categoryBits  = _entity.bodyCategory;

        Body body = app.worldModel.box2DWorld.createBody(bodyDef);
        body.setUserData(new BodyIdentity(_entity, _entity.gid, _entity.type));
        body.createFixture(fixtureDef);

        return body;
    }

    private BodyDef createBodyDef(BodyDef.BodyType bodyType, GameEntity _entity)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = true;

        bodyDef.position.set
            (
                (_entity.position.x + (_entity.frameWidth / 2)) / Gfx._PPM,
                (_entity.position.y + (_entity.frameHeight / 2)) / Gfx._PPM
            );

        _entity.bodyDef = bodyDef;

        return bodyDef;
    }

    private BodyDef createStaticBodyDef(GameEntity _entity)
    {
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.fixedRotation = true;

        bodyDef.position.set
            (
                (_entity.position.x + (_entity.frameWidth / 2)) / Gfx._PPM,
                (_entity.position.y + (_entity.frameHeight / 2)) / Gfx._PPM
            );

        return bodyDef;
    }

    private FixtureDef createFixtureDef(Shape shape, float density, float restitution)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.restitution = restitution;

        return fixtureDef;
    }
}
