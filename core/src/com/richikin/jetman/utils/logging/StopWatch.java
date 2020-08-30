
package com.richikin.jetman.utils.logging;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("WeakerAccess")
public class StopWatch
{
    private long starts;

    private StopWatch()
    {
        reset();
    }

    public static StopWatch start()
    {
        return new StopWatch();
    }

    public void reset()
    {
        starts = System.currentTimeMillis();
    }

    public long time()
    {
        long ends = System.currentTimeMillis();

        return ends - starts;
    }

    public long time(TimeUnit unit)
    {
        return unit.convert(time(), TimeUnit.MILLISECONDS);
    }
}
