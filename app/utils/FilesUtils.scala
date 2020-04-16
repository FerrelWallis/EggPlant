package utils

import java.io.{File, PrintWriter}

import org.apache.commons.io.FileUtils

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration
//允许java代码
import scala.collection.JavaConverters._
import dao.EggplantDao

object FilesUtils {

  def main(args: Array[String]): Unit = {


//    val newlist=takeLength.toList.sortBy{case(chr,num)=>chr}
//    println(newlist)
//    val writer= new PrintWriter(new File("F:\\Eggplant\\files\\length.txt"))
//    writer.flush()
//    writer.print("egg"+"\t")
//    newlist.foreach{x=>
//      writer.print(x._2+"\t")
//    }
//    writer.close()


  }


  def joinAll ={
    val gff=takeGFF
    val kegg=takeKEGG
    val nr=takeNR
    val swiss=takeSwissport
    val pfam=takePfam
    val golist=joinGo(takeWego,takeGolist)
    gff.map{ g=>
      val k=kegg.getOrElse(g._1,("—","—"))
      val n=nr.getOrElse(g._1,"—")
      val s=swiss.getOrElse(g._1,"—")
      val p=pfam.getOrElse(g._1,("—","—"))
      val go=golist.getOrElse(g._1,("—","—"))
      Array(g._1,g._2,g._3,g._4,g._5,k._1,k._2,p._1,p._2,n,s,go._1,go._2)
    }
  }


  def takeLength={
    val fileFa=FileUtils.readLines(new File("F:\\Eggplant\\files\\download\\" +
      "hic.fasta")).asScala
    var num = 0
    var chr=""
    var mapFa=mutable.HashMap[String,Int]()
    fileFa.filter(_.trim!="").foreach{x=>
      if(x.startsWith(">")){
        if(chr.indexOf(">E")==0){
          mapFa+=(chr->num)
        }
        num =0
        chr=x
      }else{
        num += x.length
      }
    }
    mapFa
  }


  def takeGFF ={
    val fileGff=
      FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
        "学习文档\\基因\\茄子基因组数据\\final_qiezi.gff")).asScala

    val mapGff= fileGff.map(_.split("\t")).filter(_(2)=="gene").map(list=>{
      val chr =list.head

      val start=list(3)
      val end=list(4)
      val strand=list(6)
      val idName=list(8).substring(list(8).indexOf("=")+1,list(8).indexOf(";"))
      (idName,chr,start,end,strand)
    })
    mapGff

  }


  def takeKEGG ={
    val fileKegg=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_KEGG")).asScala

    println("KEGG ID NUM:"+testIdNum(fileKegg))
    var mapKEGG=mutable.HashMap[String,(String,String)]()

    fileKegg.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val Knum=list(15).substring(list(15).indexOf("K"),list(15).indexOf(" "))
      val Kdiscription=judge(list(15))
      mapKEGG+=(geneid->(Knum,Kdiscription))
    }
    mapKEGG
  }


  def takeNR ={
    val fileNR=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_NR")).asScala

    println("NR ID NUM:"+testIdNum(fileNR))

    var mapNR=mutable.HashMap[String,String]()
    fileNR.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val Ndiscription=judge(list(13))
      mapNR+=(geneid->Ndiscription)
    }
    mapNR

  }

  def takeSwissport ={
    val fileSwiss=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\学习文档\\基因\\" +
      "茄子基因组数据\\04.function_annotation\\sort_Swissprot")).asScala

    println("Swissport ID NUM:"+testIdNum(fileSwiss))

    var mapSwiss=mutable.HashMap[String,String]()
    fileSwiss.filter(_.trim!="").map{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val Sdiscription=judge(list(15))
      mapSwiss+=(geneid->Sdiscription)
    }
    mapSwiss
  }

  def takePfam ={
    val filePfam=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\学习文档\\" +
      "基因\\茄子基因组数据\\04.function_annotation\\sort_stat.iprscan.pfamlist")).asScala

    println("Pfam ID NUM:"+testIdNum(filePfam))

    val lists=filePfam.filter(_.trim!="").map{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val pfam_num=list(5).split('(').head
      val pfam_des=list(5)
      (geneid,pfam_num,pfam_des)
    }

    val pmap=lists.distinct.groupBy(_._1).map{x=>
      val pf = x._2.map(_._2).mkString(";")
      val pf_d = x._2.map(_._3).mkString(";;")
      println(x._1 -> (pf,pf_d))
      x._1 -> (pf,pf_d)
    }
    pmap
  }



  def takeWego = {
    val fileWego = FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_stat.iprscan.wego")).asScala

    println("Wego ID NUM:" + testIdNum(fileWego))

    //var mapWego = mutable.HashMap[String, List[String]]()

    val wegolist = fileWego.filter(_.trim != "").map { line =>
      val list = line.split('\t')
      val geneid = list.head.split('.').head
      val len = list.toList.drop(1).length
      val li = List(geneid).padTo(len, geneid)

      li zip list.toList.drop(1)
    }

    wegolist.flatten

  }


  def takeGolist={
    val fileGolist=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_stat.iprscan.golist")).asScala

    println("Golist ID NUM:"+testIdNum(fileGolist))
    var mapGolist=mutable.HashMap[String,String]()

    fileGolist.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val discription=list(2)
      mapGolist+=(geneid->discription)
    }
    mapGolist

  }

  def joinGo(wegolist:mutable.Buffer[(String,String)],golist:mutable.HashMap[String,String])={

    val joinedGolist=wegolist.map{id_go=>
      val geneid=id_go._1
      val goid=id_go._2
      var discription=""
      if(golist(geneid).indexOf(goid)!=(-1))
        discription=golist(geneid).substring(0,golist(geneid).indexOf(goid)-1)
      while(discription.indexOf("GO:")!=(-1))
        discription=discription.substring(discription.indexOf("GO:")+12)
      discription=goid+" "+discription
      (geneid,goid,discription)
    }


    val go=joinedGolist.distinct.groupBy(_._1).map{x=>
      val go=x._2.map(_._2).mkString(";")
      val go_des=x._2.map(_._3).mkString(";;")
      x._1->(go,go_des)
    }
    go
  }


  def judge(x:String) ={
    if(x.indexOf('\"')!=(-1))
      x.substring(1,x.length-1)
    else
      x
  }

  def testIdNum(fileName:mutable.Buffer[String])={
    //检查id数量是否有重复
    val idlist = fileName.filter(_.trim != "").map{line=>
      val list=line.split('\t')
      list.head.split('.').head
    }
    val idlistDis=idlist.distinct
    if(idlist.size==idlistDis.size)
      true
    else
      false
  }

}
