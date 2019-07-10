import 'package:flutter/material.dart';

class HintBackgroundWidget extends StatelessWidget{

  final IconData icon;
  final String hint;
  final List<Widget> child;
  final String buttonText;
  final Function() onTap;

  final Color color;

  HintBackgroundWidget(this.icon, this.hint, { this.child, this.color, this.buttonText, this.onTap });

  @override
  Widget build(BuildContext context) {
    return new Center(child: Column( mainAxisSize: MainAxisSize.min, children: getWidgetList()));
  }

  List<Widget> getChild(){
    if(child == null){
      return [];
    }else{
      return child;
    }
  }

  Color getColor(){
    return color==null ? Colors.grey : color;
  }

  Widget getRaiseButton(){
    if(buttonText == null || onTap == null){
      return null;
    }else{
      return RaisedButton(child: Text(buttonText), onPressed: onTap);
    }
  }

  List<Widget> getWidgetList(){
    List<Widget> widgetList = [];

    widgetList.add(Icon(icon, size: 64, color: getColor()));
    widgetList.add(Padding(padding: EdgeInsets.only(top: 16), child: Text(hint, style: TextStyle(color: getColor()))));
    Widget buttonWidget = getRaiseButton();
    if(buttonWidget != null){
      widgetList.add(buttonWidget);
    }
    widgetList.add(Padding(padding: EdgeInsets.only(top: 16), child: Column(mainAxisSize: MainAxisSize.min, children: getChild())));


    return widgetList;
  }

}
