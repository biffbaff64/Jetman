package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.characters.DefenceStation;
import com.richikin.jetman.entities.characters.Missile;
import com.richikin.jetman.entities.characters.MissileBase;
import com.richikin.jetman.entities.characters.SparkleWeapon;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2;

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
        activeBases         = 0;
        activeMissiles      = 0;
        activeSparklers     = 0;
        isMissileActive     = false;
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
        activeBases = 0;

        SpriteDescriptor entityDescriptor = createMissileBaseMarker();

        MissileBase missileBase = new MissileBase(app);
        missileBase.initialise(entityDescriptor);
        app.entityData.addEntity(missileBase);

        app.entityManager._missileBaseIndex = missileBase.spriteNumber;

        activeBases++;
    }

    private EntityDescriptor createMissileBaseMarker()
    {
        SimpleVec2 vec2 = findCoordinates(GraphicID.G_MISSILE_BASE);

        baseLocatedLeft = (vec2.x < (Gfx.getMapWidth() / 2));

        baseTileX = vec2.x;
        baseTileY = vec2.y;

        EntityDescriptor entityDescriptor = new EntityDescriptor();
        entityDescriptor._ASSET         = app.assets.getAnimationRegion(GameAssets._MISSILE_BASE_ASSET);
        entityDescriptor._FRAMES        = GameAssets._MISSILE_BASE_FRAMES;
        entityDescriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        entityDescriptor._X             = baseTileX;
        entityDescriptor._Y             = baseTileY;
        entityDescriptor._Z             = app.entityUtils.getInitialZPosition(GraphicID.G_MISSILE_BASE);
        entityDescriptor._INDEX         = app.entityData.entityMap.size;

        return entityDescriptor;
    }

    /**
     * Launch a missile from the specified coordinates.
     *
     * @param startX    x position
     * @param startY    y position
     */
    public void launch(float startX, float startY)
    {
        EntityDescriptor entityDescriptor = new EntityDescriptor();
        entityDescriptor._ASSET         = app.assets.getAnimationRegion(GameAssets._MISSILE_ASSET);
        entityDescriptor._FRAMES        = GameAssets._MISSILE_FRAMES;
        entityDescriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        entityDescriptor._X             = (int) (startX / Gfx.getTileWidth());
        entityDescriptor._Y             = (int) (startY / Gfx.getTileHeight());
        entityDescriptor._Z             = app.entityUtils.getInitialZPosition(GraphicID.G_MISSILE);
        entityDescriptor._INDEX         = app.entityData.entityMap.size;

        Missile missile = new Missile(app);
        missile.initialise(entityDescriptor);
        app.entityData.addEntity(missile);

        isMissileActive = true;
        activeMissiles++;
    }

    public void shoot(DefenceStation station)
    {
        if (activeSparklers < (_MAX_SPARKLERS + app.getLevel()))
        {
            EntityDescriptor entityDescriptor = new EntityDescriptor();
            entityDescriptor._ASSET         = app.assets.getAnimationRegion(GameAssets._SPARKLE_WEAPON_ASSET);
            entityDescriptor._FRAMES        = GameAssets._SPARKLE_WEAPON_FRAMES;
            entityDescriptor._PLAYMODE      = Animation.PlayMode.LOOP;
            entityDescriptor._X             = (int) (station.sprite.getX() / Gfx.getTileWidth());
            entityDescriptor._Y             = (int) (station.sprite.getY() / Gfx.getTileHeight()) + 2;
            entityDescriptor._Z             = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_BULLET);
            entityDescriptor._INDEX         = app.entityData.entityMap.size;

            SparkleWeapon weapon = new SparkleWeapon(GraphicID.G_DEFENDER_BULLET, app);
            weapon.initialise(entityDescriptor);

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
