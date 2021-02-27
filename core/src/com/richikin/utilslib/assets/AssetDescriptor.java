package com.richikin.utilslib.assets;

public class AssetDescriptor
{
    public String    assetName;
    public int       frameCount;
    public AssetSize assetSize;

    public AssetDescriptor(String _name, int _frames, AssetSize _size)
    {
        this.assetName  = _name;
        this.frameCount = _frames;
        this.assetSize  = _size;
    }
}
