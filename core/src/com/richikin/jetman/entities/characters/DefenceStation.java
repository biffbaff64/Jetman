package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.StopWatch;
import com.richikin.jetman.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class DefenceStation extends GdxSprite
{
    private       ZapSprite zapSprite;
    private       StopWatch shootTimer;
    private       float     shootInterval;
    private final App       app;

    DefenceStation(App _app)
    {
        super(GraphicID.G_DEFENDER, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_MISSILE_BASE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setAction(Actions._STANDING);

        shootTimer      = StopWatch.start();
        shootInterval   = app.roomManager.getFireRate();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getSpriteAction())
        {
            case _STANDING:
            {
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                elapsedAnimTime += Gdx.graphics.getDeltaTime();

                if (shootTimer.time(TimeUnit.MILLISECONDS) > shootInterval)
                {
                    app.missileBaseManager.shoot(this);

                    shootInterval = app.roomManager.getFireRate();
                    shootTimer.reset();
                }
            }
            break;

            case _KILLED:
            case _HURT:
            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(Actions._DEAD);
                zapSprite.setAction(Actions._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        updateCommon();
    }

    void explode()
    {
//        ExplosionManager explosionManager = new ExplosionManager();
//        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this, app);
//        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, zapSprite, app);
//
//        spriteAction = Actions._EXPLODING;
//        zapSprite.spriteAction = Actions._EXPLODING;
    }

    /**
     * Adds the spark zapper beam to the
     * defence station.
     */
    void addZapper()
    {
        SpriteDescriptor entityDescriptor = Entities.getDescriptor(GraphicID.G_DEFENDER_ZAP);
        entityDescriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        entityDescriptor._POSITION.x    = (int) (sprite.getX() / Gfx.getTileWidth());
        entityDescriptor._POSITION.y    = (int) ((sprite.getY() + frameHeight) / Gfx.getTileHeight());
        entityDescriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_ZAP);
        entityDescriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER_ZAP);
        entityDescriptor._INDEX         = app.entityData.entityMap.size;

        zapSprite = new ZapSprite(GraphicID.G_DEFENDER_ZAP, this, app);
        zapSprite.initialise(entityDescriptor);
        app.entityData.addEntity(zapSprite);
    }
}
