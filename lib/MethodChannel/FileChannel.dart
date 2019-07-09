import 'dart:async';
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

  static MethodChannel _instance;

  FileChannel._();

  static getInstance(){
    if(_instance == null){
      _instance = MethodChannel(_CHANNEL_NAME);
    }
  }

  Future<bool> checkHasFilePermission() async {
    return _instance.invokeMethod(_METHOD_CHECK_HAS_FILE_PERMISSION);
  }

}
