package com.richikin.jetman.entities.managers;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.characters.Missile;
import com.richikin.jetman.entities.characters.MissileBase;
import com.richikin.jetman.entities.characters.DefenderBullet;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;

public class MissileBaseManager extends GenericEntityManager
{
    private static final int _MAX_SPARKLERS = 30;

    public int      activeMissiles;
    public int      activeSparklers;
    public int      baseTileX;
    public int      baseTileY;
    public boolean  baseLocatedLeft;
    public boolean  isMissileActive;

    private int activeBases;

    public MissileBaseManager(App _app)
    {
        super( GraphicID.G_MISSILE_BASE, _app);
    }

    @Override
    public void init()
    {
        Trace.__FILE_FUNC();

        activeBases         = 0;
        activeMissiles      = 0;
        activeSparklers     = 0;
        isMissileActive     = false;

        createBase();
    }

    @Override
    public void update()
    {
        if (activeBases == 0)
        {
            createBase();
        }
    }

    @Override
    public int getActiveCount()
    {
        return activeBases;
    }

    @Override
    public void free()
    {
        activeBases = Math.max(0, activeBases - 1);
    }

    public void releaseSparkler()
    {
        activeSparklers = Math.max(0, activeSparklers - 1);
    }

    @Override
    public void reset()
    {
    }

    private void createBase()
    {
        Trace.__FILE_FUNC();

        activeBases = 0;

        SpriteDescriptor descriptor = createMissileBaseMarker();

        MissileBase missileBase = new MissileBase(app);
        missileBase.initialise(descriptor);
        app.entityData.addEntity(missileBase);

        app.entityManager._missileBaseIndex = missileBase.spriteNumber;

        activeBases++;
    }

    private SpriteDescriptor createMissileBaseMarker()
    {
        Trace.__FILE_FUNC();

        SimpleVec2 vec2 = findCoordinates(GraphicID.G_MISSILE_BASE);

        baseLocatedLeft = (vec2.x < (Gfx.getMapWidth() / 2));

        baseTileX = vec2.x;
        baseTileY = vec2.y;

        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_MISSILE_BASE);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_MISSILE_BASE);
        descriptor._POSITION.x    = baseTileX;
        descriptor._POSITION.y    = baseTileY;
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_MISSILE_BASE);
        descriptor._INDEX         = app.entityData.entityMap.size;

        return descriptor;
    }

    /**
     * Launch a missile from the specified coordinates.
     *
     * @param startX    x position
     * @param startY    y position
     */
    public void launch(float startX, float startY)
    {
//        EntityDescriptor descriptor = new EntityDescriptor();
//        descriptor._ASSET         = app.assets.getAnimationRegion(GameAssets._MISSILE_ASSET);
//        descriptor._FRAMES        = GameAssets._MISSILE_FRAMES;
//        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
//        descriptor._X             = (int) (startX / Gfx.getTileWidth());
//        descriptor._Y             = (int) (startY / Gfx.getTileHeight());
//        descriptor._Z             = app.entityUtils.getInitialZPosition(GraphicID.G_MISSILE);
//        descriptor._INDEX         = app.entityData.entityMap.size;
//
//        Missile missile = new Missile(app);
//        missile.initialise(descriptor);
//        app.entityData.addEntity(missile);
//
//        isMissileActive = true;
//        activeMissiles++;
    }

    public void shoot(DefenceStation station)
    {
        if (activeSparklers < _MAX_SPARKLERS)
        {
            SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_DEFENDER_BULLET);
            descriptor._POSITION.x    = (int) (station.sprite.getX() / Gfx.getTileWidth());
            descriptor._POSITION.y    = (int) (station.sprite.getY() / Gfx.getTileHeight()) + 2;
            descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_BULLET);
            descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER_BULLET);
            descriptor._PARENT        = station;
            descriptor._INDEX         = app.entityData.entityMap.size;

            DefenderBullet weapon = new DefenderBullet(GraphicID.G_DEFENDER_BULLET, app);
            weapon.initialise(descriptor);
            app.entityData.addEntity(weapon);

            activeSparklers++;
        }
    }

    public void killMissiles()
    {
        if (app.entityData.entityMap != null)
        {
            GdxSprite currentEntity;

            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                currentEntity = (GdxSprite) app.entityData.entityMap.get(i);

                if (currentEntity.gid == GraphicID.G_MISSILE)
                {
                    ((Missile) currentEntity).explode();
                }
            }
        }
    }

    public void pauseAllMissiles()
    {
        if (app.entityData.entityMap != null)
        {
            GdxSprite currentEntity;

            for (int i = 0; i < app.entityData.entityMap.size; i++)
            {
                currentEntity = (GdxSprite) app.entityData.entityMap.get(i);

                if (currentEntity.gid == GraphicID.G_MISSILE)
                {
                    currentEntity.setAction(Actions._PAUSED);
                }
            }
        }
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID.G_MISSILE_BASE;
    }
}
