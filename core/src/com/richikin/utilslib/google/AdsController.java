
package com.richikin.utilslib.google;

@SuppressWarnings({"SameReturnValue", "unused"})
public interface AdsController
{
    void showBannerAd();

    void hideBannerAd();

    void showInterstitialAd(Runnable runnable);

    boolean isWifiConnected();
}
