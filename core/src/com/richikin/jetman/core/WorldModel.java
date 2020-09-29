package com.richikin.jetman.core;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.physics.box2d.BodyBuilder;
import com.richikin.jetman.physics.box2d.Box2DContactListener;
import com.richikin.jetman.utils.Developer;

public class WorldModel
{
    public World                box2DWorld;
    public Box2DDebugRenderer   b2dr;
    public Box2DContactListener box2DContactListener;
    public BodyBuilder          bodyBuilder;

    private final App app;

    public WorldModel(App _app)
    {
        this.app = _app;

        box2DWorld = new World
            (
                new Vector2
                    (
                        (Gfx._WORLD_GRAVITY.x * Gfx._PPM),
                        (Gfx._WORLD_GRAVITY.y * Gfx._PPM)
                    ),
                false
            );

        if (Developer.isDevMode())
        {
            b2dr = new Box2DDebugRenderer
                (
                    true,
                    true,
                    true,
                    true,
                    false,
                    true
                );
        }

        bodyBuilder          = new BodyBuilder(_app);
        box2DContactListener = new Box2DContactListener(_app);

        box2DWorld.setContactListener(box2DContactListener);
    }

    public void drawDebugMatrix()
    {
        if (b2dr != null)
        {
            //
            // Care needed here if the viewport sizes for SpriteGameCamera
            // and TiledGameCamera are different.
            b2dr.render
                (
                    box2DWorld,
                    app.baseRenderer.spriteGameCamera.camera.combined.cpy().scale(Gfx._PPM, Gfx._PPM, 0)
                );
        }
    }

    public void worldStep()
    {
        if (box2DWorld != null)
        {
            box2DWorld.step(Gfx._STEP_TIME, Gfx._VELOCITY_ITERATIONS, Gfx._POSITION_ITERATIONS);
        }
    }
}

