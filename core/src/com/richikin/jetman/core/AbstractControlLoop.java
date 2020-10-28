package com.richikin.jetman.core;

import com.richikin.utilslib.states.StateManager;

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
