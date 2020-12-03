package com.richikin.jetman.core;

public abstract class AbstractControlLoop implements IControlLoop
{
    public AbstractControlLoop()
    {
    }

    @Override
    public abstract void initialise();

    @Override
    public abstract void update();
}
