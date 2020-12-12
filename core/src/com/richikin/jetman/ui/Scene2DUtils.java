
package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.text.FontUtils;

import org.jetbrains.annotations.NotNull;

public class Scene2DUtils
{
    public Scene2DUtils()
    {
    }

    public @NotNull Table createTable(int x, int y, int width, int height, Skin skin)
    {
        Table table = new Table(skin);
        table.setSize(width, height);
        table.setPosition(x, y);

        return table;
    }

    public @NotNull Image createImage(String imageName, @NotNull TextureAtlas atlasLoader)
    {
        TextureRegion         region   = atlasLoader.findRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }

    public @NotNull Drawable createDrawable(String imageName, @NotNull TextureAtlas atlasLoader)
    {
        TextureRegion region = atlasLoader.findRegion(imageName);

        return new TextureRegionDrawable(region);
    }

    public @NotNull ScrollPane createScrollPane(Table table, Skin skin, String name)
    {
        ScrollPane scrollPane = new ScrollPane(table, skin);

        scrollPane.setName(name);

        return scrollPane;
    }

    // FIXME: 12/12/2020 - Too many parameters
    public @NotNull Label addLabel(String labelText, int x, int y, int size, Color color, String fontName)
    {
        FontUtils fontUtils = new FontUtils();

        Label.LabelStyle label1Style = new Label.LabelStyle();
        label1Style.font      = fontUtils.createFont(fontName, size, Color.WHITE);
        label1Style.fontColor = color;

        Label label = new Label(labelText, label1Style);
        label.setStyle(label1Style);
        label.setAlignment(Align.center);
        label.setPosition(x, y);

        return label;
    }

    public @NotNull Label addLabel(String labelText, int x, int y, Color color, Skin skin)
    {
        Label label = makeLabel(labelText, x, y, color, skin);

        App.stage.addActor(label);

        return label;
    }

    public @NotNull ImageButton addButton(String upButton, String downButton, int x, int y)
    {
        Image       imageUp     = new Image(App.assets.getButtonRegion(upButton));
        Image       imageDown   = new Image(App.assets.getButtonRegion(downButton));
        ImageButton imageButton = new ImageButton(imageUp.getDrawable(), imageDown.getDrawable());

        imageButton.setPosition(x, y);
        imageButton.setVisible(true);
        imageButton.setZIndex(1);

        App.stage.addActor(imageButton);

        return imageButton;
    }

    // FIXME: 12/12/2020 - Too many parameters
    public @NotNull CheckBox addCheckBox(String imageOn, String imageOff, int x, int y, Color color, Skin skin)
    {
        CheckBox checkBox = makeCheckBox(imageOn, imageOff, x, y, color, skin);

        App.stage.addActor(checkBox);

        return checkBox;
    }

    public @NotNull Label makeLabel(String string, int x, int y, Color color, Skin skin)
    {
        Label            label = new Label(string, skin);
        Label.LabelStyle style = label.getStyle();
        style.fontColor = color;

        label.setStyle(style);
        label.setAlignment(Align.center);

        label.setPosition(x, y);

        return label;
    }

    public @NotNull Slider makeSlider(int x, int y, Skin skin)
    {
        Slider             slider = new Slider(0, 10, 1, false, skin);
        Slider.SliderStyle style  = slider.getStyle();

        slider.setPosition(x, y);
        slider.setSize(280, 30);

        return slider;
    }

    // FIXME: 12/12/2020 - Too many parameters
    public @NotNull CheckBox makeCheckBox(String imageOn, String imageOff, int x, int y, Color color, Skin skin)
    {
        TextureRegion regionOn  = App.assets.getButtonRegion(imageOn);
        TextureRegion regionOff = App.assets.getButtonRegion(imageOff);

        CheckBox               checkBox = new CheckBox("", skin);
        CheckBox.CheckBoxStyle style    = checkBox.getStyle();

        style.fontColor   = color;
        style.checkboxOn  = new TextureRegionDrawable(regionOn);
        style.checkboxOff = new TextureRegionDrawable(regionOff);

        checkBox.setSize(regionOn.getRegionWidth(), regionOn.getRegionHeight());
        checkBox.setStyle(style);
        checkBox.setPosition(x, y);

        return checkBox;
    }

    // TODO: 12/12/2020 - Only used in MenuPage, move there instead.
    public @NotNull Image makeObjectsImage(String imageName)
    {
        TextureRegion         region   = App.assets.getObjectRegion(imageName);
        TextureRegionDrawable drawable = new TextureRegionDrawable(region);

        return new Image(drawable);
    }
}
