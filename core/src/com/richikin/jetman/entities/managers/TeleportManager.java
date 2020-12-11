package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.characters.Teleporter;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;

public class TeleportManager extends GenericEntityManager
{
    public Array<SimpleVec2> mapPositions;
    public int[]             index;
    public int               targetBooth;
    public boolean           teleportActive;
    public SimpleVec2F       targetDistance;
    public SimpleVec2        targetDirection;

    public TeleportManager()
    {
        super(GraphicID.G_TRANSPORTER);
    }

    @Override
    public void init()
    {
        activeCount = 0;

        createTransporters();
    }

    @Override
    public void update()
    {
        if (activeCount == 0)
        {
            createTransporters();
        }
    }

    @Override
    public void free()
    {
        activeCount = 0;
    }

    private void createTransporters()
    {
        if (App.entityUtils.canUpdate(GraphicID.G_TRANSPORTER))
        {
            Trace.__FILE_FUNC();

            mapPositions   = new Array<>();
            activeCount    = 0;
            teleportActive = false;

            Array<SimpleVec2> coords = findMultiCoordinates(GraphicID.G_TRANSPORTER);

            if (coords.isEmpty())
            {
                Trace.__FILE_FUNC("WARNING: Transporter count ZERO");
            }
            else
            {
                SpriteDescriptor descriptor;

                index = new int[RoomManager._MAX_TELEPORTERS];

                App.entities.teleporters = new Teleporter[RoomManager._MAX_TELEPORTERS];

                for (int i = 0; i < RoomManager._MAX_TELEPORTERS; i++)
                {
                    descriptor             = App.entities.getDescriptor(GraphicID.G_TRANSPORTER);
                    descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_TRANSPORTER);
                    descriptor._POSITION.x = coords.get(i).x;
                    descriptor._POSITION.y = coords.get(i).y;
                    descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_TRANSPORTER);
                    descriptor._INDEX      = App.entityData.entityMap.size;

                    App.entities.teleporters[activeCount] = new Teleporter();
                    App.entities.teleporters[activeCount].initialise(descriptor);
                    App.entities.teleporters[activeCount].teleporterNumber = activeCount;

                    App.entityData.addEntity(App.entities.teleporters[activeCount]);
                    App.entityManager._teleportIndex[activeCount] = descriptor._INDEX;

                    //
                    // Store the map positions of each teleporter
                    // for future use
                    mapPositions.add(new SimpleVec2((int) App.entities.teleporters[activeCount].sprite.getX(), 0));

                    index[activeCount] = descriptor._INDEX;

                    activeCount++;
                }
            }
        }
    }

    public void teleportVisual(boolean _entered)
    {
        teleportActive                 = true;
        App.entityManager.teleportBeam = new TeleportBeam();

        if (_entered)
        {
            App.getHud().messageManager.addZoomMessage("TeleportMessage", 1500);
            App.entityManager.teleportBeam.entryVisual();

//            Sfx.inst().startSound(Sfx.inst().SFX_TELEPORT);
        }
        else
        {
            App.entityManager.teleportBeam.exitVisual();
        }
    }

    public void setTeleporting(int index)
    {
        Trace.__FILE_FUNC("index: " + index);

        targetDistance  = new SimpleVec2F();
        targetDirection = new SimpleVec2();

        App.appState.set(StateID._STATE_TELEPORTING);

        if (Developer.isDevMode())
        {
            for (int i=0; i<RoomManager._MAX_TELEPORTERS; i++)
            {
                Trace.dbg("Teleporter " + i + " X: " + App.getTeleporter(i).sprite.getX());
            }
        }

        if (index == 0)
        {
            if (App.getTeleporter(0).sprite.getX() < App.getTeleporter(1).sprite.getX())
            {
                targetDirection.setX(Movement._DIRECTION_RIGHT);
                targetDistance.set
                    (
                        App.getTeleporter(1).sprite.getX() - App.getTeleporter(0).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }
            else
            {
                targetDirection.setX(Movement._DIRECTION_LEFT);
                targetDistance.set
                    (
                        App.getTeleporter(0).sprite.getX() - App.getTeleporter(1).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }

            targetBooth = 1;
        }
        else
        {
            if (App.getTeleporter(1).sprite.getX() > App.getTeleporter(0).sprite.getX())
            {
                targetDirection.setX(Movement._DIRECTION_LEFT);
                targetDistance.set
                    (
                        App.getTeleporter(1).sprite.getX() - App.getTeleporter(0).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }
            else
            {
                targetDirection.setX(Movement._DIRECTION_RIGHT);
                targetDistance.set
                    (
                        App.getTeleporter(0).sprite.getX() - App.getTeleporter(1).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }

            targetBooth = 0;
        }

        if (App.getTeleporter(targetBooth).sprite.getY() > App.getPlayer().sprite.getY())
        {
            targetDirection.setY(Movement._DIRECTION_UP);
        }
        else if (App.getTeleporter(targetBooth).sprite.getY() < App.getPlayer().sprite.getY())
        {
            targetDirection.setY(Movement._DIRECTION_DOWN);
        }

        App.getPlayer().teleport.start();

        if (Developer.isDevMode())
        {
            Trace.dbg("targetBooth: " + targetBooth);
            Trace.dbg("targetDirection: " + targetDirection);
            Trace.dbg("targetDistance: " + targetDistance);
            Trace.dbg("Teleport ready.");
        }
    }

    public void endTeleporting()
    {
        App.appState.set(StateID._STATE_GAME);

        App.getPlayer().isTeleporting = false;
        App.getPlayer().sprite.setX(App.getTeleporter(targetBooth).sprite.getX());
        App.getPlayer().sprite.setY(App.getTeleporter(targetBooth).sprite.getY());
        App.getPlayer().setAction(ActionStates._STANDING);
        App.getPlayer().actionButton.removeAction();

        teleportActive = false;

//        GdxSprite entity;
//
//        for (int i = 0; i < App.entityData.entityMap.size; i++)
//        {
//            entity = (GdxSprite) App.entityData.entityMap.get(i);
//
//            if ((entity != null) && (entity.gid == GraphicID.G_MESSAGE_BUBBLE))
//            {
//                if (((InfoBox) entity).parentID == GraphicID.G_TRANSPORTER)
//                {
//                    entity.spriteAction = Actions._DYING;
//                }
//            }
//        }
    }
}
