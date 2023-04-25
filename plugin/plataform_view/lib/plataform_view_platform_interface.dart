import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'plataform_view_method_channel.dart';

abstract class PlataformViewPlatform extends PlatformInterface {
  /// Constructs a PlataformViewPlatform.
  PlataformViewPlatform() : super(token: _token);

  static final Object _token = Object();

  static PlataformViewPlatform _instance = MethodChannelPlataformView();

  /// The default instance of [PlataformViewPlatform] to use.
  ///
  /// Defaults to [MethodChannelPlataformView].
  static PlataformViewPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [PlataformViewPlatform] when
  /// they register themselves.
  static set instance(PlataformViewPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
