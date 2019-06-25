import 'package:flutter/material.dart';
import 'package:file_manager/Widget/Page/BootPage.dart';
import 'package:file_manager/main.dart';

class FileApplication extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      title: "我的文件",
      theme: ThemeData(
        primarySwatch: Colors.deepOrange
      ),
      home: BootPage(),
    );
  }

}
