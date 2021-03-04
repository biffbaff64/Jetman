
package com.richikin.jetman.graphics.effects;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.logging.Trace;

import java.util.ArrayList;

public class StarField implements Disposable
{
    public final float speed    = 40f;
    public final int   numStars = 5000;

    private ArrayList<StarObject> stars;

    public StarField()
    {
        this.stars = new ArrayList<>();

        for (int i = 0; i < numStars; i++)
        {
            stars.add(new StarObject());
        }
    }

    public void render()
    {
        for (StarObject star : stars)
        {
            star.render(speed);
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        for (StarObject starObject : stars)
        {
            starObject.dispose();
        }

        stars.clear();
        stars = null;
    }
}
