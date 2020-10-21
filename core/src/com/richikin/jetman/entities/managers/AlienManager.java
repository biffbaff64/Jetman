package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.Asteroid;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2;

public class AlienManager extends GenericEntityManager
{
    private final GraphicID[] aliens =
        {
//            GraphicID.G_3BALLS,
//            GraphicID.G_3BALLS_UFO,
//            GraphicID.G_3LEGS_ALIEN,
            GraphicID.G_ASTEROID,
//            GraphicID.G_ALIEN_WHEEL,
//            GraphicID.G_BLOB,
//            GraphicID.G_DOG,
//            GraphicID.G_GREEN_BLOCK,
//            GraphicID.G_SPINNING_BALL,
//            GraphicID.G_STAR_SPINNER,
//            GraphicID.G_TOPSPIN,
//            GraphicID.G_TWINKLES,
        };

    public AlienManager(final App _app)
    {
        super(_app);
    }

    @Override
    public void init()
    {
    }

    @Override
    public void update()
    {
    }

    @Override
    public void free()
    {
    }

    @Override
    public void create()
    {
        for (GraphicID graphicID : aliens)
        {
            if (app.entityUtils.canUpdate(graphicID))
            {
                SimpleVec2 markerPos = setInitialPosition(graphicID);

                descriptor = Entities.getDescriptor(graphicID);
                descriptor._SIZE = GameAssets.getAssetSize(graphicID);
                descriptor._POSITION.x = markerPos.getX();
                descriptor._POSITION.y = markerPos.getY();
                descriptor._POSITION.z = app.entityUtils.getInitialZPosition(graphicID);
                descriptor._INDEX = app.entityData.entityMap.size;

                switch (graphicID)
                {
                    case G_3BALLS:
                    {
                    }
                    break;

                    case G_3BALLS_UFO:
                    {
                    }
                    break;

                    case G_3LEGS_ALIEN:
                    {
                    }
                    break;

                    case G_ASTEROID:
                    {
                        Asteroid asteroid = new Asteroid(app);
                        asteroid.initialise(descriptor);
                        app.entityData.addEntity(asteroid);
                    }
                    break;

                    case G_ALIEN_WHEEL:
                    {
                    }
                    break;

                    case G_BLOB:
                    {
                    }
                    break;

                    case G_DOG:
                    {
                    }
                    break;

                    case G_GREEN_BLOCK:
                    {
                    }
                    break;

                    case G_SPINNING_BALL:
                    {
                    }
                    break;

                    case G_STAR_SPINNER:
                    {
                    }
                    break;

                    case G_TOPSPIN:
                    {
                    }
                    break;

                    case G_TWINKLES:
                    {
                    }
                    break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Set up the initial position for the alien specified
     * by graphicID.
     * Some aliens have limited areas of the map height they can
     * exist in, or must start at specified heights etc.
     *
     * @param graphicID The GraphicID of the alien.
     *
     * @return  SimpleVec2 holding X and Y.
     */
    private SimpleVec2 setInitialPosition(GraphicID graphicID)
    {
        SimpleVec2 initPos = new SimpleVec2();

        switch (graphicID)
        {
            case G_ASTEROID:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = ((Gfx._VIEW_HEIGHT / Gfx.getTileHeight()) / 2) + MathUtils.random(4);

                initPos.x += ((MathUtils.random(100) < 50) ?
                    (Gfx._VIEW_WIDTH / Gfx.getTileWidth()) * 2:
                    -(Gfx._VIEW_WIDTH / Gfx.getTileWidth()) * 2);
                initPos.y += (Gfx._VIEW_HEIGHT / Gfx.getTileHeight());
            }
            break;

            default:
            break;
        }

        return initPos;
    }
}
