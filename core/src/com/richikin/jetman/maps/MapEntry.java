package com.richikin.jetman.maps;

public class MapEntry
{
    public final float baseOffset;
    public final int   fireRate;

    public final int maxAsteroids;
    public final int maxGreenBlocks;
    public final int maxWheels;
    public final int max3Balls;
    public final int maxStarSpinners;
    public final int maxTwinkles;
    public final int maxBlobs;
    public final int maxDogs;
    public final int maxTopSpinners;
    public final int max3LegAliens;
    public final int max3BallAliens;
    public final int maxSpinningBalls;

    public MapEntry()
    {
        this.baseOffset         = 0;
        this.fireRate           = 0;

        this.maxAsteroids       = 0;
        this.maxGreenBlocks     = 0;
        this.maxWheels          = 0;
        this.max3Balls          = 0;
        this.maxStarSpinners    = 0;
        this.maxTwinkles        = 0;
        this.maxBlobs           = 0;
        this.maxDogs            = 0;
        this.maxTopSpinners     = 0;
        this.max3LegAliens      = 0;
        this.max3BallAliens     = 0;
        this.maxSpinningBalls   = 0;
    }

    public MapEntry(float _baseOffset,
                    int _fireRate,
                    int _maxAsteroids, int _maxGreenBlocks, int _maxWheels,
                    int _max3Balls, int _maxStarSpinners, int _maxTwinkles, int _maxBlobs, int _maxDogs,
                    int _maxTopSpinners, int _max3LegAliens, int _max3BallAliens, int _maxSpinningBalls)
    {
        this.baseOffset         = _baseOffset;
        this.fireRate           = _fireRate;

        this.maxAsteroids       = _maxAsteroids;
        this.maxGreenBlocks     = _maxGreenBlocks;
        this.maxWheels          = _maxWheels;
        this.max3Balls          = _max3Balls;
        this.maxStarSpinners    = _maxStarSpinners;
        this.maxTwinkles        = _maxTwinkles;
        this.maxBlobs           = _maxBlobs;
        this.maxDogs            = _maxDogs;
        this.maxTopSpinners     = _maxTopSpinners;
        this.max3LegAliens      = _max3LegAliens;
        this.max3BallAliens     = _max3BallAliens;
        this.maxSpinningBalls   = _maxSpinningBalls;
    }
}
