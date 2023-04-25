import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:plataform_view/plataform_view_method_channel.dart';

void main() {
  MethodChannelPlataformView platform = MethodChannelPlataformView();
  const MethodChannel channel = MethodChannel('plataform_view');

  TestWidgetsFlutterBinding.ensureInitialized();

  setUp(() {
    channel.setMockMethodCallHandler((MethodCall methodCall) async {
      return '42';
    });
  });

  tearDown(() {
    channel.setMockMethodCallHandler(null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
