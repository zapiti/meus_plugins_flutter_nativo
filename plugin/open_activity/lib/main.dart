import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
        title: 'Flutter Demo',
        home: Scaffold(
            appBar: AppBar(
              title: const Text('Welcome to Flutter'),
            ),
            body: MethodChannelPage()));
  }
}

class MethodChannelPage extends StatefulWidget {
  static const String rName = "MethodChannel";

  const MethodChannelPage({Key? key}) : super(key: key);

  @override
  _MethodChannelPageState createState() => _MethodChannelPageState();
}

class _MethodChannelPageState extends State<MethodChannelPage> {
  int count = 0;

  @override
  void initState() {
    super.initState();
    FlutterMethodChannel.methodChannel.setMethodCallHandler(decrement);
  }

  Future<dynamic> decrement(MethodCall call) async {
    // Log.info("Android arguments: ${call.arguments}", StackTrace.current);
    setState(() {
      count = call.arguments;
    });
    return Future.value(true);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Text(
          '$count',
          style: const TextStyle(fontSize: 40),
        ),
      ),
      floatingActionButton: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          FloatingActionButton(
            heroTag: null,
            child: const Icon(Icons.edit_location_rounded),
            onPressed: () async {
              FlutterMethodChannel.startLocation(-18.895630, -48.278660);
              Future.delayed(Duration(seconds: 10), () {
                print('entro aqui ===============>');
                FlutterMethodChannel.updateLocation(-18.895630, -48.278660);

                Future.delayed(Duration(seconds: 10), () {
                  FlutterMethodChannel.updateLocation(-18.892720, -48.282060);
                });
              });
            },
          ),
          const SizedBox(height: 10),
          FloatingActionButton(
            heroTag: null,
            child: const Icon(Icons.add),
            onPressed: () async {
              int? _count = await FlutterMethodChannel.increment(count);
              if (_count != null) {
                setState(() {
                  count = _count;
                });
              }
            },
          ),
          const SizedBox(height: 10),
          FloatingActionButton(
            heroTag: null,
            child: const Icon(Icons.remove),
            onPressed: () async {
              ///Flutter 调用 原生 代码
              int? _count = await FlutterMethodChannel.decrement(count);
              if (_count != null) {
                setState(() {
                  count = _count;
                });
              }
            },
          ),
        ],
      ),
    );
  }
}

class FlutterMethodChannel {
  static const MethodChannel methodChannel =
      MethodChannel('flutter_demo/method_channel');

  static Future<int?> increment(int value) async {
    final result =
        await methodChannel.invokeMethod<int>('increment', {'count': value});
    return result;
  }

  static Future<int?> decrement(int value) async {
    final result =
        await methodChannel.invokeMethod<int>('decrement', {'count': value});
    return result;
  }

  static Future<bool?> startLocation(double lat, double long) async {
    final result = await methodChannel
        .invokeMethod<bool>('start_location', {'lat': lat, 'long': long});
    return result;
  }

  static Future<bool?> updateLocation(double lat, double long) async {
    final result = await methodChannel
        .invokeMethod<bool>('update_location', {'lat': lat, 'long': long});
    return result;
  }

  static void back() async {
    methodChannel.invokeMethod<void>('back', {});
  }
}
