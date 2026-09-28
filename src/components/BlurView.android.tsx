import React, { forwardRef, useEffect, useState } from 'react';
import {
  View,
  DeviceEventEmitter,
  findNodeHandle,
  StyleSheet,
  ViewProps,
  ViewStyle,
} from 'react-native';
import NativeBlurView from '../fabric/BlurViewNativeComponentAndroid';

const OVERLAY_COLORS = {
  light: 'rgba(255, 255, 255, 0.2)',
  xlight: 'rgba(255, 255, 255, 0.75)',
  dark: 'rgba(16, 12, 12, 0.64)',
};

export type BlurViewProps = ViewProps & {
  // Android needs the content to blur wrapped in a <BlurTarget>; pass a ref
  // to it here, or blurring silently stays off. See the README's Android
  // migration note.
  target?: React.RefObject<View>;
  blurAmount?: number;
  blurType?: 'dark' | 'light' | 'xlight';
  blurRadius?: number;
  downsampleFactor?: number;
  overlayColor?: string;
  enabled?: boolean;
  autoUpdate?: boolean;
};

const BlurView = forwardRef<View, BlurViewProps>(
  (
    {
      target,
      downsampleFactor,
      blurRadius,
      blurAmount = 10,
      blurType = 'dark',
      overlayColor,
      enabled,
      autoUpdate,
      children,
      style,
      ...rest
    },
    ref
  ) => {
    const [targetId, setTargetId] = useState<number | undefined>(undefined);

    useEffect(() => {
      const id = target?.current ? findNodeHandle(target.current) : null;
      setTargetId(id ?? undefined);
      if (__DEV__ && id == null) {
        console.warn(
          '[ReactNativeBlur] BlurView requires a "target" prop referencing a mounted <BlurTarget> on Android.'
        );
      }
    }, [target]);

    useEffect(() => {
      DeviceEventEmitter.addListener('ReactNativeBlurError', (message) => {
        throw new Error(`[ReactNativeBlur]: ${message}`);
      });

      return () => {
        DeviceEventEmitter.removeAllListeners('ReactNativeBlurError');
      };
    }, []);

    const getOverlayColor = () => {
      if (overlayColor != null) {
        return overlayColor;
      }

      return OVERLAY_COLORS[blurType] || OVERLAY_COLORS.dark;
    };

    const getBlurRadius = () => {
      if (blurRadius != null) {
        if (blurRadius > 25) {
          throw new Error(
            `[ReactNativeBlur]: blurRadius cannot be greater than 25! (was: ${blurRadius})`
          );
        }
        return blurRadius;
      }

      // iOS seems to use a slightly different blurring algorithm (or scale?).
      // Android blurRadius + downsampleFactor is approximately 80% of blurAmount.
      const equivalentBlurRadius = Math.round(blurAmount * 0.8);

      if (equivalentBlurRadius > 25) {
        return 25;
      }
      return equivalentBlurRadius;
    };

    const getDownsampleFactor = () => {
      if (downsampleFactor != null) {
        return downsampleFactor;
      }

      return blurRadius;
    };

    return (
      <NativeBlurView
        {...rest}
        ref={ref}
        blurRadius={getBlurRadius()}
        downsampleFactor={getDownsampleFactor()}
        overlayColor={getOverlayColor()}
        blurAmount={blurAmount}
        blurType={blurType}
        enabled={enabled}
        autoUpdate={autoUpdate}
        targetId={targetId}
        pointerEvents="none"
        style={StyleSheet.compose(styles.transparent, style)}
      >
        {children}
      </NativeBlurView>
    );
  }
);

const styles = StyleSheet.create<{ transparent: ViewStyle }>({
  transparent: { backgroundColor: 'transparent' },
});

export default BlurView;
