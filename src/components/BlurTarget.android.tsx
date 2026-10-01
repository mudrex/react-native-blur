import React, { forwardRef } from 'react';
import type { View, ViewProps } from 'react-native';
import NativeBlurTarget from '../fabric/BlurTargetNativeComponentAndroid';

export type BlurTargetProps = ViewProps;

const BlurTarget = forwardRef<View, BlurTargetProps>((props, ref) => (
  <NativeBlurTarget {...props} ref={ref} />
));

export default BlurTarget;
