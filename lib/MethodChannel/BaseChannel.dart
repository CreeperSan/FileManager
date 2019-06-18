import 'dart:async';
import 'package:flutter/services.dart';

abstract class BaseChannel{
  MethodChannel channel;

  _BaseChannel(){
    channel = MethodChannel(getChannelName());
  }

  String getChannelName();



}