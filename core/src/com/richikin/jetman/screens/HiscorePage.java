
package com.richikin.jetman.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.misc.HighScore;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.states.StateManager;
import com.richikin.utilslib.ui.IUIPage;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
public class HiscorePage implements IUIPage, Disposable
{
    private static final int _RANK_X  = 360;
    private static final int _LEVEL_X = 580;
    private static final int _SCORE_X = 760;

    private static final int _TABLE_Y = 460;
    private static final int _SPACING = 64;

    private static final int _FNT_SIZE = 48;

    private static final int _DISPLAYED_HISCORES = 6;

    private final Color[] colours =
        {
            Color.CYAN,
            Color.SKY,
            Color.SKY,
            Color.SLATE,
            Color.SLATE,
            Color.ROYAL,
            Color.ROYAL,
            Color.BLUE,
            Color.BLUE,
            Color.NAVY,
            Color.NAVY,
            Color.BLUE,
            Color.BLUE,
            Color.ROYAL,
            Color.ROYAL,
            Color.SLATE,
            Color.SLATE,
            Color.SKY,
            Color.SKY,
            Color.CYAN,
        };

    private Label[]         levelLabels;
    private Label[]         rankLabels;
    private Label[]         scoreLabels;

    private final StopWatch    stopWatch;
    private       StateManager state;
    private       Texture foreground;
    private final App     app;

    private int[]           colorIndex;
    private int             loopCount;

    public HiscorePage(App _app)
    {
        this.app = _app;

        addItems();
        addClickListeners();

        state = new StateManager();

        stopWatch = StopWatch.start();

        showItems(false);
    }

    @Override
    public boolean update()
    {
        boolean isFinished = false;

        if (state.get() != StateID._STATE_NEW_HISCORE)
        {
            if (stopWatch.time(TimeUnit.MILLISECONDS) >= 75)
            {
                for (int i = 0; i < _DISPLAYED_HISCORES; i++)
                {
                    rankLabels[i].setStyle(new Label.LabelStyle(rankLabels[i].getStyle().font, colours[colorIndex[i]]));
                    levelLabels[i].setStyle(new Label.LabelStyle(levelLabels[i].getStyle().font, colours[colorIndex[i]]));
                    scoreLabels[i].setStyle(new Label.LabelStyle(scoreLabels[i].getStyle().font, colours[colorIndex[i]]));

                    if (--colorIndex[i] < 0)
                    {
                        colorIndex[i] = (colours.length - 1);

                        if (i == 0)
                        {
                            if (++loopCount >= 10)
                            {
                                isFinished = true;
                            }
                        }
                    }
                }

                stopWatch.reset();
            }        }

        return isFinished;
    }

    @Override
    public void reset()
    {
        loopCount = 0;
    }

    @Override
    public void show()
    {
        showItems(true);

        loopCount = 0;

        if (app.gameProgress.newHiScoreAvailable)
        {
            state.set(StateID._STATE_NEW_HISCORE);
        }
        else
        {
            state.set(StateID._STATE_PANEL_UPDATE);
        }

        stopWatch.reset();
    }

    @Override
    public void hide()
    {
        showItems(false);

        app.gameProgress.newHiScoreAvailable = false;
    }

    @Override
    public void draw(final SpriteBatch spriteBatch, float originX, float originY)
    {
        if (foreground != null)
        {
            spriteBatch.draw(foreground, originX, originY);
        }
    }

    /**
     * Adds the Menu items to the stage
     */
    private void addItems()
    {
        final float originX = app.baseRenderer.hudGameCamera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2);
        final float originY = app.baseRenderer.hudGameCamera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2);

        foreground = app.assets.loadSingleAsset("data/hiscore_foreground.png", Texture.class);

        Scene2DUtils scene2DUtils = new Scene2DUtils(app);

        rankLabels  = new Label[Constants._MAX_HISCORES];
        levelLabels = new Label[Constants._MAX_HISCORES];
        scoreLabels = new Label[Constants._MAX_HISCORES];

        app.highScoreUtils.loadTableData();

        HighScore highScore = new HighScore();

        if (app.gameProgress.newHiScoreAvailable)
        {
            highScore.score = app.gameProgress.getScoreOne().getTotal();
            highScore.level = app.gameProgress.playerLevel[Player._PLAYER_ONE.get()];
            highScore.rank = app.highScoreUtils.findInsertLevel(highScore);

            app.highScoreUtils.addHighScore(highScore);
        }

        colorIndex = new int[Constants._MAX_HISCORES];
        loopCount = 0;

        for (int i=0; i<Constants._MAX_HISCORES; i++)
        {
            // Hiscore table rank
            rankLabels[i] = scene2DUtils.addLabel
                (
                    "" + (i + 1),
                    (int) (originX + _RANK_X), (int) (originY + _TABLE_Y - (_SPACING * i)),
                    _FNT_SIZE, Color.WHITE, GameAssets._BENZOIC_FONT
                );

            // The game level achieved
            levelLabels[i] = scene2DUtils.addLabel
                (
                    "" + app.highScoreUtils.getHighScoreTable()[i].level,
                    (int) (originX + _LEVEL_X), (int) (_TABLE_Y - (_SPACING * i)),
                    _FNT_SIZE, Color.WHITE, GameAssets._BENZOIC_FONT
                );

            // The player score
            scoreLabels[i] = scene2DUtils.addLabel
                (
                    String.format(Locale.UK, "%8d", app.highScoreUtils.getHighScoreTable()[i].score),
                    (int) (originX + _SCORE_X), (int) (originY + _TABLE_Y - (_SPACING * i)),
                    _FNT_SIZE, Color.WHITE, GameAssets._BENZOIC_FONT
                );

            colorIndex[i] = i;

            if (i < _DISPLAYED_HISCORES)
            {
                app.stage.addActor(rankLabels[i]);
                app.stage.addActor(levelLabels[i]);
                app.stage.addActor(scoreLabels[i]);
            }
        }
    }

    /**
     * Show or Hide all menu items
     *
     * @param _visible visibility flag/
     */
    private void showItems(boolean _visible)
    {
        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (levelLabels != null)
        {
            for (Label label : levelLabels)
            {
                label.setVisible(_visible);
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.setVisible(_visible);
            }
        }
    }

    @Override
    public void dispose()
    {
        if (rankLabels != null)
        {
            for (Label label : rankLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (levelLabels != null)
        {
            for (Label label : levelLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        if (scoreLabels != null)
        {
            for (Label label : scoreLabels)
            {
                label.addAction(Actions.removeActor());
            }
        }

        colorIndex = null;
        rankLabels = null;
        levelLabels = null;
        scoreLabels = null;
        state = null;
    }

    private void addClickListeners()
    {
    }
}
