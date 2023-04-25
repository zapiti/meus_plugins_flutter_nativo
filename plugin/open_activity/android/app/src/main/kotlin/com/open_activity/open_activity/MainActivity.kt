package com.open_activity.open_activity


import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.open_activity.open_activity.view.java.com.example.navigationapidemo.NavViewActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StandardMessageCodec
import java.io.InputStream
import java.util.*


class MainActivity : FlutterActivity() {
    val idMaps: Int = 5
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor, "flutter_demo/method_channel")
            .setMethodCallHandler { call, result ->
                val count: Int? = call.argument<Int>("count")
                val lat: Double? = call.argument<Double>("lat")
                val long: Double? = call.argument<Double>("long")
                when (call.method) {
                    "increment" -> inclement(result, count)
                    "decrement" -> decrement(result, count)
                    "back" -> back()
                    "start_location" -> result.success(openMaps(lat, long))
                    "update_location" -> result.success(updateMaps(lat, long))
                    else -> result.notImplemented()
                }
            }

        val sensorManger: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometerSensor: Sensor = sensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        EventChannel(flutterEngine.dartExecutor, "flutter_demo/event_channel")
            .setStreamHandler(AccelerometerStreamHandler(sensorManger, accelerometerSensor))

        BasicMessageChannel(
            flutterEngine.dartExecutor,
            "flutter_demo/basic_message_channel",
            StandardMessageCodec()
        )
            .setMessageHandler { message, reply ->
                val inputStream: InputStream = assets.open(message as String)
                reply.reply(inputStream.readBytes())
            }

//        // hybrid view
//        flutterEngine.platformViewsController.registry.registerViewFactory(
//            "<platform-hybrid-view>",
//            HybridViewFactor()
//        )
//        flutterEngine.platformViewsController.registry.registerViewFactory(
//            "<platform-virtual-view>",
//            VirtualViewFactor()
//        )
    }

    private fun decrement(result: MethodChannel.Result, count: Int?) {
        if (count == null) {
            result.error("INVALID ARGUMENT", "Value of count cannot be null", null)
        } else {
            result.success(count - 1)
        }
    }

    private fun inclement(result: MethodChannel.Result, count: Int?) {
        if (count == null) {
            result.error("INVALID ARGUMENT", "Value of count cannot be null", null)
        } else {
            result.success(count + 1)
        }
    }

    private fun back() {
        finishActivity(idMaps)
    }

    private fun openMaps(lat: Double?, long: Double?): Boolean {
        val i = Intent(context, NavViewActivity::class.java)
        i.putExtra("lat", lat)
        i.putExtra("long", long)
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(i, idMaps)
        // Add a marker in Sydney and move the camera
        return true
    }

    private fun updateMaps(lat: Double?, long: Double?): Boolean {

        if (lat != null && long != null) {
            // Add a marker in Sydney and move the camera
            val locale = LatLng(lat, long)
            NavViewActivity.navigateToLatLong(locale.latitude, locale.longitude, ({
                back()
                Log.d("callbackFinish", "callbackFinish")
            }))
        }
        return true
    }


}
