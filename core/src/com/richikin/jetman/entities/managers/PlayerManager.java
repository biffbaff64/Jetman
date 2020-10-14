package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class PlayerManager
{
    public int playerTileX;
    public int playerTileY;

    private       SpriteDescriptor entityDescriptor;
    private final App              app;

    public PlayerManager(App _app)
    {
        this.app = _app;
    }

    public void setSpawnPoint()
    {
        if ((app.mapData.placementTiles == null)
            || (app.roomManager.getStartPosition().isEmpty()))
        {
            playerTileX = (((Gfx.getMapWidth() / Gfx.getTileWidth()) / 2) - 10) + MathUtils.random(20);
            playerTileY = 1;
        }
        else
        {
            playerTileX = app.roomManager.getStartPosition().x;
            playerTileY = app.roomManager.getStartPosition().y;
        }

        entityDescriptor             = Entities.getDescriptor(GraphicID.G_PLAYER);
        entityDescriptor._PLAYMODE   = Animation.PlayMode.LOOP;
        entityDescriptor._POSITION.x = playerTileX;
        entityDescriptor._POSITION.y = playerTileY;
        entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_PLAYER);
        entityDescriptor._INDEX      = app.entityData.entityMap.size;
        entityDescriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_PLAYER);
    }

    public void createPlayer()
    {
        app.entityManager._playerIndex = 0;
        app.entityManager._playerReady = false;

        MainPlayer mainPlayer = new MainPlayer(app);
        mainPlayer.initialise(entityDescriptor);

        app.entityData.addEntity(mainPlayer);
        app.entityManager.updateIndexes();
        app.entityManager._playerReady = true;
        app.entityManager._playerIndex = entityDescriptor._INDEX;

        mainPlayer.addCollisionListener(app.getPlayer().collision);
    }
}
