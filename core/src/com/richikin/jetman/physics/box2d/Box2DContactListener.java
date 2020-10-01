package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.Trace;
import net.dermetfan.gdx.Multiplexer;

public class Box2DContactListener implements ContactListener
{
    private final App app;

    public Box2DContactListener(App _app)
    {
        this.app = _app;
    }

    /**
     * Called when two fixtures begin to touch.
     *
     * @param _contact
     */
    @Override
    public void beginContact(Contact _contact)
    {
        Fixture fixtureA = _contact.getFixtureA();
        Fixture fixtureB = _contact.getFixtureB();

        if (fixtureB.getBody().getType() != BodyDef.BodyType.StaticBody)
        {
            Trace.__FILE_FUNC
                (
                    ((BodyIdentity) fixtureA.getBody().getUserData()).gid
                        + " is touching "
                        + ((BodyIdentity) fixtureB.getBody().getUserData()).gid
                );
        }
    }

    /**
     * Called when two fixtures cease to touch.
     *
     * @param _contact
     */
    @Override
    public void endContact(Contact _contact)
    {
    }

    @Override
    public void preSolve(Contact _contact, Manifold _oldManifold)
    {
    }

    @Override
    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
    }
}
