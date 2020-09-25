package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.characters.TwinkleStar;
import com.richikin.jetman.entities.characters.Ufo;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;

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
            SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_BACKGROUND_UFO);
            entityDescriptor._PLAYMODE   = Animation.PlayMode.LOOP;
            entityDescriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_BACKGROUND_UFO);
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
            {  96, 544},    // 0
//            { 192, 352},    // 1
//            {1440, 480},    // 2
//            { 544, 416},    // 3
//            {1760, 576},    // 4
//            { 800, 416},    // 5
//            { 928, 480},    // 6
//            { 992, 576},    // 7
//            {1056, 416},    // 8
//            {1120, 480},    // 9
//            {1440, 672},    // 10
//            { 192, 608},    // 11
//            { 288, 480},    // 12
//            { 352, 864},    // 13
//            { 672, 480},    // 14
//            {1696, 640},    // 15
//            { 960, 800},    // 16
        };

    public void addTwinkleStars()
    {
        for (int[] position : twinklestarPositions)
        {
            String asset = (MathUtils.random(100) < 50) ? GameAssets._TWINKLE_STAR1_ASSET : GameAssets._TWINKLE_STAR2_ASSET;

            SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_TWINKLE_STAR);
            entityDescriptor._ASSET      = asset;
            entityDescriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_TWINKLE_STAR);
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
