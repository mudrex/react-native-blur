package com.reactnativecommunity.blurview;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

@ReactModule(name = BlurTargetManagerImpl.REACT_CLASS)
class BlurTargetManager extends ViewGroupManager<ReactBlurTarget> {

  public BlurTargetManager(ReactApplicationContext context) {}

  @NonNull
  @Override
  public String getName() {
    return BlurTargetManagerImpl.REACT_CLASS;
  }

  @NonNull
  @Override
  protected ReactBlurTarget createViewInstance(@NonNull ThemedReactContext context) {
    return BlurTargetManagerImpl.createViewInstance(context);
  }
}
