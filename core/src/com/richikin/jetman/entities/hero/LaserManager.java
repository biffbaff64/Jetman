package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Laser;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.pooling.ObjectPool;

public class LaserManager implements Disposable
{
    private ObjectPool<Laser> laserPool;

    public LaserManager()
    {
        ObjectPool.ObjectPoolFactory<Laser> laserFactory = new ObjectPool.ObjectPoolFactory<Laser>()
        {
            @Override
            public Laser createObject()
            {
                return new Laser();
            }

            @Override
            public Laser createObject(Rectangle rectangle)
            {
                return new Laser();
            }

            @Override
            public Laser createObject(int x, int y, int width, int height, GraphicID type)
            {
                return new Laser();
            }
        };

        laserPool = new ObjectPool<>(laserFactory, 50);
    }

    public void createLaser(GdxSprite parent)
    {
        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_LASER);
        descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_LASER);
        descriptor._ANIM_RATE = 5f / 6f;
        descriptor._POSITION.x = 0;
        descriptor._POSITION.y = 0;
        descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_LASER);
        descriptor._INDEX = App.entityData.entityMap.size;
        descriptor._PARENT = parent;

        Laser laser = laserPool.newObject();
        laser.initialise(descriptor);

        App.entityData.addEntity(laser);
    }

    @Override
    public void dispose()
    {
        laserPool = null;
    }
}
