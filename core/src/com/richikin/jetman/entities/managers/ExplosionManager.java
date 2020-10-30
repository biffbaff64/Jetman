package com.richikin.jetman.entities.managers;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Explosion;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;

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
        for (ExplosionInfo explosionType : explosionTypes)
        {
            if (explosionType.graphicID == _gid)
            {
                SpriteDescriptor descriptor;
                descriptor             = Entities.getDescriptor(_gid);
                descriptor._PARENT     = _parent;
                descriptor._SIZE       = GameAssets.getAssetSize(_gid);
                descriptor._POSITION.x = (int) _parent.sprite.getX() / Gfx.getTileWidth();
                descriptor._POSITION.y = (int) _parent.sprite.getY() / Gfx.getTileHeight();
                descriptor._POSITION.z = _parent.zPosition;
                descriptor._INDEX      = _app.entityData.entityMap.size;

                Explosion explosion = new Explosion(_gid, _app);
                explosion.initialise(descriptor);
                explosion.sprite.setScale(explosionType.scale);
                _app.entityData.addEntity(explosion);
            }
        }
    }
}
