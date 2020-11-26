package com.richikin.jetman.entities.managers;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.characters.Missile;
import com.richikin.jetman.entities.characters.MissileBase;
import com.richikin.jetman.entities.characters.DefenderBullet;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
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

    public MissileBaseManager()
    {
        super( GraphicID.G_MISSILE_BASE);
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

        App.entities.missileBase = new MissileBase();
        App.entities.missileBase.initialise(descriptor);
        App.entityData.addEntity(App.entities.missileBase);

        App.entityManager._missileBaseIndex = App.entities.missileBase.spriteNumber;

        activeBases++;
    }

    private SpriteDescriptor createMissileBaseMarker()
    {
        Trace.__FILE_FUNC();

        SimpleVec2 vec2 = findCoordinates(GraphicID.G_MISSILE_BASE);

        baseLocatedLeft = (vec2.x < (Gfx.getMapWidth() / 2));

        baseTileX = vec2.x;
        baseTileY = vec2.y;

        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_MISSILE_BASE);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_MISSILE_BASE);
        descriptor._POSITION.x    = baseTileX;
        descriptor._POSITION.y    = baseTileY;
        descriptor._POSITION.z    = App.entityUtils.getInitialZPosition(GraphicID.G_MISSILE_BASE);
        descriptor._INDEX         = App.entityData.entityMap.size;

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
//        descriptor._ASSET         = App.assets.getAnimationRegion(GameAssets._MISSILE_ASSET);
//        descriptor._FRAMES        = GameAssets._MISSILE_FRAMES;
//        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
//        descriptor._X             = (int) (startX / Gfx.getTileWidth());
//        descriptor._Y             = (int) (startY / Gfx.getTileHeight());
//        descriptor._Z             = App.entityUtils.getInitialZPosition(GraphicID.G_MISSILE);
//        descriptor._INDEX         = App.entityData.entityMap.size;
//
//        Missile missile = new Missile();
//        missile.initialise(descriptor);
//        App.entityData.addEntity(missile);
//
//        isMissileActive = true;
//        activeMissiles++;
    }

    public void shoot(DefenceStation station)
    {
        if (activeSparklers < _MAX_SPARKLERS)
        {
            SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_DEFENDER_BULLET);
            descriptor._POSITION.x    = (int) (station.sprite.getX() / Gfx.getTileWidth());
            descriptor._POSITION.y    = (int) (station.sprite.getY() / Gfx.getTileHeight()) + 2;
            descriptor._POSITION.z    = App.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_BULLET);
            descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER_BULLET);
            descriptor._PARENT        = station;
            descriptor._INDEX         = App.entityData.entityMap.size;

            DefenderBullet weapon = new DefenderBullet(GraphicID.G_DEFENDER_BULLET);
            weapon.initialise(descriptor);
            App.entityData.addEntity(weapon);

            activeSparklers++;
        }
    }

    public void killMissiles()
    {
        if (App.entityData.entityMap != null)
        {
            GdxSprite currentEntity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                currentEntity = (GdxSprite) App.entityData.entityMap.get(i);

                if (currentEntity.gid == GraphicID.G_MISSILE)
                {
                    ((Missile) currentEntity).explode();
                }
            }
        }
    }

    public void pauseAllMissiles()
    {
        if (App.entityData.entityMap != null)
        {
            GdxSprite currentEntity;

            for (int i = 0; i < App.entityData.entityMap.size; i++)
            {
                currentEntity = (GdxSprite) App.entityData.entityMap.get(i);

                if (currentEntity.gid == GraphicID.G_MISSILE)
                {
                    currentEntity.setAction(ActionStates._PAUSED);
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
