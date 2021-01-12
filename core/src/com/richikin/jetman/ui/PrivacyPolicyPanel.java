package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

import java.io.BufferedReader;
import java.io.IOException;

public class PrivacyPolicyPanel extends DefaultPanel implements IUserInterfacePanel
{
    private static final String _FILE_NAME = "documents/privacy_policy.txt";

    private Image title;

    public PrivacyPolicyPanel()
    {
        super();
    }

    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.top().left();
        buffer.pad(30, 30, 30, 30);
        buffer.setDebug(false);

        Texture sky   = App.assets.loadSingleAsset("data/night_sky.png", Texture.class);
        Image   image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

        createTitle();
        populateTable();

        scrollPane = new ScrollPane(buffer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 400);
        scrollPane.setHeight((float) Gfx._HUD_HEIGHT - 200);
        scrollPane.setPosition(AppConfig.hudOriginX + 200, AppConfig.hudOriginY + 100);

        App.stage.addActor(scrollPane);
        App.stage.addActor(title);
    }

    @Override
    public boolean update()
    {
        return AppConfig.backButton.isChecked();
    }

    @Override
    public void populateTable()
    {
        try
        {
            FileHandle     file   = Gdx.files.internal(_FILE_NAME);
            BufferedReader reader = new BufferedReader(file.reader());
            String         string;
            Label          label;
            BitmapFont     bitmapFont;

            FontUtils fontUtils = new FontUtils();
            bitmapFont = fontUtils.createFont(GameAssets._PRO_WINDOWS_FONT, 18);

            while ((string = reader.readLine()) != null)
            {
                label = new Label(string, skin);
                label.setAlignment(Align.left);
                label.setWrap(true);

                Label.LabelStyle labelStyle = new Label.LabelStyle();
                labelStyle.font = bitmapFont;
                labelStyle.font.setColor(Color.WHITE);
                label.setStyle(labelStyle);

                buffer.add(label).align(Align.left);
                buffer.row();
            }

            buffer.setVisible(true);
        }
        catch (NullPointerException npe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._NULL_POINTER_EXCEPTION.get());
        }
        catch (IOException ioe)
        {
            Trace.__FILE_FUNC_LINE();
            Stats.incMeter(Meters._IO_EXCEPTION.get());
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

        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
        title.addAction(Actions.removeActor());

        title = null;
        skin  = null;
    }

    private void createTitle()
    {
        TextureRegion         region   = App.assets.getTextRegion("title_small");
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        title = new Image(drawable);
        title.setPosition(AppConfig.hudOriginX + 351, AppConfig.hudOriginY + (720 - 159));
    }
}
