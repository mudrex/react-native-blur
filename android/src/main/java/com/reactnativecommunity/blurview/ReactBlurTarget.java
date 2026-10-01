package com.reactnativecommunity.blurview;

import android.content.Context;
import androidx.annotation.NonNull;

import eightbitlab.com.blurview.BlurTarget;

/**
 * BlurTarget that lets RN's UIManager drive positioning of its children
 * instead of Android's normal ViewGroup layout pass, matching how RN's own
 * ReactViewGroup handles this.
 */
class ReactBlurTarget extends BlurTarget {

  ReactBlurTarget(@NonNull Context context) {
    super(context);
  }

  @Override
  public void requestLayout() {
    // ponytail: no-op, RN positions children directly via layout() — a
    // bubbled requestLayout() here would trigger a conflicting native pass.
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    // ponytail: no-op, FrameLayout's default onLayout repositions children
    // by gravity, which would fight the positions RN already applied.
  }
}
