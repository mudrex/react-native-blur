package com.reactnativecommunity.blurview;

import com.facebook.react.uimanager.ThemedReactContext;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
class BlurTargetManagerImpl {

  public static final String REACT_CLASS = "AndroidBlurTarget";

  public static @Nonnull ReactBlurTarget createViewInstance(@Nonnull ThemedReactContext ctx) {
    return new ReactBlurTarget(ctx);
  }
}
