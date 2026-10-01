package com.reactnativecommunity.blurview;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

class BlurTargetManager extends ViewGroupManager<ReactBlurTarget> {

  public BlurTargetManager(ReactApplicationContext reactContext) {}

  @Override
  public ReactBlurTarget createViewInstance(ThemedReactContext context) {
    return BlurTargetManagerImpl.createViewInstance(context);
  }

  @NonNull
  @Override
  public String getName() {
    return BlurTargetManagerImpl.REACT_CLASS;
  }
}
