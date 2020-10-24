
package com.richikin.jetman.entities.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.richikin.utilslib.logging.Trace;

public class FixedPath
{
    public final int            pathNumber;
    public final Array<Vector2> data;

    public FixedPath(int _pathNum)
    {
        this.pathNumber = _pathNum;
        this.data = new Array<>();
    }

    public void debug()
    {
        Trace.__FILE_FUNC("Path Number: " + pathNumber);
        Trace.dbg("Number of entries: " + data.size);

        for (int i=0; i<data.size; i++)
        {
            Trace.dbg(data.get(i).toString());
        }
    }
}
