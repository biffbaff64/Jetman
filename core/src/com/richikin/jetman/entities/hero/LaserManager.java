package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Laser;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.pooling.ObjectPool;

public class LaserManager implements Disposable
{
    private ObjectPool<Laser> laserPool;

    public LaserManager(App app)
    {
        ObjectPool.ObjectPoolFactory<Laser> laserFactory = new ObjectPool.ObjectPoolFactory<Laser>()
        {
            @Override
            public Laser createObject()
            {
                return new Laser(app);
            }

            @Override
            public Laser createObject(Rectangle rectangle)
            {
                return new Laser(app);
            }

            @Override
            public Laser createObject(int x, int y, int width, int height, GraphicID type)
            {
                return new Laser(app);
            }
        };

        laserPool = new ObjectPool<>(laserFactory, 50);
    }

    public void createLaser(GdxSprite parent, App app)
    {
        SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_LASER);
        entityDescriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_LASER);
        entityDescriptor._ANIM_RATE = 5f / 6f;
        entityDescriptor._POSITION.x = 0;
        entityDescriptor._POSITION.y = 0;
        entityDescriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_LASER);
        entityDescriptor._INDEX = app.entityData.entityMap.size;
        entityDescriptor._PARENT = parent;

        Laser laser = laserPool.newObject();
        laser.initialise(entityDescriptor);

        app.entityData.addEntity(laser);
    }

    @Override
    public void dispose()
    {
        laserPool = null;
    }
}
