package com.richikin.jetman.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.characters.misc.DefenceStation;
import com.richikin.jetman.entities.components.IEntityManagerComponent;
import com.richikin.jetman.entities.objects.BaseEntity;
import com.richikin.utilslib.logging.Trace;

import java.util.ArrayList;

public class EntityData implements Disposable
{
    public Array<BaseEntity>                  entityMap;
    public ArrayList<IEntityManagerComponent> managerList;
    public DefenceStation[]                   defenceStations;

    public EntityData()
    {
    }

    public void createData()
    {
        Trace.__FILE_FUNC();

        entityMap   = new Array<>();
        managerList = new ArrayList<>();
    }

    public void update()
    {
        //
        // Scan through the entity map and update the
        // number of active entities for each type.
        int thisCount;

        for (final IEntityManagerComponent managerComponent : managerList)
        {
            thisCount = 0;

            for (BaseEntity baseEntity : new Array.ArrayIterator<>(entityMap))
            {
                if (baseEntity.gid == managerComponent.getGID())
                {
                    thisCount++;
                }
            }

            managerComponent.setActiveCount(thisCount);
        }
    }

    public int addManager(IEntityManagerComponent manager)
    {
        managerList.add(manager);

        return managerList.size() - 1;
    }

    public void addEntity(BaseEntity _entity)
    {
        if (_entity != null)
        {
            entityMap.add(_entity);
        }
        else
        {
            Trace.__FILE_FUNC("***** Attempt to add NULL Object, EntityMap current size: " + entityMap.size);
        }
    }

    public void removeEntity(int index)
    {
        entityMap.removeIndex(index);
    }

    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        for (int i = 0; i < entityMap.size; i++)
        {
            if (entityMap.get(i) != null)
            {
                entityMap.get(i).dispose();
            }
        }

        for (IEntityManagerComponent manager : managerList)
        {
            manager.dispose();
        }

        entityMap.clear();
        managerList.clear();

        entityMap   = null;
        managerList = null;
        defenceStations = null;
    }
}
