
package com.richikin.jetman.entities;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;

public interface EntityManagerComponent
{
    void init();

    void update();

    void create();

    SimpleVec2 findCoordinates(final GraphicID targetGID);

    Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID);

    void free();

    void free(final GraphicID _gid);

    void reset();

    int getActiveCount();

    void setActiveCount(int numActive);

    GraphicID getGID();

    boolean isPlaceable();

    void setPlaceable(boolean placeable);

    void dispose();
}
