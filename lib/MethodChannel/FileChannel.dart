import 'dart:async';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:file_manager/MethodChannel/BaseChannel.dart';

class FileChannel extends BaseChannel{
  static const _CHANNEL_NAME = "com.creepersan.file_manager/FileChannel";

  static const _METHOD_GET_STORAGE_ROOT_PATH = "getStorageRootPath";
  static const _METHOD_CHECK_HAS_FILE_PERMISSION = "checkHasFilePermission";
  static const _METHOD_REQUEST_FILE_PERMISSION = "requestFilePermission";
  static const _METHOD_GET_DIRECTORY_FILE_LIST = "getDirectoryFileList";
  static const _METHOD_GET_FILE_DETAIL = "getFileDetail";
  static const _METHOD_COPY_FILE = "copyFile";
  static const _METHOD_CUT_FILE = "cutFile";
  static const _METHOD_DELETE_FILE = "deleteFile";
  static const _METHOD_RENAME_FILE = "renameFile";

  static MethodChannel _channel = MethodChannel(_CHANNEL_NAME);
  static FileChannel _instance;

  FileChannel._();

  static FileChannel getInstance(){
    if(_instance == null){
      _instance = FileChannel._();
    }
    return _instance;
  }

  Future<bool> checkHasFilePermission(){
    return _channel.invokeMethod(_METHOD_CHECK_HAS_FILE_PERMISSION);
  }

  Future<String> getStorageRootPath(){
    return _channel.invokeMethod(_METHOD_GET_STORAGE_ROOT_PATH);
  }

  getFileDetail(String path) async {
    Map<String, Object> fileItem = Map();
    String content = await _channel.invokeMethod(_METHOD_GET_FILE_DETAIL, path);
    Map<String, dynamic> jsonObj = json.decode(content);
    return jsonObj;
  }




}
