package com.richikin.utilslib.core;

public class HighScore
{
    public int rank;
    public int level;
    public int score;
    public String name;

    public HighScore()
    {
        this.rank   = 0;
        this.level  = 0;
        this.score  = 0;
        this.name   = "";
    }

    public HighScore(int _rank, int _level, int _score, String _name)
    {
        this.rank   = _rank;
        this.level  = _level;
        this.score  = _score;
        this.name   = _name;
    }
}
