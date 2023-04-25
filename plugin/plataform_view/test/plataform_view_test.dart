import 'package:flutter_test/flutter_test.dart';
import 'package:plataform_view/plataform_view.dart';
import 'package:plataform_view/plataform_view_platform_interface.dart';
import 'package:plataform_view/plataform_view_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockPlataformViewPlatform
    with MockPlatformInterfaceMixin
    implements PlataformViewPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final PlataformViewPlatform initialPlatform = PlataformViewPlatform.instance;

  test('$MethodChannelPlataformView is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelPlataformView>());
  });

  test('getPlatformVersion', () async {
    PlataformView plataformViewPlugin = PlataformView();
    MockPlataformViewPlatform fakePlatform = MockPlataformViewPlatform();
    PlataformViewPlatform.instance = fakePlatform;

    expect(await plataformViewPlugin.getPlatformVersion(), '42');
  });
}
