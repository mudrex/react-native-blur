package com.reactnativecommunity.blurview;

import android.view.View;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.UIManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerHelper;

import eightbitlab.com.blurview.BlurView;

import java.util.Objects;
import javax.annotation.Nonnull;

@SuppressWarnings("unused")
class BlurViewManagerImpl {

  public static final String REACT_CLASS = "AndroidBlurView";

  public static final int defaultRadius = 10;
  public static final int defaultSampling = 10;

  // ponytail: bounded retry, not an event-driven mount listener — a few
  // attempts across posted frames comfortably covers normal RN mount
  // ordering. Switch to a UIManager mount listener if that ever proves
  // insufficient for a real late-mounting target.
  private static final int MAX_TARGET_RESOLVE_ATTEMPTS = 5;

  public static @Nonnull ReactBlurView createViewInstance(@Nonnull ThemedReactContext ctx) {
    // BlurView 3.x requires an explicit BlurTarget in the hierarchy — it can
    // no longer auto-attach to the Activity's decor content. setupWith() is
    // deferred until the targetId prop resolves (see setTargetId below).
    return new ReactBlurView(ctx);
  }

  public static void setRadius(BlurView view, int radius) {
    ((ReactBlurView) view).pendingRadius = radius;
    view.setBlurRadius(radius);
    view.invalidate();
  }

  public static void setColor(BlurView view, int color) {
    ((ReactBlurView) view).pendingColor = color;
    view.setOverlayColor(color);
    view.invalidate();
  }

  public static void setDownsampleFactor(BlurView view, int factor) {}

  public static void setAutoUpdate(BlurView view, boolean autoUpdate) {
    ((ReactBlurView) view).pendingAutoUpdate = autoUpdate;
    view.setBlurAutoUpdate(autoUpdate);
    view.invalidate();
  }

  public static void setBlurEnabled(BlurView view, boolean enabled) {
    ((ReactBlurView) view).pendingEnabled = enabled;
    view.setBlurEnabled(enabled);
  }

  public static void setTargetId(@Nonnull BlurView view, @Nonnull ThemedReactContext context, int targetId) {
    if (targetId < 0) return; // unset on the JS side
    resolveTarget((ReactBlurView) view, context, targetId, MAX_TARGET_RESOLVE_ATTEMPTS);
  }

  private static void resolveTarget(
      @Nonnull ReactBlurView view, @Nonnull ReactContext context, int targetId, int attemptsLeft) {
    UIManager uiManager = UIManagerHelper.getUIManager(context, targetId);
    View target = uiManager != null ? uiManager.resolveView(targetId) : null;

    if (target instanceof ReactBlurTarget) {
      setupBlur(view, (ReactBlurTarget) target);
    } else if (attemptsLeft > 0) {
      // Target may not be mounted yet (conditional/async render on JS side).
      view.post(() -> resolveTarget(view, context, targetId, attemptsLeft - 1));
    }
    // else: give up silently — bad/removed targetId, view stays inert.
  }

  private static void setupBlur(@Nonnull ReactBlurView view, @Nonnull ReactBlurTarget target) {
    View decorView = Objects.requireNonNull(
      ((ThemedReactContext) view.getContext()).getCurrentActivity()
    ).getWindow().getDecorView();

    // applyNoise=false: skip the library's blue-noise dithering overlay, which
    // reads as visible grain on real devices.
    view.setupWith(target, 4f, false)
      .setFrameClearDrawable(decorView.getBackground())
      .setBlurRadius(view.pendingRadius)
      .setOverlayColor(view.pendingColor);
    view.setBlurEnabled(view.pendingEnabled);
    view.setBlurAutoUpdate(view.pendingAutoUpdate);
    view.invalidate();
  }
}
