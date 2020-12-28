package com.richikin.jetman.physics.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import net.dermetfan.gdx.Multiplexer;

public class Box2DContactListener extends Multiplexer<ContactListener> implements ContactListener
{
    public Box2DContactListener()
    {
        //
        // Initialises the Multiplexer#receivers array
        super();
    }

    /**
     * Called when two fixtures begin to touch.
     */
    @Override
    public void beginContact(Contact contact)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.beginContact(contact);
            }
        }
    }

    /**
     * Called when two fixtures cease to touch.
     */
    @Override
    public void endContact(Contact contact)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.endContact(contact);
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
    public void preSolve(Contact contact, Manifold oldManifold)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.preSolve(contact, oldManifold);
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
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
        if (receivers.notEmpty())
        {
            for (ContactListener listener : receivers)
            {
                listener.postSolve(contact, impulse);
            }
        }
    }

    public void addListener(ContactListener listener)
    {
        receivers.add(listener);
    }
}
