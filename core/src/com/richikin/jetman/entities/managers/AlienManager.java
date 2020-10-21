package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.Asteroid;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.graphics.GraphicIndex;
import com.richikin.jetman.maths.SimpleVec2;

public class AlienManager extends GenericEntityManager
{
    private final GraphicIndex[] aliens =
        {
//            new GraphicIndex(GraphicID.G_3BALLS, 0),
//            new GraphicIndex(GraphicID.G_3BALLS_UFO, 0),
//            new GraphicIndex(GraphicID.G_3LEGS_ALIEN, 0),
            new GraphicIndex(GraphicID.G_ASTEROID, 0),
//            new GraphicIndex(GraphicID.G_ALIEN_WHEEL, 0),
//            new GraphicIndex(GraphicID.G_BLOB, 0),
//            new GraphicIndex(GraphicID.G_DOG, 0),
//            new GraphicIndex(GraphicID.G_GREEN_BLOCK, 0),
//            new GraphicIndex(GraphicID.G_SPINNING_BALL, 0),
//            new GraphicIndex(GraphicID.G_STAR_SPINNER, 0),
//            new GraphicIndex(GraphicID.G_TOPSPIN, 0),
//            new GraphicIndex(GraphicID.G_TWINKLES, 0),
        };

    public AlienManager(final App _app)
    {
        super(_app);
    }

    @Override
    public void init()
    {
        for (GraphicIndex item : aliens)
        {
            item.value = 0;
        }
    }

    @Override
    public void update()
    {
        for (GraphicIndex alien : aliens)
        {
            if (alien.value < app.roomManager.getMaxAllowed(alien.graphicID))
            {
                create(alien.graphicID);
                alien.value++;
            }
        }
    }

    public void create(GraphicID graphicID)
    {
        if (app.entityUtils.canUpdate(graphicID))
        {
            SimpleVec2 markerPos = setInitialPosition(graphicID);

            descriptor             = Entities.getDescriptor(graphicID);
            descriptor._SIZE       = GameAssets.getAssetSize(graphicID);
            descriptor._POSITION.x = markerPos.getX();
            descriptor._POSITION.y = markerPos.getY();
            descriptor._POSITION.z = app.entityUtils.getInitialZPosition(graphicID);
            descriptor._INDEX      = app.entityData.entityMap.size;

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

    /**
     * Set up the initial position for the alien specified
     * by graphicID.
     * Some aliens have limited areas of the map height they can
     * exist in, or must start at specified heights etc.
     *
     * @param graphicID The GraphicID of the alien.
     * @return SimpleVec2 holding X and Y.
     */
    private SimpleVec2 setInitialPosition(GraphicID graphicID)
    {
        SimpleVec2 initPos = new SimpleVec2();

        switch (graphicID)
        {
            case G_3BALLS:
            case G_3BALLS_UFO:
            case G_3LEGS_ALIEN:
            {
            }
            break;

            case G_ASTEROID:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = ((Gfx._VIEW_HEIGHT / Gfx.getTileHeight()) / 2) + MathUtils.random(4);

                initPos.x += ((MathUtils.random(100) < 50) ?
                    (Gfx._VIEW_WIDTH / Gfx.getTileWidth()) * 2 :
                    -(Gfx._VIEW_WIDTH / Gfx.getTileWidth()) * 2);
                initPos.y += (Gfx._VIEW_HEIGHT / Gfx.getTileHeight());
            }
            break;

            default:
                break;
        }

        return initPos;
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._ALIEN_MANAGER;
    }
}
