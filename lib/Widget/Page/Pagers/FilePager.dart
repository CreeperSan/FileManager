import 'package:flutter/material.dart';
import 'package:file_manager/Collection/StackCollection.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';
import 'package:file_manager/Widget/Dialog/CreateFileDirectoryDialog.dart';
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

  int currentState = _stateSuccess;


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
        Expanded(child: _getContentWidget()),
        Divider(height: 1, color: Colors.blueGrey),
        Row(children: <Widget>[
          _getBottomButton("選擇", Icons.select_all, _onSelectTap),
          _getBottomButton("新建", Icons.add, _onNewTap),
          _getBottomButton("搜索", Icons.search, _onSearchTap),
          _getBottomButton("刷新", Icons.refresh, _onRefreshTap),
          _getBottomButton("更多", Icons.more_horiz, _onMoreTap),
        ])
      ])),
    ]);
  }

  Widget _getContentWidget(){
    switch(currentState){
      case _stateSuccess:
        return ListView(children: <Widget>[
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

  }

  void _onRefreshTap(){
    setState(() { currentState = _stateRefreshing; });
    new Future.delayed(Duration(seconds: 1), ()=>{
      setState(() { currentState = _stateSuccess; })
    });
  }

  void _onMoreTap(){

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

  Widget _getBottomButton(String name, IconData icon, Function onTab) {
    return Expanded(child: GestureDetector(onTap: onTab,
        child: Padding(
            padding: EdgeInsets.symmetric(vertical: 8, horizontal: 16),
            child: Column(children: <Widget>[Icon(icon), Text(name)]))));
  }

  Widget _getCreateDialog(BuildContext context){
    final FILE_TYPE_FILE = 0;
    final FILE_TYPE_DIRECTORY = 1;
    int fileType = FILE_TYPE_FILE;
    String dialogTitle = "創建";

    return StatefulBuilder(builder: (context, state){

      void onValueChange(int value){
        print("select -> $value");
        state(() {});
      }

      return AlertDialog(
        title: Text(dialogTitle),
        content: SingleChildScrollView(child: ListBody(children: <Widget>[
          Row(children: <Widget>[
            Expanded(child: RadioListTile(title: Text("文件"), value: FILE_TYPE_FILE, groupValue: fileType, onChanged: onValueChange)),
            Expanded(child: RadioListTile(title: Text("文件夾"), value: FILE_TYPE_DIRECTORY, groupValue: fileType, onChanged: onValueChange)),
            Expanded(child: RadioListTile(title: Text("???"), value: 3, groupValue: fileType, onChanged: (value){
              fileType++;
              fileType = fileType~/3;
            })),
          ]),
          Text(fileType.toString()),
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
