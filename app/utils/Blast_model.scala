package utils

import play.api.libs.json.{JsObject, Json}
import utils.Read._
import utils.Utils

import scala.collection.mutable
import scala.language.implicitConversions

object Blast {
  implicit def getDataByTable(path:String) : GetDataByTable = new GetDataByTable(path)
}

class GetDataByTable(val path:String){
  val xlsx:mutable.Buffer[String] = (path+ "/table.xls").readLine
  //  val xlsx = FileUtils.readLines(new File(Utils.path+ "/table.xls"), "UTF-8").asScala

  val line: mutable.Buffer[Array[String]] = xlsx.tail.map(_.split("\t").map(_.trim))

  val sort1 : mutable.Buffer[String] = (path + "/seq.fa").readLine.filter(_.startsWith(">")).map{x=>
    x.tail.split(" ").head
  }

  def getBarData : mutable.Buffer[JsObject] = {
    val le =  (Utils.path + "/length.txt").readLine

    val heightM = le.map(_.split("\t")).map(x => x.head.trim -> x.tail).toMap

    val blastMap = line.map{xlx=>
      (xlx(5), xlx(11), xlx(13), xlx(14))
    }.groupBy(_._1).map{x=>
      (x._1,x._2.map(z=>(z._2, z._3, z._1 + "-" + z._2 + ":" + z._3 + "-" + z._4,z._2 + "-" + z._1)))
    }


    val barMap = line.map(x=> (x(5),x(11))).distinct

    val sort = line.map(x=>(x(5), x(11).split("-").last)).distinct

     sort.map(_._1).distinct.zipWithIndex.flatMap { x =>
      sort.filter(_._1 == x._1).zipWithIndex.map { y =>
        val bar = y._1._2
        val height = heightM(bar)
        val s = height.map(_.toInt).max
        val xx = s.toString.take(2).toInt
        var z = xx + 1
        while (z % 5 != 0 && z % 6 != 0) {
          z += 1
        }
        val index = if (z % 5 == 0) 5 else 6
        val e = z + "0" * (s.toString.length - 2)
        val max = e
        val yaxis = index
        val barIndex = barMap.filter(_._1 == x._1).map(_._2).distinct.zipWithIndex

        val gen = blastMap(x._1).map { g =>
          Json.obj("name" -> x._1, "chr" -> g._1.drop(3), "height" -> g._2, "title" -> g._3,"hitid" -> ((x._2+1) + "-" + (barIndex.filter(_._1 == g._4).head._2+1)))
        }
        Json.obj("seq" -> (sort1.indexOf(x._1)+1),"bar" -> bar, "height" -> height, "max" -> max, "yaxis" -> yaxis, "gen" -> gen)
      }
    }
  }

  def getBarData2 : mutable.Buffer[JsObject] = {
    val le =  (Utils.path + "/length.txt").readLine

    val heightM = le.head.split("\t").tail

//    val blastMap = line.map { xlx =>
//      (xlx(5), xlx(11).split("-").last, xlx(11).split("-").head, xlx(13), xlx(14))
//    }.groupBy(_._1).map { x =>
//      (x._1 , x._2.groupBy(_._2).map { y =>
//        (y._1 , y._2.map { z =>
//          val chr =  z._2 + "-" + z._3
//          val range =  z._4 + "-" + z._5
//          val length = Math.abs(z._5.toInt - z._4.toInt)
//          val title = "E : " + chr + "\nRange : " + range + "\nLength : " + length
//          (z._3, z._4, title, z._3 + "-" + z._2, length)
//        })
//      })
//    }

    val blastMap = line.map{xlx=>
      (xlx(5), xlx(11), xlx(13), xlx(14))
    }.groupBy(_._1).map{x=>
      (x._1,x._2.map { z =>
        val chr = z._2
        val range = z._3 + "-" + z._4
        val length = Math.abs(z._4.toInt - z._3.toInt)
        val title = "E : " + chr + "\nRange : " + range + "\nLength : " + length
        (z._2,z._3, title, length)
      })

    }


    val barMap = line.map(x=> (x(5),x(11))).distinct

    val sort = line.map(x=>(x(5), x(11).split("-").last)).distinct

    sort.map(_._1).distinct.zipWithIndex.flatMap { x =>
      sort.filter(_._1 == x._1).zipWithIndex.map { y =>
        val bar = y._1._2
        val height = heightM.zipWithIndex.map{z=>
          if(z._2 <9){
            Json.obj("key" ->  ("E0" + (z._2+1)),"value" ->  z._1)
          }else{
            Json.obj("key" ->  ("E" + (z._2+1)),"value" ->  z._1)
          }
        }

        val barIndex = barMap.filter(_._1 == x._1).map(_._2).distinct.zipWithIndex

        val sortMax = blastMap(x._1).sortBy(_._3).reverse
        val gen = sortMax.drop(3).map { g =>
          Json.obj("name" -> x._1, "key" ->("E" + "0"*(2-g._1.drop(1).trim.length) +  g._1.drop(1)), "value" -> g._2, "title" -> g._3,"hitid" ->   ((x._2+1) + "-" + (barIndex.filter(_._1 == g._1).head._2+1))
          )
        }
        val max3 = sortMax.take(3).map{g=>
          Json.obj("name" -> x._1, "key" ->("E" + "0"*(2-g._1.drop(1).trim.length) +  g._1.drop(1)), "value" -> g._2, "title" -> g._3,"hitid" ->  ((x._2+1) + "-" + (barIndex.filter(_._1 == g._1).head._2+1))
          )
        }

        Json.obj("seq" -> (sort1.indexOf(x._1)+1),"bar" -> bar, "height" -> height,  "gen" -> gen,"max3" -> max3)
      }
    }
  }


  def getSyntenyData : Array[JsObject] = {

    line.map{xlx=>
      (xlx(5),xlx(11),xlx(7),xlx(8),xlx(9),xlx(13),xlx(14))
    }.groupBy(_._1).flatMap{x=>
      val sort2 = line.filter(_(5) == x._1).map(_(11)).distinct
      x._2.groupBy(_._2).map{y=>
        val aName = x._1
        val aMin = 1
        val aMax = y._2.head._3.toInt
        val rectA = y._2.map{z=>
          Array("",z._4,z._5)
        }

        val bName = y._1
        val bRange = y._2.flatMap(z=> Array(z._6.toInt,z._7.toInt))
        val bMin = bRange.min
        val bMax = bRange.max
        val rectB = y._2.map{z=>
          Array("",z._6,z._7)
        }

        val relate = y._2.zipWithIndex.map(z=> Array(z._2+1,z._2+1))

        val data = Json.obj("A" -> Json.obj("name" -> aName, "min" -> aMin, "max" -> aMax, "seqType" -> "Query" ,"rect" -> rectA),
          "B" -> Json.obj("name" -> bName, "min" -> bMin, "max" -> bMax ,"seqType" -> "Subject", "rect" -> rectB))

        val i1 = sort1.indexOf(x._1) + 1
        val i2 = sort2.indexOf(y._1) + 1
        Json.obj("data" -> data, "relate"-> relate,"index" -> (i1 + "-" + i2))
      }
    }.toArray
  }

}
