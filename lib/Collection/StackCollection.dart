class StackCollection<T>{
  List<T> _itemList = [];

  void push(T item){
    _itemList.add(item);
  }

  T pop(){
    if(_itemList.length > 0){
      return _itemList.removeLast();
    }else{
      return null;
    }
  }

  int size(){
    return _itemList.length;
  }

}