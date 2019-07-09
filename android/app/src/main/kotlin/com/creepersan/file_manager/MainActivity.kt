package com.creepersan.file_manager

import android.os.Bundle

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {

  companion object{
    const val CHANNEL = "samples.flutter.io/battery"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

    MethodChannel(flutterView, CHANNEL).setMethodCallHandler { methodCall, result ->
      when(methodCall.method){
        "getApplicationList" -> {
          result.success("{\"name\":\"1023541asd\"}")
        }
      }
    }
  }
}
