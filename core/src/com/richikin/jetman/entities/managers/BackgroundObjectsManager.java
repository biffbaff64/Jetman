package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.characters.TwinkleStar;
import com.richikin.jetman.entities.characters.Ufo;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class BackgroundObjectsManager
{
    private final App app;

    public BackgroundObjectsManager(App _app)
    {
        this.app = _app;
    }

    /**
     * Add a number of background UFOs
     *
     * @param numUfos The number of UFOs to add
     */
    public void addUFOs(int numUfos)
    {
        for (int i = 0; i < numUfos; i++)
        {
            SpriteDescriptor entityDescriptor = new SpriteDescriptor();
            entityDescriptor._ASSET      = GameAssets._BACKGROUND_UFO_ASSET;
            entityDescriptor._FRAMES     = GameAssets._BACKGROUND_UFO_FRAMES;
            entityDescriptor._PLAYMODE   = Animation.PlayMode.LOOP;
            entityDescriptor._POSITION.x = 0;
            entityDescriptor._POSITION.y = 0;
            entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_BACKGROUND_UFO);
            entityDescriptor._INDEX      = app.entityData.entityMap.size;

            Ufo ufo = new Ufo(app);
            ufo.initialise(entityDescriptor);

            app.entityData.addEntity(ufo);
        }
    }

    private final int[][] twinklestarPositions =
        {
            {  82, 554},    // 0
            { 196, 354},    // 1
            {1440, 484},    // 2
            { 528, 401},    // 3
            {1756, 562},    // 4
            { 792, 401},    // 5
            { 924, 484},    // 6
            { 988, 562},    // 7
            {1069, 420},    // 8
            {1124, 476},    // 9
            {1434, 669},    // 10
            { 205, 611},    // 11
            { 280, 488},    // 12
            { 342, 856},    // 13
            { 661, 495},    // 14
            {1695, 632},    // 15
            { 967, 792},    // 16
        };

    public void addTwinkleStars()
    {
        for (int[] position : twinklestarPositions)
        {
            String asset = (MathUtils.random(100) < 50) ? GameAssets._TWINKLE_STAR1_ASSET : GameAssets._TWINKLE_STAR2_ASSET;

            SpriteDescriptor entityDescriptor = new SpriteDescriptor();
            entityDescriptor._ASSET      = asset;
            entityDescriptor._FRAMES     = GameAssets._TWINKLE_STAR_FRAMES;
            entityDescriptor._PLAYMODE   = Animation.PlayMode.LOOP;
            entityDescriptor._POSITION.x = position[0] / Gfx.getTileWidth();
            entityDescriptor._POSITION.y = position[1] / Gfx.getTileHeight();
            entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_TWINKLE_STAR);
            entityDescriptor._INDEX      = app.entityData.entityMap.size;

            TwinkleStar twinkleStar = new TwinkleStar(app);
            twinkleStar.initialise(entityDescriptor);

            app.entityData.addEntity(twinkleStar);
        }
    }
}
