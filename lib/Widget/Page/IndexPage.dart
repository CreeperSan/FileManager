import 'package:flutter/material.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';
import 'package:file_manager/Widget/Page/Pagers/HomePager.dart';
import 'package:file_manager/Widget/Page/Pagers/FilePager.dart';
import 'package:file_manager/Widget/Page/Pagers/ApplicationPager.dart';

class IndexPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new _IndexPageState();
  }

}

class _IndexPageState extends State<IndexPage>{
  String _titleName = "文件管理器";
  List<BasePager> _pagerList = [];

  @override
  void initState() {
    super.initState();
    // 初始化頁面
    print("初始化页面");
    _pagerList.clear();
    _pagerList.add(new FilePager());
    _pagerList.add(new HomePager());
    _pagerList.add(new ApplicationPager());
  }

  void _onPagerPageChange(int newPage){
    setState(() {
      _titleName = _pagerList[newPage].getPagerName();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        leading: Icon(Icons.menu),
        title: Text(_titleName)
      ),
      body: PageView.custom(
        childrenDelegate: SliverChildBuilderDelegate((context, index) => _pagerList[index], childCount: _pagerList.length),
        onPageChanged: _onPagerPageChange,
      ),
      drawer: Drawer(child: ListView(children: _getDrawerItems())),
    );
  }

  List<Widget> _getDrawerItems(){
    List<Widget> widgetList = [];
    widgetList.add(Image.asset("assets/image/img_drawer_header.png"));
    for(var pageItem in _pagerList){
      widgetList.add(Padding(padding: EdgeInsets.symmetric(horizontal: 12, vertical: 8), child: Row(children: <Widget>[
        Icon(pageItem.getPagerIconWidget(false), size: 28),
        Expanded(flex: 1, child: Padding(padding: EdgeInsets.only(left: 8), child: Text(pageItem.getPagerName()))),
      ])));
    }
    return widgetList;
  }

}
