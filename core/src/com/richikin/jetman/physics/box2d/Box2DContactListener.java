package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.physics.box2d.*;
import com.richikin.jetman.core.App;
import net.dermetfan.gdx.Multiplexer;

public class Box2DContactListener extends Multiplexer<ContactListener> implements ContactListener
{
    private final App app;

    public Box2DContactListener(App _app)
    {
        super();

        this.app = _app;
    }

    /**
     * Called when two fixtures begin to touch.
     */
    @Override
    public void beginContact(Contact _contact)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.beginContact(_contact);
            }
        }
    }

    /**
     * Called when two fixtures cease to touch.
     */
    @Override
    public void endContact(Contact _contact)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.endContact(_contact);
            }
        }
    }

    /*
     * This is called after a contact is updated. This allows you to inspect
     * a contact before it goes to the solver. If you are careful, you can modify
     * the contact manifold (e.g. disable contact). A copy of the old manifold is
     * provided so that you can detect changes.
     *
     * Note: this is called only for awake bodies.
     * Note: this is called even when the number of contact points is zero.
     * Note: this is not called for sensors.
     * Note: if you set the number of contact points to zero, you will not get an
     * EndContact callback. However, you may get a BeginContact callback the next step.
     */
    @Override
    public void preSolve(Contact _contact, Manifold _oldManifold)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.preSolve(_contact, _oldManifold);
            }
        }
    }

    /*
     * This lets you inspect a contact after the solver is finished. This
     * is useful for inspecting impulses.
     *
     * Note: the contact manifold does not include time of impact impulses,
     * which can be arbitrarily large if the sub-step is small. Hence the
     * impulse is provided explicitly in a separate data structure.
     * Note: this is only called for contacts that are touching, solid, and awake.
     */
    @Override
    public void postSolve(Contact _contact, ContactImpulse _impulse)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.postSolve(_contact, _impulse);
            }
        }
    }

    public void addListener(ContactListener _listener)
    {
        receivers.add(_listener);
    }
}
