package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;

public class ExplosionManager
{
    static class ExplosionInfo
    {
        GraphicID graphicID;
        float     scale;

        ExplosionInfo(GraphicID _gid, float _scale)
        {
            this.graphicID = _gid;
            this.scale = _scale;
        };
    }

    private final ExplosionInfo[] explosionTypes =
        {
            new ExplosionInfo(GraphicID.G_EXPLOSION12, 0.18f),
            new ExplosionInfo(GraphicID.G_EXPLOSION64, 1.00f),
            new ExplosionInfo(GraphicID.G_EXPLOSION128, 2.00f),
            new ExplosionInfo(GraphicID.G_EXPLOSION256, 4.00f),
        };

    public void createExplosion(GraphicID _gid, GdxSprite _parent, App _app)
    {
        int index = 0;

        for (int i=0; i<explosionTypes.length; i++)
        {
            if (explosionTypes[i].graphicID == _gid)
            {
                index = i;
            }
        }

        SpriteDescriptor entityDescriptor = new SpriteDescriptor
            (
                "Explosion",
                explosionTypes[index].graphicID,
                GraphicID._DECORATION,
                GameAssets._EXPLOSION64_ASSET,
                GameAssets._EXPLOSION64_FRAMES,
                Animation.PlayMode.NORMAL,
                TileID._EXPLOSION_TILE

//                (int) _parent.sprite.getX() / Gfx.getTileWidth(),
//                (int) _parent.sprite.getY() / Gfx.getTileHeight(),
//                _app.entityUtils.getInitialZPosition(_gid),
//                _app.assets.getAnimationRegion(GameAssets._EXPLOSION64_ASSET),
//                GameAssets._EXPLOSION64_FRAMES,
//                Animation.PlayMode.LOOP
            );

        entityDescriptor._INDEX         = _app.entityData.entityMap.size;
        entityDescriptor._PARENT        = _parent;
        entityDescriptor._SIZE          = GameAssets.getAssetSize(_gid);

        Explosion explosion = new Explosion(_gid, _app);
        explosion.initialise(entityDescriptor);
        explosion.sprite.setScale(explosionTypes[index].scale);

        _app.entityData.addEntity(explosion);
    }
}
