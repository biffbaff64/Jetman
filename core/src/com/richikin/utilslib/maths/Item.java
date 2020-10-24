
package com.richikin.utilslib.maths;

import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class Item
{
    private       int       maximum;
    private       int       minimum;
    private       int       total;
    private       int       refillAmount;
    private final StopWatch stopWatch;

    public Item()
    {
        this(0, 100, 0);
    }

    public Item(int minimum, int maximum)
    {
        this(minimum, maximum, minimum);
    }

    public Item(int maximum)
    {
        this(0, maximum, 0);
    }

    public Item(int minimum, int maximum, int total)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.total = total;
        this.refillAmount = minimum;
        this.stopWatch = StopWatch.start();
    }

    public boolean slowDecrementOnTimer(int interval, int amount)
    {
        boolean empty = false;

        if (stopWatch.time(TimeUnit.MILLISECONDS) >= interval)
        {
            this.total -= amount;

            stopWatch.reset();
        }

        if (isEmpty() || isUnderflowing())
        {
            this.total = this.minimum;

            empty = true;
        }

        return empty;
    }

    public int getTotal()
    {
        this.validate();

        return this.total;
    }

    public void setTotal(int amount)
    {
        this.total = amount;
    }

    public int getMin()
    {
        return minimum;
    }

    public void setMin(int minimum)
    {
        this.minimum = minimum;
    }

    public int getMax()
    {
        return maximum;
    }

    public void setMax(int maximum)
    {
        this.maximum = maximum;
    }

    public void add(int amount)
    {
        if ((this.total += amount) < 0)
        {
            this.total = 0;
        }
        else
        {
            if (this.total > this.maximum)
            {
                this.total = this.maximum;
            }
        }
    }

    public void add(int amount, int wrap)
    {
        if ((this.total += amount) > wrap)
        {
            this.total -= wrap;
        }
    }

    public void subtract(int amount)
    {
        this.total = Math.max((this.total - amount), this.minimum);
    }

    public void subtract(int amount, int wrap)
    {
        if ((this.total -= amount) < this.minimum)
        {
            this.total = wrap;
        }
    }

    public void setMinMax(int minimum, int maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void setToMaximum()
    {
        this.total = this.maximum;
    }

    public void setToMinimum()
    {
        this.total = this.minimum;
    }

    public boolean isFull()
    {
        return this.total >= this.maximum;
    }

    public boolean isEmpty()
    {
        return this.total <= this.minimum;
    }

    public boolean isOverflowing()
    {
        return this.total > this.maximum;
    }

    private boolean isUnderflowing()
    {
        return this.total < this.minimum;
    }

    public void refill()
    {
        this.total = this.refillAmount;
    }

    public void refill(int refillAmount)
    {
        this.total = refillAmount;
    }

    public int getRefillAmount()
    {
        return refillAmount;
    }

    public void setRefillAmount(int refill)
    {
        this.refillAmount = refill;
    }

    public int getFreeSpace()
    {
        return this.maximum - this.total;
    }

    public void boostMax(int boost)
    {
        this.maximum += boost;
    }

    private void validate()
    {
        if (this.total < this.minimum)
        {
            this.total = this.minimum;
        }

        if (this.total > this.maximum)
        {
            this.total = this.maximum;
        }
    }
}
