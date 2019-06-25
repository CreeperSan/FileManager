import 'package:flutter/material.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';
import 'package:fluttertoast/fluttertoast.dart';

class HomePager extends BasePager{

  @override
  State<StatefulWidget> createState() {
    return new HomePagerState();
  }

  @override
  String getPagerName() {
    return "主頁";
  }

  @override
  IconData getPagerIconWidget(bool isSelected) {
    return Icons.home;
  }

}

class HomePagerState extends State<HomePager>{
  static const double _HEADER_ITEM_ICON_SIZE = 36;
  static const Color _HEADER_ITEM_COLOR = Colors.deepOrangeAccent;

  List<_IndexHeaderIcon> _headerIconList = [];

  @override
  void initState() {
    super.initState();
    // 初始化 Header
    _headerIconList.clear();
    _headerIconList.add(_IndexHeaderIcon("圖片", Icons.image, onClick: ()=> _toast("Image") ));
    _headerIconList.add(_IndexHeaderIcon("音樂", Icons.music_note, onClick: ()=> _toast("Music") ));
    _headerIconList.add(_IndexHeaderIcon("視頻", Icons.movie, onClick: ()=> _toast("Video") ));
    _headerIconList.add(_IndexHeaderIcon("文檔", Icons.description, onClick: ()=> _toast("Document") ));
    _headerIconList.add(_IndexHeaderIcon("應用", Icons.android, onClick: ()=> _toast("Application") ));
  }

  @override
  void didUpdateWidget(HomePager oldWidget) {
    super.didUpdateWidget(oldWidget);
  }

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
        scrollDirection: Axis.vertical,
        child: Column(children: <Widget>[
          Card(
              margin: EdgeInsets.symmetric(vertical: 6, horizontal: 12),
              child: Padding(
                  padding: EdgeInsets.all(12),
                  child: Column(
                    children: <Widget>[
                      // 這裏下面是頭部5個快捷方式
                      Row(children: _generateHeaderWidget()),
                    ],
                  ))
          ),
          // 這下面是剩餘的存儲空間
          Card(margin: EdgeInsets.symmetric(vertical: 6, horizontal: 12),
              child: Padding(
                  padding: EdgeInsets.all(12), child: Row(children: <Widget>[
                    Expanded(flex: 0, child: Icon(Icons.sd_storage, size: 36,)),
                    Expanded(flex: 1, child: Padding(padding: EdgeInsets.only(left: 4), child: Column(children: <Widget>[
                      Row(children: <Widget>[
                        Expanded(flex: 1, child: Text("内置存儲")),
                        Expanded(flex: 0, child: Text("16.52G / 64.00G"))
                      ]),
                      Padding(padding: EdgeInsets.only(top: 4),
                          child: LinearProgressIndicator(value: 0.2))
                    ]))),
              ]))),
          Text("22222"),
        ],));
  }

  /* 生成 View 的方法區 */
  List<Widget> _generateHeaderWidget(){
    List<Widget> list = [];
    for(var item in _headerIconList){
      list.add(Expanded(child: GestureDetector(onTap: item.onClick,child: Column(children: <Widget>[
        Icon(item.icon, size: _HEADER_ITEM_ICON_SIZE, color: _HEADER_ITEM_COLOR),
        Text(item.name, style: TextStyle(
          color: _HEADER_ITEM_COLOR
        ))
      ]))));
    }
    return list;
  }

  void _toast(String content){
    Fluttertoast.showToast(
      msg: content,
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.CENTER,
      timeInSecForIos: 2, // iOS Toast持續時間
      backgroundColor: Colors.blueGrey,
      textColor: Colors.white,
      fontSize: 16.0
    );
  }

}


class _IndexHeaderIcon{
  String name;
  IconData icon;
  void Function() onClick;

  _IndexHeaderIcon(
      this.name,
      this.icon,{
        this.onClick
  });
}
