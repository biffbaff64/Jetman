
package com.richikin.utilslib.logging;

import com.badlogic.gdx.Gdx;

public class Stats
{
    private static com.badlogic.gdx.Preferences prefs;

    public static void setup()
    {
        Trace.__FILE_FUNC();

        prefs = Gdx.app.getPreferences("com.richikin.gdx7lib.preferences");
    }

    public static void setMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger(Meters.fromValue(meter).name(), amount);
            prefs.flush();
        }
    }

    public static void addToMeter(int meter, int amount)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) + amount)
                    );

            prefs.flush();
        }
    }

    public static void decMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) - 1)
                    );

            prefs.flush();
        }
    }

    public static void incMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger
                    (
                            Meters.fromValue(meter).name(),
                            (prefs.getInteger(Meters.fromValue(meter).name(), 0) + 1)
                    );

            prefs.flush();
        }
    }

    public static void clearMeter(int meter)
    {
        if (prefs != null)
        {
            prefs.putInteger(Meters.fromValue(meter).name(), 0);
            prefs.flush();
        }
    }

    public static int getMeter(int meter)
    {
        return (prefs == null) ? 0 : prefs.getInteger(Meters.fromValue(meter).name(), 0);
    }

    public static void resetAllMeters()
    {
        if (prefs != null)
        {
            int end = Meters._MAX_STATS_METERS.get();

            for (int i = 0; i < end; i++)
            {
                prefs.putInteger(Meters.fromValue(i).name(), 0);
            }

            prefs.flush();
        }
    }
}
