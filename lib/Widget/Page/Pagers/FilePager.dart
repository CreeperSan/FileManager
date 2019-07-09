import 'package:flutter/material.dart';
import 'package:file_manager/Collection/StackCollection.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';
import 'package:file_manager/Widget/Dialog/CreateFileDirectoryDialog.dart';
import 'package:file_manager/MethodChannel/FileChannel.dart';
import 'dart:async';

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
  static const int _stateIdle = 0;
  static const int _stateSuccess = 1;
  static const int _stateRefreshing = 2;
  static const int _stateSearching = 3;
  static const int _stateSelecting = 4;

  int currentState = _stateSuccess;

  FileStack fileStack = FileStack();

  @override
  void initState() {
    super.initState();
    // 初始化虛擬數據
    FolderItem item = FolderItem();
    item.folderName = "folder";
    for(int i=0; i<64; i++){
      FileItem tmpItem = FileItem();
      tmpItem.name = "文件$i.txt";
      item._fileList.add(tmpItem);
    }
    fileStack.fileStack.add(item);
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
        Expanded(child: _getContentWidget()),
        Divider(height: 1, color: Colors.blueGrey),
        _getBottomWidget()
      ])),
    ]);
  }

  Widget _getContentWidget(){
    switch(currentState){
      case _stateSelecting:
      case _stateSuccess:
        return ListView(children: <Widget>[
          Column(mainAxisAlignment: MainAxisAlignment.center, children: _getCurrentFolderFileWidgetList())
        ]);
      case _stateRefreshing:
        return Column(mainAxisSize: MainAxisSize.max, mainAxisAlignment: MainAxisAlignment.center,crossAxisAlignment: CrossAxisAlignment.center, children: <Widget>[
          CircularProgressIndicator(),
          Container(height: 12),
          Text("正在讀取文件列表...")
        ]);
      default:
        return Center(child: Text("Idle..."));
    }
  }

  /// 點擊事件
  void _onSelectTap(){
    if(currentState == _stateSuccess){
      setState(() { currentState = _stateSelecting; });
    }else if(currentState == _stateSelecting){
      setState(() { currentState = _stateSuccess; });
    }
  }

  void _onNewTap(){
    showDialog(
        context: context,
        builder: (context){
          const FILE_TYPE_FILE = 0;
          const FILE_TYPE_DIRECTORY = 1;
          int fileType = FILE_TYPE_FILE;
          String dialogTitle = "創建文件";

          return StatefulBuilder(builder: (context, state){
            void onValueChange(int value){
              print("value -> $value");
              state(() {
                fileType = value;
                switch(fileType){
                  case FILE_TYPE_FILE :
                    dialogTitle = "創建文件";
                    break;
                  case FILE_TYPE_DIRECTORY :
                    dialogTitle = "創建文件夾";
                    break;
                }
              });
            }

            return AlertDialog(
              title: Text(dialogTitle),
              content: SingleChildScrollView(child: ListBody(children: <Widget>[
                Container(height: 30, child: Row(children: <Widget>[
                  Expanded(child: RadioListTile(title: Text("文件"), value: FILE_TYPE_FILE, groupValue: fileType, onChanged: onValueChange)),
                  Expanded(child: RadioListTile(title: Text("文件夾"), value: FILE_TYPE_DIRECTORY, groupValue: fileType, onChanged: onValueChange)),
                ])),
                TextField(decoration: InputDecoration(
                    labelText: "請輸入名稱"
                )),
              ])),
              actions: <Widget>[
                FlatButton(child: Text("創建"), onPressed: ()=>{
                  Navigator.of(context).pop()
                })
              ],
            );
          });
        }
    );
  }

  void _onSearchTap(){
    showDialog(
      context: context,
      builder: (context){
        return StatefulBuilder(builder: (context, state){
          return AlertDialog(
            title: Text("搜索文件"),
            content: SingleChildScrollView(child: ListBody(children: <Widget>[
              TextField(decoration: InputDecoration(
                labelText: "請輸入搜索的文件内容"
              ))
            ])),
            actions: <Widget>[
              FlatButton(child: Text("搜索"), onPressed: ()=>{
                Navigator.of(context).pop()
              })
            ]
          );
        });
      }
    );
  }

  void _onRefreshTap(){
    setState(() { currentState = _stateRefreshing; });
    new Future.delayed(Duration(seconds: 1), ()=>{
      setState(() { currentState = _stateSuccess; })
    });
  }

  void _onMoreTap(){
    showModalBottomSheet(context: context, builder: (BuildContext context){
      return BottomSheet(builder: (builderContext){
        return Column(mainAxisSize: MainAxisSize.min, children: <Widget>[
          ListTile(
            leading: Icon(Icons.info),
            title: Text("文件詳情"),
            trailing: Icon(Icons.arrow_right),
          ),
        ]);
      }, onClosing: (){});
    });
  }

  Widget _getBottomWidget(){
    return Row(children: <Widget>[
      _createBottomButtonWidget("選擇", Icons.select_all, _onSelectTap),
      _createBottomButtonWidget("新建", Icons.add, _onNewTap),
      _createBottomButtonWidget("搜索", Icons.search, _onSearchTap),
      _createBottomButtonWidget("刷新", Icons.refresh, _onRefreshTap),
      _createBottomButtonWidget("更多", Icons.more_horiz, _onMoreTap),
    ]);
  }

  List<Widget> _getCurrentFolderFileWidgetList(){
    List<Widget> widgetList = [];
    fileStack.fileStack[0]._fileList.forEach((fileItem){
      widgetList.add(_getFileItemWidgett(fileItem));
    });
    return widgetList;
  }

  Widget _getFileItemWidgett(FileItem fileItem){
    if(currentState == _stateSelecting){
      return Row(children: <Widget>[
        Container(width: 48, height: 48, child: Icon(Icons.insert_drive_file)),
        Expanded(child: Column(children: <Widget>[
          Row(children: <Widget>[
            Text(fileItem.name, style: TextStyle(fontSize: 16))
          ]),
          Padding(padding: EdgeInsets.only(right: 16), child: Row(children: <Widget>[
            Expanded(child: Text(fileItem.getFileSize(), textAlign: TextAlign.left)),
            Expanded(child: Text(fileItem.getPermissionText(), textAlign: TextAlign.center)),
            Expanded(child: Text(fileItem.getModifyTime(), textAlign: TextAlign.right)),
          ]))
        ])),
        Checkbox(value: fileItem.isSelected, onChanged: (newState){
          setState(() { fileItem.isSelected = !fileItem.isSelected; });
        })
      ]);
    }else{
      return Row(children: <Widget>[
        Container(width: 48, height: 48, child: Icon(Icons.insert_drive_file)),
        Expanded(child: Column(children: <Widget>[
          Row(children: <Widget>[
            Text(fileItem.name, style: TextStyle(fontSize: 16))
          ]),
          Padding(padding: EdgeInsets.only(right: 16), child: Row(children: <Widget>[
            Expanded(child: Text(fileItem.getFileSize(), textAlign: TextAlign.left)),
            Expanded(child: Text(fileItem.getPermissionText(), textAlign: TextAlign.center)),
            Expanded(child: Text(fileItem.getModifyTime(), textAlign: TextAlign.right)),
          ]))
        ]))
      ]);
    }
  }

  Widget _getFileItemWidget(String fileName, String fileSize, String permission, String time){
    if(currentState == _stateSelecting){
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
        ])),
        Checkbox(value: true, onChanged: (newState){

        })
      ]);
    }else{
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
  }

  Widget _getFilePathWidget(String folderName){
    return Row(children: <Widget>[
      Text("/", style: TextStyle(color: Colors.blue)),
      Text(folderName),
    ]);
  }

  Widget _createBottomButtonWidget(String name, IconData icon, Function onTab) {
    return Expanded(child: GestureDetector(onTap: onTab,
        child: Padding(
            padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
            child: Column(children: <Widget>[Icon(icon), Text(name)]))));
  }

}

/// 文件管理隊列
class FileStack{
  List<FolderItem> fileStack = List();

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
  bool isSelected = false;
  bool isDirectory = false;
  String path = "";
  String name = "";
  int fileSize;
  int createTimeStamp = 0;
  int modifyTimeStamp = 0;

  String getPermissionText(){
    return "rwe";
  }

  String getFileSize(){
    return "26.03kb";
  }

  String getModifyTime(){
    return "03/07/19";
  }
}
