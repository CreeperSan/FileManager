import 'package:file_manager/MethodChannel/FileChannel.dart';

class ChannelHelper{
  static FileChannel fileChannel;

  static FileChannel getFileChannel(){
    if(fileChannel == null){
      fileChannel = FileChannel();
    }
    return fileChannel;
  }

}