
package com.richikin.utilslib.maths;

import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class ItemF
{
    protected float     maximum;
    protected float     minimum;
    protected float     total;
    protected float     refillAmount;
    protected StopWatch stopWatch;

    public ItemF()
    {
        this.maximum = 100;
        this.minimum = 0;
        this.total = 0;
        this.refillAmount = 0;
        this.stopWatch = StopWatch.start();
    }

    public ItemF(float minimum, float maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.total = minimum;
        this.refillAmount = minimum;
        this.stopWatch = StopWatch.start();
    }

    public ItemF(float minimum, float maximum, float total)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.total = total;
        this.refillAmount = minimum;
        this.stopWatch = StopWatch.start();
    }

    public ItemF(float maximum)
    {
        this.minimum = 0;
        this.maximum = maximum;
        this.total = 0;
        this.refillAmount = 0;
        this.stopWatch = StopWatch.start();
    }

    public boolean slowDecrementOnTimer(float interval, float amount)
    {
        boolean empty = false;

        if(stopWatch.time(TimeUnit.MILLISECONDS) >= interval)
        {
            this.total -= amount;

            stopWatch.reset();
        }

        if(isEmpty() || isUnderflowing())
        {
            this.total = this.minimum;

            empty = true;
        }

        return empty;
    }

    public void setMin(float minimum)
    {
        this.minimum = minimum;
    }

    public void setMax(float maximum)
    {
        this.maximum = maximum;
    }

    public float getTotal()
    {
        this.validate();

        return this.total;
    }

    public float getMin()
    {
        return minimum;
    }

    public float getMax()
    {
        return maximum;
    }

    public void add(float amount)
    {
        if((this.total += amount) < 0)
        {
            this.total = 0;
        }
        else
        {
            if(this.total > this.maximum)
            {
                this.total = this.maximum;
            }
        }
    }

    public void add(float amount, float wrap)
    {
        if((this.total += amount) > wrap)
        {
            this.total = this.minimum;
        }
    }

    public void subtract(float amount)
    {
        this.total = Math.max((this.total - amount), this.minimum);
    }

    public void subtract(float amount, float wrap)
    {
        if ((this.total -= amount) < this.minimum)
        {
            this.total = wrap;
        }
    }

    public void setMinMax(float minimum, float maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void setTotal(float amount)
    {
        this.total = amount;
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
        return (this.total >= this.maximum);
    }

    public boolean isEmpty()
    {
        return (this.total <= this.minimum);
    }

    public boolean hasRoom()
    {
        return !isFull();
    }

    public boolean isOverflowing()
    {
        return this.total > this.maximum;
    }

    public boolean isUnderflowing()
    {
        return this.total < this.minimum;
    }

    public void refill()
    {
        this.total = this.refillAmount;
    }

    public void refill(float refillAmount)
    {
        this.total = refillAmount;
    }

    public void setRefillAmount(float refill)
    {
        this.refillAmount = refill;
    }

    protected void validate()
    {
        if(this.total < this.minimum)
        {
            this.total = this.minimum;
        }

        if(this.total > this.maximum)
        {
            this.total = this.maximum;
        }
    }
}
