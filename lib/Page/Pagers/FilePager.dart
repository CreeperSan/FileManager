import 'package:flutter/material.dart';
import 'package:file_manager/Collection/StackCollection.dart';
import 'package:file_manager/Page/Pagers/BasePager.dart';
import 'package:flutter/services.dart';

class FilePager extends BasePager{

  @override
  IconData getPagerIconWidget(bool isSelected) {
    return Icons.insert_drive_file;
  }

  @override
  String getPagerName() {
    return "文件";
  }

  @override
  State<StatefulWidget> createState() {
    return _FilePagerState();
  }

}

class _FilePagerState extends State<FilePager>{


  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Center(child: Text("文件"));
  }

}

/// 文件管理隊列
class FileStack{
  StackCollection<FolderItem> fileStack = StackCollection();

  String getPath(){
    return "/";
  }

}

/// 文件的列表
class FolderItem{
  String folderName = "";
  List<FileItem> _fileList = [];
}

/// 單個文件對象
class FileItem{
  bool isDirectory = false;
  String path = "";
  String name = "";
  int createTimeStamp = 0;
  int modifyTimeStamp = 0;
}
