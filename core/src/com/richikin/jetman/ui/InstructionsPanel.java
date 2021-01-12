package com.richikin.jetman.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.ui.DefaultPanel;

public class InstructionsPanel extends DefaultPanel
{
    private static final String _FILE_NAME = "data/howto.png";

    public InstructionsPanel()
    {
        super();
    }

    @Override
    public void setup()
    {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        buffer = new Table();
        buffer.top().left();
        buffer.pad(10, 10, 10, 10);
        buffer.setDebug(false);

        Texture sky   = App.assets.loadSingleAsset(_FILE_NAME, Texture.class);
        Image   image = new Image(new TextureRegion(sky));
        buffer.setBackground(image.getDrawable());

        populateTable();

        scrollPane = new ScrollPane(buffer, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setWidth(Gfx._HUD_WIDTH - 400);
        scrollPane.setHeight((float) Gfx._HUD_HEIGHT - 200);
        scrollPane.setPosition(AppConfig.hudOriginX + 200, AppConfig.hudOriginY + 100);

        App.stage.addActor(scrollPane);
    }

    @Override
    public boolean update()
    {
        return AppConfig.backButton.isChecked();
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        buffer.clear();
        buffer.addAction(Actions.removeActor());

        scrollPane.clear();
        scrollPane.addAction(Actions.removeActor());

        skin = null;
    }
}
