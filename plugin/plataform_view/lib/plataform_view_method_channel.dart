import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'plataform_view_platform_interface.dart';

/// An implementation of [PlataformViewPlatform] that uses method channels.
class MethodChannelPlataformView extends PlataformViewPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('plataform_view');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
