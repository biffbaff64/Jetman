package com.richikin.jetman.entities.structures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.utilslib.logging.Trace;

public class MissileBase extends GdxSprite
{
    private GdxSprite     topSection;
    private TextureRegion emptyFrame;

    public MissileBase()
    {
        super(GraphicID.G_MISSILE_BASE);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MISSILE_BASE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        emptyFrame = App.assets.getAnimationRegion("launcher_top_empty");

        setAction(ActionStates._STANDING);

        addTopSection();

        App.gameProgress.baseDestroyed = false;

        addDynamicPhysicsBody();
    }

    @Override
    public void update(int spriteNum)
    {
        topSection.preUpdate();

        switch(getAction())
        {
            case _STANDING:
            case _WAITING:
            case _EXPLODING:
            case _RESETTING:
            {
            }
            break;

            case _FIGHTING:
            {
                App.getHud().messageManager.addZoomMessage
                    (
                        "missilewarning",
                        2000,
                        185, (720 - 196)
                    );

                App.missileBaseManager.launch(topSection.sprite.getX(), topSection.sprite.getY());
                App.missileBaseManager.launch(topSection.sprite.getX(), topSection.sprite.getY());

                topSection.isFlippedX = (sprite.getX() < App.getPlayer().sprite.getX());

                setAction(ActionStates._WAITING);
            }
            break;

            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);
                explosionManager.createExplosion(GraphicID.G_EXPLOSION256, topSection);

                App.defenceStationManager.killStations();
                App.missileBaseManager.killMissiles();

                setAction(ActionStates._EXPLODING);

                Shake.start();
            }
            break;

            case _DYING:
            {
                Trace.__FILE_FUNC("---------- BASE DESTROYED ----------");

                App.getHud().messageManager.addZoomMessage("base_destroyed", 3000);

                App.gameProgress.baseDestroyed = true;

                App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(gid));

                setAction(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        animate();

        updateCommon();
        topSection.updateCommon();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

        if (topSection != null)
        {
            switch (getAction())
            {
                case _STANDING:
                {
                    topSection.elapsedAnimTime += Gdx.graphics.getDeltaTime();
                    topSection.sprite.setRegion(App.entityUtils.getKeyFrame
                        (
                            topSection.animation,
                            topSection.elapsedAnimTime,
                            true
                        ));
                }
                break;

                case _FIGHTING:
                case _WAITING:
                {
                    topSection.sprite.setRegion(emptyFrame);
                }
                break;

                default:
                {
                }
                break;
            }
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        topSection.sprite.setPosition(sprite.getX() + 15, sprite.getY() + this.frameHeight);

        super.draw(spriteBatch);
        topSection.draw(spriteBatch);
    }

    @Override
    public void setAction(ActionStates action)
    {
        super.setAction(action);

        if (topSection != null)
        {
            topSection.setAction(action);
        }
    }

    /**
     * Adds the launcher top section to the
     * missile base.
     */
    public void addTopSection()
    {
        Trace.__FILE_FUNC();

        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_MISSILE_LAUNCHER);
        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        descriptor._POSITION.x    = (int) (this.sprite.getX() / Gfx.getTileWidth());
        descriptor._POSITION.y    = (int) (this.sprite.getY() / Gfx.getTileHeight()) - 1;
        descriptor._POSITION.z    = App.entityUtils.getInitialZPosition(GraphicID.G_MISSILE_LAUNCHER);
        descriptor._INDEX         = App.entityData.entityMap.size;
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_MISSILE_LAUNCHER);

        topSection = new GdxSprite(GraphicID.G_MISSILE_LAUNCHER);
        topSection.bodyCategory = Gfx.CAT_FIXED_ENEMY;
        topSection.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
        topSection.create(descriptor);
        topSection.sprite.setPosition(topSection.sprite.getX() + 15, topSection.sprite.getY() + this.frameHeight);
        topSection.animation.setFrameDuration(0.5f / 6f);
        topSection.setAction(ActionStates._STANDING);
        topSection.addDynamicPhysicsBody();

        App.entityData.addEntity(topSection);
    }

    @Override
    public void tidy(int index)
    {
        App.missileBaseManager.free();
        collisionObject.kill();
        App.entityData.removeEntity(index);
        App.gameProgress.levelCompleted = true;
    }
}
