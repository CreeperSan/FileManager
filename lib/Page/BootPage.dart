import 'dart:async';

import 'package:flutter/material.dart';
import 'package:file_manager/Page/IndexPage.dart';

class BootPage extends StatefulWidget{
  @override
  State<StatefulWidget> createState() {
    return new BootPageState();
  }

}

class BootPageState extends State<BootPage>{

  @override
  void initState() {
    super.initState();
    new Timer(const Duration(milliseconds: 1500), () {
      try {
        Navigator.of(context).pushAndRemoveUntil(new MaterialPageRoute(
            builder: (BuildContext context) => new IndexPage()), ( //跳转到主页
            Route route) => route == null);
      } catch (e) {

      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Center(child:
      Text("文件管理器")
    ));
  }

}
