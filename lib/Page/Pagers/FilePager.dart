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
    return Stack(children: <Widget>[
      Container(child: Column(children: <Widget>[
        Container(height: 32, decoration: BoxDecoration(color: Colors.white),child: Row(children: <Widget>[
          Expanded(child: ListView(scrollDirection: Axis.horizontal, children: <Widget>[
            Icon(Icons.chevron_right),
            _getFilePathWidget("emulated"),
            _getFilePathWidget("storage"),
            _getFilePathWidget("0"),
            _getFilePathWidget("tmp"),
            _getFilePathWidget(".Data"),
          ]))
        ])),
        Divider(height: 1, color: Colors.blueGrey),
        Expanded(child: ListView(children: <Widget>[
          Column(mainAxisAlignment: MainAxisAlignment.center, children: <Widget>[
            _getFileItemWidget("12.txt", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("13.txt", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("14.mp4", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("15.sh", "26.01 kb", "-we", "01/05/19"),
            _getFileItemWidget("16.java", "26.01 kb", "-we", "01/05/19"),
          ])
        ])),
        Divider(height: 1, color: Colors.blueGrey),
        Row(children: <Widget>[
          _getBottomButtom("選擇", Icons.select_all),
          _getBottomButtom("新建", Icons.add),
          _getBottomButtom("搜索", Icons.search),
          _getBottomButtom("刷新", Icons.refresh),
          _getBottomButtom("更多", Icons.more_horiz),
        ])
      ])),
    ]);
  }

  /// 點擊事件
  void _onMorePress(){
    print("onMenuPressed()");
  }

  Widget _getFileItemWidget(String fileName, String fileSize, String permission, String time){
    return Row(children: <Widget>[
      Container(width: 48, height: 48, child: Icon(Icons.insert_drive_file)),
      Expanded(child: Column(children: <Widget>[
        Row(children: <Widget>[
          Text(fileName, style: TextStyle(fontSize: 16))
        ]),
        Padding(padding: EdgeInsets.only(right: 16), child: Row(children: <Widget>[
          Expanded(child: Text(fileSize, textAlign: TextAlign.left)),
          Expanded(child: Text(permission, textAlign: TextAlign.center)),
          Expanded(child: Text(time, textAlign: TextAlign.right)),
        ]))
      ]))
    ]);
  }

  Widget _getFilePathWidget(String folderName){
    return Row(children: <Widget>[
      Text("/", style: TextStyle(color: Colors.blue)),
      Text(folderName),
    ]);
  }

  Widget _getBottomButtom(String name, IconData icon){
    return Expanded(child: Padding(padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16), child: Column(children: <Widget>[
      Icon(icon),
      Text(name)
    ])));
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
