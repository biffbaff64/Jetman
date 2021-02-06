
package com.richikin.utilslib.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IAssets
{
    <T> T loadSingleAsset(String asset, Class<?> type);

    void loadAtlas(String atlasName);

    void unloadAsset(String asset);

    TextureRegion getButtonRegion(String _name);

    TextureRegion getAnimationRegion(String _name);

    TextureRegion getObjectRegion(String _name);

    TextureRegion getTextRegion(String _name);

    TextureRegion getAchievementRegion(final String _name);

    AssetManager getAssetManager();

    TextureAtlas getButtonsLoader();

    TextureAtlas getAnimationsLoader();

    TextureAtlas getObjectsLoader();

    TextureAtlas getTextsLoader();

    TextureAtlas getAchievementsLoader();
}
