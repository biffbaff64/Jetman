package com.richikin.jetman.entities.systems;

import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.SpriteDescriptor;

public class EntityCreationSystem
{
    private SpriteDescriptor spriteDescriptor;
    private final App app;

    public EntityCreationSystem(App _app)
    {
        this.app = _app;
    }

    public void processPlacementTiles()
    {
        for (SpriteDescriptor descriptor : app.mapData.placementTiles)
        {
            spriteDescriptor = Entities.getDescriptor(descriptor._GID);

            switch (descriptor._GID)
            {
                case G_PLAYER:
                {
                    addPlayer();
                }
                break;

                case G_ROVER:
                {
                    addRover();
                }
                break;

                case G_TRANSPORTER:
                {
                    addTeleporters();
                }

                default:
                    break;
            }
        }
    }

    private void addPlayer()
    {
    }

    private void addRover()
    {
    }

    private void addTeleporters()
    {
    }
}
