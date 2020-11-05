package com.richikin.utilslib.core;

public interface ISettings
{
    void initialise();

    boolean isEnabled(final String preference);

    void enable(final String preference);

    void disable(final String preference);

    void resetToDefaults();

    com.badlogic.gdx.Preferences getPrefs();
}
