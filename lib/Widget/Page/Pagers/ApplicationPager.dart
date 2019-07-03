import 'package:flutter/material.dart';
import 'package:file_manager/Widget/Page/Pagers/BasePager.dart';

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
  @override
  Widget build(BuildContext context) {
    return Center(child: Text("asdasd"));
  }

}

