package com.creepersan.file_manager

import android.os.Bundle
import com.creepersan.file_manager.bridge.FileChannel

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity: FlutterActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)

    MethodChannel(flutterView, FileChannel.CHANNEL_NAME).setMethodCallHandler(FileChannel(this))

  }
}
