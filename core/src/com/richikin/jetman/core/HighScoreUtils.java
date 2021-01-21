package com.richikin.jetman.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Json;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.Item;

import java.util.Arrays;
import java.util.Comparator;

public class HighScoreUtils implements Disposable
{
    public static final int    _MAX_HISCORES = 10;
    public static final String filePath      = "hiscore_data.json";

    public boolean newHighScoreAvailable;

    /**
     * The HighScore Table.
     * The table is 10 entrys in size PLUS one extra.
     * This extra entry is where a new hiscore is placed so that
     * array sorting can move it into its correct place.
     * <p>
     * Array index 0 is the highest value.
     * <p>
     * I thought about using Array but it just seemed
     * to be a bit much for such a small number of entries.
     */
    private       HighScore[] highScores;
    private final Json        json;

    public HighScoreUtils()
    {
        this.json       = new Json();
        this.highScores = new HighScore[_MAX_HISCORES + 1];

        for (int i = 0; i < (_MAX_HISCORES + 1); i++)
        {
            highScores[i] = new HighScore();
        }
    }

    /**
     * Adds a value to the extra position at the end of the table,
     * then sorts the table
     */
    public void addHighScore(HighScore highScore)
    {
        if (highScore.score >= highScores[_MAX_HISCORES - 1].score)
        {
            highScores[_MAX_HISCORES].level = highScore.level;
            highScores[_MAX_HISCORES].score = highScore.score;
            highScores[_MAX_HISCORES].name  = highScore.name;

            sortTable();

            writeTable();
        }
    }

    public int findInsertLevel(HighScore highScore)
    {
        int index = _MAX_HISCORES;

        while (highScores[index].score > highScore.score)
        {
            index--;
        }

        return index;
    }

    /**
     * Loads the hiscore data from the json file into the
     * hiscore table array.
     * <p>
     * NOTE: If the Json file does not exist then the table is set up
     * with default data and then written out as a Json file
     * for future use.
     */
    public void loadTableData()
    {
        if (!Gdx.files.local(filePath).exists())
        {
            setDefaultHiscoreTable();
        }

        highScores = json.fromJson(HighScore[].class, Gdx.files.local(filePath));
    }

    /**
     * Writes the hiscore table back to the json file
     */
    private void writeTable()
    {
        FileHandle handle = Gdx.files.local(filePath);

        handle.writeString(json.prettyPrint(json.toJson(highScores)), false);
    }

    /**
     * Sorts the table
     */
    private void sortTable()
    {
        HighScore[] temp = new HighScore[_MAX_HISCORES + 1];

        for (int i = 0; i < (_MAX_HISCORES + 1); i++)
        {
            temp[i] = new HighScore();

            temp[i].level = highScores[i].level;
            temp[i].score = highScores[i].score;
            temp[i].name  = highScores[i].name;
        }

        Arrays.sort(temp, new SortByScore());

        int j = _MAX_HISCORES;

        for (int i = 0; i < (_MAX_HISCORES + 1); i++)
        {
            highScores[i].rank  = i + 1;
            highScores[i].level = temp[j].level;
            highScores[i].score = temp[j].score;
            highScores[i].name  = temp[j--].name;
        }
    }

    /**
     * Checks if a new hiscore entry can be added.
     *
     * @return boolean - TRUE if can be added.
     */
    public boolean canAddNewEntry(Item _entry)
    {
        boolean available = false;

        if (_entry != null)
        {
            if (_entry.getTotal() >= highScores[_MAX_HISCORES - 1].score)
            {
                available = true;
            }
        }

        return available;
    }

    /**
     * Gets the hiscore table
     *
     * @return a reference to the table
     */
    public HighScore[] getHighScoreTable()
    {
        return highScores;
    }

    /**
     * Reset the hiscore table to default values.
     */
    public void resetTable()
    {
        setDefaultHiscoreTable();
        newHighScoreAvailable = false;
    }

    /**
     * Writes default values to the hiscore table if the
     * json file does not exist, ie because of a
     * fresh installation
     */
    private void setDefaultHiscoreTable()
    {
        final HighScore[] defaultTable =
            {
                new HighScore(11, 1, 0, "TABLE END"),
                new HighScore(10, 1, 1000, "CHUCKY"),
                new HighScore(9, 1, 2000, "THING"),
                new HighScore(8, 1, 3000, "LUIGI"),
                new HighScore(7, 1, 4000, "PACMAN"),
                new HighScore(6, 1, 5000, "KONG"),
                new HighScore(5, 1, 6000, "HORACE"),
                new HighScore(4, 1, 7000, "MARIO"),
                new HighScore(3, 1, 8000, "JS WILLY"),
                new HighScore(2, 1, 9000, "MONTY"),
                new HighScore(1, 1, 10000, "JETMAN"),
            };

        for (int i = 0; i < _MAX_HISCORES + 1; i++)
        {
            highScores[i].rank  = defaultTable[i].rank;
            highScores[i].level = defaultTable[i].level;
            highScores[i].score = defaultTable[i].score;
            highScores[i].name  = defaultTable[i].name;
        }

        sortTable();
        writeTable();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();
    }

    static class SortByScore implements Comparator<HighScore>
    {
        public int compare(HighScore a, HighScore b)
        {
            return Integer.compare(a.score, b.score);
        }
    }
}
