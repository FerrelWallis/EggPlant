package test

import java.io.File

import org.apache.commons.io.FileUtils

import scala.collection.mutable
//允许java代码
import scala.collection.JavaConverters._

object fileUtils {

  def main(args: Array[String]): Unit = {

    println(takeGFF)
    println(takeKEGG)
    println(takeNR)
    println(takeSwissport)
    println(takePfam)

    joinGo(takeWego,takeGolist)

//    val test="molecular_function:\"Interacting selectively and non-covalently with a nucleotide, any compound consisting of a nucleoside that is esterified with (ortho)phosphate or an oligophosphate at any hydroxyl group on the ribose or deoxyribose.\" [GOC:mah, ISBN:0198547684](GO:0000166)," +
//      "molecular_function:\"Interacting selectively and non-covalently with any nucleic acid.\" [GOC:jl](GO:0003676)," +
//      "molecular_function:\"Catalysis of the formation of aminoacyl-tRNA from ATP, amino acid, and tRNA with the release of diphosphate and AMP.\" [ISBN:0198506732](GO:0004812)," +
//      "molecular_function:\"Catalysis of the reaction: ATP + L-alanine + tRNA(Ala) = AMP + diphosphate + L-alanyl-tRNA(Ala).\" [EC:6.1.1.7](GO:0004813)," +
//      "molecular_function:\"Interacting selectively and non-covalently with ATP, adenosine 5'-triphosphate, a universally important coenzyme and enzyme regulator.\" [ISBN:0198506732](GO:0005524)," +
//      "cellular_component:\"All of the contents of a cell excluding the plasma membrane and nucleus, but including other subcellular structures.\" [ISBN:0198547684](GO:0005737)," +
//      "biological_process:\"The process of coupling alanine to alanyl-tRNA, catalyzed by alanyl-tRNA synthetase. The alanyl-tRNA synthetase is a  class-II synthetases. The activated amino acid is transferred to the 3'-OH group of an alanine accetping tRNA.\" [GOC:mcc, ISBN:0716730510](GO:0006419)," +
//      "biological_process:\"The chemical reactions and pathways by which the various amino acids become bonded to their corresponding tRNAs. The most common route for synthesis of aminoacyl tRNA is by the formation of an ester bond between the 3'-hydroxyl group of the most 3' adenosine of the tRNA and the alpha carboxylic acid  group of an amino acid, usually catalyzed by the cognate aminoacyl-tRNA ligase. A given aminoacyl-tRNA ligase aminoacylates all species of an isoaccepting group of tRNA molecules.\" [GOC:ma, GOC:mah, MetaCyc:Aminoacyl-tRNAs](GO:0043039)"
//
//    var dis=""
//    if(test.indexOf("GO:0006419")!=(-1))
//        dis=test.substring(0,test.indexOf("GO:0006419")-1)
//    while(dis.indexOf("GO:")!=(-1)){
//      dis=dis.substring(dis.indexOf("GO:")+12)
//    }
//    println("dis:"+dis)

  }


  def takeGFF ={
    val fileGff=
      FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
        "学习文档\\基因\\茄子基因组数据\\final_qiezi.gff")).asScala

    import scala.collection.mutable
    var mapGff=mutable.HashMap[String,List[String]]()

    fileGff.foreach(line=>{
      val list=line.split('\t')
      //确保无重复
      val distincted=list.distinct
      val types=list(2)
      if(types.equals("gene")){
        val start=list(3)
        val end=list(4)
        val strand=list(6)
        val idName=list(8).substring(list(8).indexOf("=")+1,list(8).indexOf(";"))
        mapGff+=(idName->List(idName,start,end,strand))
      }
    })

    "mapGFF:"+mapGff.size
  }


  def takeKEGG ={
    val fileKegg=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_KEGG")).asScala

    println("KEGG ID NUM:"+testIdNum(fileKegg))

    var mapKEGG=mutable.HashMap[String,List[String]]()

    fileKegg.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val Knum=list(15).substring(list(15).indexOf("K"),list(15).indexOf(" "))
      val discription=judge(list(15))
      mapKEGG+=(geneid->List(geneid,Knum,discription))
    }

    "mapKEGG:"+mapKEGG.size
  }


  def takeNR ={
    val fileNR=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\" +
      "学习文档\\基因\\茄子基因组数据\\04.function_annotation\\sort_NR")).asScala

    println("NR ID NUM:"+testIdNum(fileNR))

    var mapNR=mutable.HashMap[String,List[String]]()
    fileNR.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val discription=judge(list(13))
      mapNR+=(geneid->List(geneid,discription))
    }
    "mapNR:"+mapNR.size

  }

  def takeSwissport ={
    val fileSwiss=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\学习文档\\基因\\" +
      "茄子基因组数据\\04.function_annotation\\sort_Swissprot")).asScala

    println("Swissport ID NUM:"+testIdNum(fileSwiss))

    var mapSwiss=mutable.HashMap[String,List[String]]()
    fileSwiss.filter(_.trim!="").foreach{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val discription=judge(list(15))
      mapSwiss+=(geneid->List(geneid,discription))
    }
    "Swissport"+mapSwiss.size
  }

  def takePfam ={
    val filePfam=FileUtils.readLines(new File("C:\\Users\\yingf\\Desktop\\学习文档\\" +
      "基因\\茄子基因组数据\\04.function_annotation\\sort_stat.iprscan.pfamlist")).asScala

    println("Pfam ID NUM:"+testIdNum(filePfam))

    val lists=filePfam.filter(_.trim!="").map{line=>
      val list=line.split('\t')
      val geneid=list.head.split('.').head
      val pfam_acc=list(5).split('(').head
      val pfam_des=list(5)
      (geneid,pfam_acc,pfam_des)
    }

    lists.groupBy(_._1)
    "Pfam"+lists.groupBy(_._1).size
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
      println((geneid,goid,discription))
      (geneid,goid,discription)
    }

    joinedGolist
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
