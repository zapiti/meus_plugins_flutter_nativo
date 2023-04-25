
import 'plataform_view_platform_interface.dart';

class PlataformView {
  Future<String?> getPlatformVersion() {
    return PlataformViewPlatform.instance.getPlatformVersion();
  }
}
