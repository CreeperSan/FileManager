import 'package:flutter/material.dart';

abstract class BasePager extends StatefulWidget{
  String getPagerName();
  IconData getPagerIconWidget(bool isSelected);
}

