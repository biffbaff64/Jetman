package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.Trace;

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
    public void initialise(EntityDescriptor entityDescriptor)
    {
        Trace.__FILE_FUNC();

        create(entityDescriptor);

        setAction(Actions._STANDING);

        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        isAnimating = true;
        isCollected = false;
        isDrawable  = true;

        setCollisionListener();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getSpriteAction())
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
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
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

    private void setCollisionListener()
    {
//        addCollisionListener(new CollisionListener()
//        {
//            @Override
//            public void onPositiveCollision(GraphicID graphicID)
//            {
//                if (spriteAction == Actions._FALLING)
//                {
//                    GraphicID contactID = app.collisionUtils.getBoxHittingBottom(app.getBomb()).gid;
//
//                    if (contactID == GraphicID._GROUND)
//                    {
//                        direction.setY(Movement._DIRECTION_STILL);
//                        speed.setY(0);
//                        setAction(Actions._STANDING);
//                    }
//                }
//            }
//
//            @Override
//            public void onNegativeCollision()
//            {
//            }
//
//            @Override
//            public void dispose()
//            {
//            }
//        });
    }
}
