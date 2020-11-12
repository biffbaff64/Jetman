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
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.AppSystem;
import com.richikin.utilslib.graphics.text.FontUtils;
import com.richikin.utilslib.logging.Meters;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.ui.DefaultPanel;
import com.richikin.utilslib.ui.IUserInterfacePanel;

import java.io.BufferedReader;
import java.io.IOException;

public class PrivacyPolicyPanel extends DefaultPanel implements IUserInterfacePanel
{
    private final String _FILE_NAME = "documents/privacy_policy.txt";
//    private final String _BACKGROUND    = "data/empty_screen.png";

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

//        texture = app.assets.loadSingleAsset(_BACKGROUND, Texture.class);

        createTitle();

        populateTable();

        scrollPane = new ScrollPane(buffer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._VIEW_WIDTH - 80);
        scrollPane.setHeight((float) (Gfx._VIEW_HEIGHT / 8) * 6);
        scrollPane.setPosition(AppSystem.hudOriginX + 40, AppSystem.hudOriginY + 40);

        App.stage.addActor(scrollPane);
        App.stage.addActor(title);
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

//        app.assets.unloadAsset(_BACKGROUND);

        buffer.addAction(Actions.removeActor());
        scrollPane.addAction(Actions.removeActor());
        title.addAction(Actions.removeActor());

        title = null;
        skin  = null;
//        texture = null;
    }

    private void createTitle()
    {
        TextureRegion         region   = App.assets.getTextRegion("title_small");
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        title = new Image(drawable);
        title.setPosition(AppSystem.hudOriginX + 351, AppSystem.hudOriginY + (720 - 159));
    }

    @Override
    public boolean update()
    {
        return false;
    }
}
