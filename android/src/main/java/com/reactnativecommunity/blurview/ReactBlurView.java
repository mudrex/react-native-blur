package com.reactnativecommunity.blurview;

import android.content.Context;
import androidx.annotation.NonNull;

import eightbitlab.com.blurview.BlurView;

/**
 * BlurView subclass that caches prop values applied before setupWith() has
 * run (i.e. before the targetId prop resolves to a mounted BlurTarget), so
 * they can be replayed once setup completes.
 */
class ReactBlurView extends BlurView {
  int pendingRadius = BlurViewManagerImpl.defaultRadius;
  int pendingColor = 0;
  boolean pendingEnabled = true;
  boolean pendingAutoUpdate = true;

  ReactBlurView(@NonNull Context context) {
    super(context);
  }
}
