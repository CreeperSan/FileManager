import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';
import 'dart:async';

class ApplicationPager extends BasePager{
  @override
  State<StatefulWidget> createState() {
    return ApplicationState();
  }

  @override
  IconData getPagerIconWidget(bool isSelected) {
    return Icons.android;
  }

  @override
  String getPagerName() {
    return "應用";
  }

}

class ApplicationState extends State<ApplicationPager>{
  static const application = const MethodChannel("samples.flutter.io/battery");

  List<_ApplicationItem> _applicationItem = [];

  @override
  Widget build(BuildContext context) {
    _applicationItem.clear();
    _applicationItem.add(_ApplicationItem());

    return GridView.custom(
        gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(crossAxisCount: 3, mainAxisSpacing: 10, crossAxisSpacing: 20),
        childrenDelegate: SliverChildBuilderDelegate((context, position){
          return _getApplicationWidget(context, _applicationItem[position], position);
        }, childCount: _applicationItem.length)
    );
  }

  Widget _getApplicationWidget(BuildContext context, _ApplicationItem item, int position){
    return Center(child: Column(children: <Widget>[Text("App #$position"), RaisedButton(child: Text("Click Me"), onPressed:  _onPressed)]));
  }

  void _onPressed()async{
    String applicationListJson = await application.invokeMethod("getApplicationList");
    print(applicationListJson);
  }

}

class _ApplicationItem{
  String name = "App";
}

