package com.richikin.jetman.core;

public abstract class AbstractControlLoop implements ControlLoop
{
    final App app;

    AbstractControlLoop(App _app)
    {
        this.app = _app;
    }

    @Override
    public abstract void initialise();

    @Override
    public abstract void update(StateManager gameState);
}
