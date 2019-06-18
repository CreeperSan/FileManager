import 'dart:async';
import 'package:flutter/services.dart';
import 'package:file_manager/MethodChannel/BaseChannel.dart';

class FileChannel extends BaseChannel{

  Future<String> getExternalDirectory() async {
    final result = await channel.invokeMethod("getExternalStoragePath");
    return result.data;
  }

  @override
  String getChannelName() {
    return "com.creepersan.com.method_channel.file";
  }

}
