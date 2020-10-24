package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class Teleporter extends GdxSprite
{
    public int          teleporterNumber;
    public boolean      isCollected;
    public GdxSprite    collector;

    private final App app;

    public Teleporter(App _app)
    {
        super(GraphicID.G_TRANSPORTER, _app);

        this.app = _app;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        create(descriptor);

        setAction(Actions._STANDING);

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        isAnimating = true;
        isCollected = false;
        isDrawable  = true;

        bodyCategory = Gfx.CAT_TELEPORTER;
        collidesWith = Gfx.CAT_PLAYER
            | Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_GROUND;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            case _HELD:
            {
            }
            break;

            case _HURT:
            {
                setAction(Actions._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void tidy(int _index)
    {
        app.entityManager._teleportIndex[teleporterNumber] = 0;
        app.entityData.removeEntity(_index);
    }
}
