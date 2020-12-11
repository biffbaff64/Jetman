package com.richikin.jetman.entities.types;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.GenericCollisionListener;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class TeleportThief extends GdxSprite
{
    private int         heldTransporter;
    private boolean     attachedToTransporter;
    private boolean     canTakeTransporter;
    private StopWatch   stopWatch;
    private StopWatch   collectTimer;
    private StopWatch   carryTimer;
    private int         carryTime;
    private float       restingTime;

    public TeleportThief(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        animation.setFrameDuration(0.3f / 6f);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_GROUND | Gfx.CAT_TELEPORTER;

        speed.setX(MathUtils.random(Gfx._VIEW_WIDTH * 0.0015f, Gfx._VIEW_WIDTH * 0.003f));
        speed.setY(0);

        setAction(ActionStates._RUNNING);

        if (sprite.getX() < App.getPlayer().sprite.getX())
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
        else
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }

        addCollisionListener(new GenericCollisionListener(this));

        collectTimer = StopWatch.start();
        stopWatch = StopWatch.start();
        carryTimer = StopWatch.start();

        attachedToTransporter = false;
        canTakeTransporter = true;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                if (!attachedToTransporter
                    && !canTakeTransporter
                    && (collectTimer.time(TimeUnit.MILLISECONDS) > 10000))
                {
                    canTakeTransporter = true;
                }

                moveEntity();
            }
            break;

            case _HOLDING:
            {
                moveEntity();

                if ((carryTimer.time(TimeUnit.MILLISECONDS) >= carryTime)
                    && isValidDropPoint())
                {
                    if ((MathUtils.random(100) < 20)
                        && (sprite.getX() > (Gfx._VIEW_WIDTH * 2))
                        && (sprite.getX() < (Gfx.getMapWidth() - (Gfx._VIEW_WIDTH * 2))))
                    {
                        attachedToTransporter = false;
                        App.getTeleporter(heldTransporter).collector = null;
                        App.getTeleporter(heldTransporter).isCollected = false;
                        App.getTeleporter(heldTransporter).setAction(ActionStates._STANDING);

                        collectTimer.reset();
                        setAction(ActionStates._RUNNING);
                    }
                }
            }
            break;

            case _STEAL_TELEPORTER:
            {
                if (collisionObject.idBottom == GraphicID.G_TRANSPORTER)
                {
                    attachedToTransporter = true;

                    App.getTeleporter(heldTransporter).isCollected = true;
                    App.getTeleporter(heldTransporter).collector = this;
                    App.getTeleporter(heldTransporter).setAction(ActionStates._HELD);

                    sprite.setY
                        (
                            App.getTeleporter(heldTransporter).sprite.getY()
                                + App.getTeleporter(heldTransporter).frameHeight
                        );

                    setAction(ActionStates._CLIMBING);
                    stopWatch.reset();
                    restingTime = 750;
                }
                else if ((sprite.getY() + frameHeight) < 0)
                {
                    setAction(ActionStates._HURT);
                }
                else
                {
                    sprite.translate(0, (speed.getY() * Movement._DIRECTION_DOWN));
                    distance.addY(speed.getY());
                    speed.y += 0.2f;
                }
            }
            break;

            case _USE_TELEPORTER:
            {
                if (collisionObject.idBottom == GraphicID._GROUND)
                {
                    setAction(ActionStates._TELEPORTING);
                    stopWatch.reset();
                    restingTime = 750;
                }
                else if ((sprite.getY() + frameHeight) < 0)
                {
                    setAction(ActionStates._HURT);
                }
                else
                {
                    sprite.translate(0, (speed.getY() * Movement._DIRECTION_DOWN));
                    distance.addY(speed.getY());
                    speed.y += 0.2f;
                }
            }
            break;

            case _TELEPORTING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime)
                {
                    int targetBooth = (heldTransporter + 1) & 1;

                    sprite.setPosition
                        (
                            App.getTeleporter(targetBooth).getCollisionRectangle().x,
                            App.getTeleporter(targetBooth).getCollisionRectangle().y + this.frameHeight
                        );

                    direction.setY(Movement._DIRECTION_UP);

                    attachedToTransporter = false;
                    canTakeTransporter = false;
                    setAction(ActionStates._RUNNING);
                }
            }
            break;

            case _CLIMBING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) >= restingTime)
                {
                    if (distance.getY() <= 0)
                    {
                        setAction(ActionStates._HOLDING);
                        speed.setY(0);

                        carryTime = 2000 + (MathUtils.random(200, 500) * 10);
                        carryTimer.reset();

                        if (MathUtils.random(100) < 35)
                        {
                            direction.toggleX();
                        }
                    }
                    else
                    {
                        sprite.translate(0, (speed.getY() * Movement._DIRECTION_UP));
                        distance.subY(speed.getY());
                        speed.y -= 0.2f;
                    }
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

                if (getAction() == ActionStates._KILLED)
                {
                    App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(gid));
                }

                if (attachedToTransporter)
                {
                    App.getTeleporter(heldTransporter).setAction(ActionStates._STANDING);

                }

                setAction(ActionStates._EXPLODING);
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
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
    }

    @Override
    public void animate()
    {
        switch (getAction())
        {
            case _KILLED:
            case _EXPLODING:
            {
            }
            break;

            case _RUNNING:
            case _STEAL_TELEPORTER:
            case _USE_TELEPORTER:
            case _CLIMBING:
            default:
            {
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            }
            break;
        }
    }

    private void moveEntity()
    {
        sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

        distance.subX(speed.getX());
        distance.subY(speed.getY());

        wrap();

        if (distance.getY() <= 0)
        {
            speed.setY(0);
            direction.setY(Movement._DIRECTION_STILL);
        }
        else
        {
            speed.y -= 0.2f;
        }

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);

        if (canTakeTransporter && !attachedToTransporter)
        {
            for (int i=0; i<2; i++)
            {
                if (!attachedToTransporter
                    && (sprite.getX() > App.getTeleporter(i).sprite.getX())
                    && (sprite.getX() < (App.getTeleporter(i).sprite.getX() + (App.getTeleporter(i).frameWidth / 3)))
                    && (sprite.getY() > (App.getTeleporter(i).sprite.getY() + App.getTeleporter(i).frameHeight)))
                {
                    if (!App.getTeleporter(i).isCollected && (MathUtils.random(100) < 25))
                    {
                        heldTransporter = i;
                        canTakeTransporter = false;

                        collectTimer.reset();
                        distance.setY(0);                            // ?????
                        direction.setY(Movement._DIRECTION_STILL);   // ?????
                        setAction(ActionStates._STEAL_TELEPORTER);
                    }
                }
            }
        }
    }

    /**
     * Checks if safe to drop the teleporter.
     *
     * @return TRUE if safe to drop.
     */
    public boolean isValidDropPoint()
    {
        return  (App.collisionUtils.getMarkerTileOn(((int) sprite.getX() / Gfx.getTileWidth()), 1) == TileID._UNKNOWN);
    }
}
