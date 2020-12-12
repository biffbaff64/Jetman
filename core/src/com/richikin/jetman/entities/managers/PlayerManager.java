package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;

public class PlayerManager
{
    public int playerTileX;
    public int playerTileY;

    private SpriteDescriptor descriptor;

    public PlayerManager()
    {
    }

    public void createPlayer()
    {
        App.entityManager._playerIndex = 0;
        App.entityManager._playerReady = false;

        App.entities.mainPlayer = new MainPlayer();
        App.entities.mainPlayer.initialise(descriptor);

        App.entityData.addEntity(App.entities.mainPlayer);
        App.entityManager.updateIndexes();
        App.entityManager._playerReady = true;
        App.entityManager._playerIndex = descriptor._INDEX;

        App.entities.mainPlayer.addCollisionListener(App.getPlayer().collision);
    }

    public void setSpawnPoint()
    {
        if ((App.mapData.placementTiles == null)
            || (App.roomManager.getStartPosition().isEmpty()))
        {
            playerTileX = (((Gfx.getMapWidth() / Gfx.getTileWidth()) / 2) - 10) + MathUtils.random(20);
            playerTileY = 1;
        }
        else
        {
            playerTileX = App.roomManager.getStartPosition().x;
            playerTileY = App.roomManager.getStartPosition().y;
        }

        descriptor             = App.entities.getDescriptor(GraphicID.G_PLAYER);
        descriptor._PLAYMODE   = Animation.PlayMode.LOOP;
        descriptor._POSITION.x = playerTileX;
        descriptor._POSITION.y = playerTileY;
        descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_PLAYER);
        descriptor._INDEX      = App.entityData.entityMap.size;
        descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_PLAYER);
    }
}
