package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.text.FontUtils;
import com.richikin.utilslib.logging.Meters;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.DefaultPanel;

public class StatsPanel extends DefaultPanel
{
    private static class StatsInfo
    {
        final String name;
        final Meters meter;

        StatsInfo(String _name, Meters _meter)
        {
            this.name = _name;
            this.meter = _meter;
        }
    }

    private static final String _SCROLL_PANE_NAME  = "statsScrollPane";
    private static final String _BUFFER_NAME       = "statsBuffer";

    private Image title;

    private final StatsInfo[] meterNames =
        {
            new StatsInfo("  Illegal Game Mode            ", Meters._ILLEGAL_GAME_MODE),
            new StatsInfo("  Sound Load Failure           ", Meters._SOUND_LOAD_FAIL),
            new StatsInfo("  Bad Player Action            ", Meters._BAD_PLAYER_ACTION),
            new StatsInfo("  Font Load Failure            ", Meters._FONT_LOAD_FAILURE),
            new StatsInfo("  Bordered Font Load Failure   ", Meters._BORDERED_FONT_LOAD_FAILURE),

            new StatsInfo("divider", null),

            new StatsInfo("  I/O Exception                ", Meters._IO_EXCEPTION),
            new StatsInfo("  Index Out Of Bounds Exception", Meters._INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  Array Index O.O.B Exception  ", Meters._ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION),
            new StatsInfo("  SAX Exception                ", Meters._SAX_EXCEPTION),
            new StatsInfo("  Interrupted Exception        ", Meters._INTERRUPTED_EXCEPTION),
            new StatsInfo("  Null Pointer Exception       ", Meters._NULL_POINTER_EXCEPTION),
            new StatsInfo("  Illegal State Exception      ", Meters._ILLEGAL_STATE_EXCEPTION),
            new StatsInfo("  GDX Runtime Exception        ", Meters._GDX_RUNTIME_EXCEPTION),
            new StatsInfo("  Unknown Exception            ", Meters._UNKNOWN_EXCEPTION),
            new StatsInfo("  Should Always be ZERO >>>>   ", Meters._DUMMY_METER),
            new StatsInfo("  Entity Data Exception        ", Meters._ENTITY_DATA_EXCEPTION),

            new StatsInfo("divider", null),
            };

    public StatsPanel()
    {
        super();
    }

    @Override
    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.setName(_BUFFER_NAME);
        buffer.top().left();
        buffer.pad(30, 30, 30, 30);
        buffer.setDebug(false);

        Texture sky   = App.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image   image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

        Scene2DUtils scene2DUtils = new Scene2DUtils();

        title = scene2DUtils.createImage("title_small", App.assets.getTextsLoader());
        title.setPosition(AppConfig.hudOriginX + 350, AppConfig.hudOriginY + (720 - 160));

        populateTable(buffer, skin);

        // Wrap the buffer in a scrollpane.
        scrollPane = scene2DUtils.createScrollPane(buffer, skin, _SCROLL_PANE_NAME);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 400);
        scrollPane.setHeight((float) Gfx._HUD_HEIGHT - 200);
        scrollPane.setPosition(AppConfig.hudOriginX + 200, AppConfig.hudOriginY + 100);

        App.stage.addActor(scrollPane);
        App.stage.addActor(title);
    }

    /**
     * Populate table.
     */
    @Override
    public void populateTable()
    {
        populateTable(buffer, skin);
    }

    @Override
    public boolean update()
    {
        return AppConfig.backButton.isChecked();
    }

    /**
     * Populate table.
     *
     * @param table the table
     * @param skin  the skin
     */
    private void populateTable(Table table, Skin skin)
    {
        try
        {
            TextField.TextFieldStyle textFieldStyle;

            for (StatsInfo meterName : meterNames)
            {
                if ("divider".equals(meterName.name))
                {
                    TextureRegion         region   = App.assets.getObjectRegion("divider");
                    TextureRegionDrawable drawable = new TextureRegionDrawable(region);
                    Image                 image    = new Image(drawable);

                    table.add(image).padLeft(200);
                    table.row();
                }
                else
                {
                    FontUtils fontUtils = new FontUtils();

                    TextField label = new TextField(meterName.name, skin);
                    textFieldStyle = new TextField.TextFieldStyle(label.getStyle());
                    textFieldStyle.font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 16, Color.WHITE);
                    textFieldStyle.fontColor = Color.WHITE;

                    label.setStyle(textFieldStyle);
                    label.setAlignment(Align.left);
                    label.setDisabled(true);

                    String meterString = Integer.toString(Stats.getMeter(meterName.meter.get()));

//                    if (meterName.isOutOf)
//                    {
//                        meterString = meterString.concat(" / ");
//
//                        int outOf;
//
//                        switch (meterName.special)
//                        {
//                            default:
//                            {
//                                outOf = 0;
//                            }
//                            break;
//                        }
//
//                        meterString = meterString.concat(Integer.toString(outOf));
//                    }

                    TextField meterLabel = new TextField(meterString, skin);
                    textFieldStyle = new TextField.TextFieldStyle(meterLabel.getStyle());
                    textFieldStyle.font = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 16, Color.YELLOW);
                    textFieldStyle.fontColor = Color.YELLOW;

                    meterLabel.setStyle(textFieldStyle);
                    meterLabel.setAlignment(Align.center);
                    meterLabel.setDisabled(true);

                    float prefHeight = label.getPrefHeight() * 1.5f;

                    table.add(label).padLeft(40).padBottom(10).prefWidth((float) Gfx._VIEW_WIDTH / 3).prefHeight(prefHeight);
                    table.add(meterLabel).padLeft(40).padBottom(10).prefWidth((float) Gfx._VIEW_WIDTH / 4).prefHeight(prefHeight);
                }

                table.row();
            }

            table.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        buffer.clear();
        scrollPane.clear();

        App.assets.unloadAsset("data/stats_screen.png");

        title.addAction(Actions.removeActor());
        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());

        title = null;
        skin = null;
    }
}
