package utils

import java.io.File


object Utils {

  val windowsPath="F:/Eggplant/files"
  val linuxPath=""


  val path:String={
    if(new File(windowsPath).exists()) windowsPath else linuxPath
  }



}
