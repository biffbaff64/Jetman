package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.utilslib.graphics.LibGfx;

public class BodyBuilder
{
    public BodyBuilder()
    {
    }

    /**
     * Creates a Dynamic Box2D body which can be assigned to a GdxSprite.
     * <p>
     * Dynamic bodies are objects which move around and are affected by
     * forces and other dynamic, kinematic and static objects. Dynamic
     * bodies are suitable for any object which needs to move and be
     * affected by forces.
     *
     * @param _entity      The GdxSprite of this entity.
     * @param _density     Object density.
     * @param _friction    Object friction.
     * @param _restitution The object restitution.
     */
    public Body createDynamicCircle(GameEntity _entity, float _density, float _friction, float _restitution)
    {
        CircleShape shape = new CircleShape();
        shape.setRadius((_entity.frameWidth / 2) / LibGfx._PPM);

        BodyDef bodyDef = createBodyDef(BodyDef.BodyType.DynamicBody, _entity);
        FixtureDef fixtureDef = createFixtureDef(_entity, shape, _density, _friction, _restitution);

        return buildBody(_entity, bodyDef, fixtureDef);
    }

    /**
     * Creates a Dynamic Box2D body which can be assigned to a GdxSprite.
     * <p>
     * Dynamic bodies are objects which move around and are affected by
     * forces and other dynamic, kinematic and static objects. Dynamic
     * bodies are suitable for any object which needs to move and be
     * affected by forces.
     *
     * @param _entity      The GdxSprite of this entity.
     * @param _density     Object density.
     * @param _friction    Object friction.
     * @param _restitution The object restitution.
     */
    public Body createDynamicBox(GameEntity _entity, float _density, float _friction, float _restitution)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((_entity.frameWidth / 2) / LibGfx._PPM),
                ((_entity.frameHeight / 2) / LibGfx._PPM)
            );

        BodyDef bodyDef = createBodyDef(BodyDef.BodyType.DynamicBody, _entity);
        FixtureDef fixtureDef = createFixtureDef(_entity, shape, _density, _friction, _restitution);

        return buildBody(_entity, bodyDef, fixtureDef);
    }

    /**
     * Creates a Kinematic Box2D body which can be assigned to a GdxSprite.
     * <p>
     * Kinematic bodies are somewhat in between static and dynamic bodies.
     * Like static bodies, they do not react to forces, but like dynamic bodies,
     * they do have the ability to move. Kinematic bodies are great for things
     * where you, the programmer, want to be in full control of a body's motion,
     * such as a moving platform in a platform game.
     * It is possible to set the position on a kinematic body directly, but it's
     * usually better to set a velocity instead, and letting Box2D take care of
     * position updates.
     *
     * @param _entity      The GdxSprite of this entity
     * @param _density     Object density
     * @param _restitution The object restitution.
     */
    public Body createKinematicBody(GameEntity _entity, float _density, float _restitution)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((_entity.frameWidth / 2) / LibGfx._PPM),
                ((_entity.frameHeight / 2) / LibGfx._PPM)
            );

        BodyDef bodyDef = createBodyDef(BodyDef.BodyType.KinematicBody, _entity);
        FixtureDef fixtureDef = createFixtureDef(_entity, shape, _density, 0, _restitution);

        return buildBody(_entity, bodyDef, fixtureDef);
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite.
     * <p>
     * Static bodies are objects which do not move and are not affected by forces.
     * Dynamic bodies are affected by static bodies. Static bodies are perfect for
     * ground, walls, and any object which does not need to move. Static bodies
     * require less computing power.
     *
     * @param _entity The {@link GameEntity} to extract properties from.
     */
    public Body createStaticBody(GameEntity _entity)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((_entity.frameWidth / 2) / LibGfx._PPM),
                ((_entity.frameHeight / 2) / LibGfx._PPM)
            );

        BodyDef bodyDef = createBodyDef(BodyDef.BodyType.StaticBody, _entity);
        FixtureDef fixtureDef = createFixtureDef(_entity, shape, 1.0f, 1.0f, 0.15f);

        return buildBody(_entity, bodyDef, fixtureDef);
    }

    /**
     * Creates a Static Box2D body which can be assigned to a GdxSprite.
     * <p>
     * Static bodies are objects which do not move and are not affected by forces.
     * Dynamic bodies are affected by static bodies. Static bodies are perfect for
     * ground, walls, and any object which does not need to move. Static bodies
     * require less computing power.
     *
     * @param _entity      The {@link GameEntity} to extract properties from.
     * @param _density     Object density
     * @param _friction    Object friction
     * @param _restitution The object restitution.
     * @return The newly created Body.
     */
    public Body createStaticBody(GameEntity _entity, float _density, float _friction, float _restitution)
    {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox
            (
                ((_entity.frameWidth / 2) / LibGfx._PPM),
                ((_entity.frameHeight / 2) / LibGfx._PPM)
            );

        BodyDef bodyDef = createBodyDef(BodyDef.BodyType.StaticBody, _entity);
        FixtureDef fixtureDef = createFixtureDef(_entity, shape, _density, _friction, _restitution);

        return buildBody(_entity, bodyDef, fixtureDef);
    }

    private Body buildBody(GameEntity _entity, BodyDef _bodyDef, FixtureDef _fixtureDef)
    {
        Body body = App.worldModel.box2DWorld.createBody(_bodyDef);
        body.setUserData(new BodyIdentity(_entity, _entity.gid, _entity.type));
        body.createFixture(_fixtureDef);

        return body;
    }

    private BodyDef createBodyDef(BodyDef.BodyType bodyType, GameEntity _entity)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type          = bodyType;
        bodyDef.fixedRotation = true;

        bodyDef.position.set
            (
                (_entity.position.x + (_entity.frameWidth / 2)) / LibGfx._PPM,
                (_entity.position.y + (_entity.frameHeight / 2)) / LibGfx._PPM
            );

        return bodyDef;
    }

    private FixtureDef createFixtureDef(GameEntity _entity, Shape _shape, float _density, float _friction, float _restitution)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape               = _shape;
        fixtureDef.density             = _density;
        fixtureDef.friction            = _friction;
        fixtureDef.restitution         = _restitution;
        fixtureDef.filter.maskBits     = _entity.collidesWith;
        fixtureDef.filter.categoryBits = _entity.bodyCategory;

        return fixtureDef;
    }
}
