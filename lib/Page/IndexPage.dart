import 'package:flutter/material.dart';
import 'package:file_manager/Page/Pagers/BasePager.dart';
import 'package:file_manager/Page/Pagers/HomePager.dart';
import 'package:file_manager/Page/Pagers/FilePager.dart';

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
    _pagerList.clear();
    _pagerList.add(new FilePager());
    _pagerList.add(new HomePager());
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
    );
  }

}
