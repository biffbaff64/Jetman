
package com.richikin.jetman.graphics.effects;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;

import java.util.ArrayList;

/**
 * 3D Starfield Manager.
 *
 * @author Richard Ikin. This code is adapted from similar
 *         code that I have seen in the past. I cannot
 *         remember who the author of that code was and,
 *         when his/her identity is established, this
 *         javaDoc will be updated to add credits.
 */
@SuppressWarnings({"WeakerAccess"})
public class StarField implements Disposable
{
    public final float speed    = 90f;
    public final int   numStars = 5000;

    private ArrayList<StarObject> stars;
    private App app;

    public StarField(App _app)
    {
        this.app    = _app;
        this.stars  = new ArrayList<>();

        for (int i=0; i<numStars; i++)
        {
            stars.add(new StarObject(app));
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
        stars.clear();
        stars = null;
        app = null;
    }
}
