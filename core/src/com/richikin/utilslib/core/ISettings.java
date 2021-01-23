package com.richikin.utilslib.core;

public interface ISettings
{
    void createPreferencesObject();

    boolean isEnabled(final String preference);

    boolean isDisabled(final String preference);

    void enable(final String preference);

    void disable(final String preference);

    void resetToDefaults();

    void freshInstallCheck();

    com.badlogic.gdx.Preferences getPrefs();

    void debugReport();
}
