
package com.richikin.utilslib.maths;

public abstract class NumberUtils
{
    public static int getCount(int currentTotal)
    {
        int count;

        if (currentTotal >= 1000)
        {
            count = 100;
        }
        else if (currentTotal >= 100)
        {
            count = 10;
        }
        else if (currentTotal >= 50)
        {
            count = 5;
        }
        else
        {
            count = 1;
        }

        return count;
    }
}
