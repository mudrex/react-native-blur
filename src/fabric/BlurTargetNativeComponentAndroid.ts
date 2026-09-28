import codegenNativeComponent from 'react-native/Libraries/Utilities/codegenNativeComponent';
import type { ViewProps, HostComponent } from 'react-native';

interface NativeProps extends ViewProps {}

export default codegenNativeComponent<NativeProps>('AndroidBlurTarget', {
  excludedPlatforms: ['iOS'],
}) as HostComponent<NativeProps>;
