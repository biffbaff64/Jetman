
package com.richikin.jetman.graphics.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.maths.SimpleVec3F;

/**
 * A 3D Star object
 *
 * @author Richard Ikin.
 * @author TBC.
 *
 * This code is adapted from similar
 * code that I have seen in the past. I cannot
 * remember who the author of that code was and,
 * when his/her identity is established, this
 * javaDoc will be updated to add credits.
 */
@SuppressWarnings("FieldCanBeLocal")
public class StarObject implements Disposable
{
    private final float _INITIAL_DEPTH          = 100.0f;
    private final float _FINAL_DEPTH            = 1000.0f;
    private final float _MINIMUM_VELOCITY       = 0.5f;
    private final float _MAXIMUM_VELOCITY       = 5.0f;
    private final float _MAXIMUM_STAR_RADIUS    = 16.0f;

    //
    // Note: SimpleVec3F is a simplified Vector3.
    private SimpleVec3F     position;
    private SimpleVec3F     velocity;
    private TextureRegion   region;
    private App             app;

    /**
     * COnstructor
     *
     * @param _app App - A reference to the App global.
     */
    StarObject(App _app)
    {
        app         = _app;
        position    = new SimpleVec3F();
        velocity    = new SimpleVec3F();
        region      = app.assets.getObjectRegion("solid_white32x32");

        resetPosition();
    }

    /**
     * Update and draw this star.
     *
     * @param speed Float - Speed of movement
     */
    public void render(float speed)
    {
        update(speed);

        float x = ((position.x / position.z)/* * 100*/) * (Gfx._VIEW_WIDTH * 0.5f);
        float y = ((position.y / position.z)/* * 100*/) * (Gfx._VIEW_HEIGHT * 0.5f);

        float radius = ((_MAXIMUM_STAR_RADIUS - ((position.z * _MAXIMUM_STAR_RADIUS) * 0.001f)) * velocity.z) * 0.2f;

        app.spriteBatch.draw(region, x, y, radius, radius);
    }

    /**
     * Update the star.
     *
     * @param speed floast holding speed of movement
     */
    private void update(float speed)
    {
        if ((position.z < 0) || (position.z > _FINAL_DEPTH)
            || (position.y < -Gfx._VIEW_HEIGHT) || (position.y > Gfx._VIEW_HEIGHT)
            || (position.x < -Gfx._VIEW_WIDTH) || (position.x > Gfx._VIEW_WIDTH))
        {
            resetPosition();
        }

        float deltaTime = Gdx.graphics.getDeltaTime();

        moveStar((velocity.x * speed) * deltaTime, (velocity.y * speed) * deltaTime, (velocity.z * speed) * deltaTime);
    }

    /**
     * Set a new position, and velocity, for this star.
     */
    private void resetPosition()
    {
        position.x = MathUtils.random(-Gfx._VIEW_WIDTH, Gfx._VIEW_WIDTH);
        position.y = MathUtils.random(-Gfx._VIEW_HEIGHT,Gfx._VIEW_HEIGHT);
        position.z = MathUtils.random(_INITIAL_DEPTH, _FINAL_DEPTH);
        velocity.z = MathUtils.random(_MINIMUM_VELOCITY, _MAXIMUM_VELOCITY);
    }

    /**
     * Update this stars position by the
     * supplied x, y & z values.
     *
     * @param x - Float - The X modifier.
     * @param y - Float - The Y modifier.
     * @param z - Float - The Z modifier.
     */
    private void moveStar(float x, float y, float z)
    {
        position.sub(x, y, z);
    }

    /**
     * Free up resources used.
     */
    @Override
    public void dispose()
    {
        position = null;
        velocity = null;
        region = null;
        app = null;
    }
}
