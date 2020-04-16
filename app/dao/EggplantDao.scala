package dao

import javax.inject.Inject
import models.Tables._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

class EggplantDao @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit exec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile]{

  import profile.api._

  def insert(row:Seq[EggplantRow]) : Future[Unit] = {
    db.run(Eggplant ++= row).map(_=>())
  }

  def getAllEggplant:Future[Seq[EggplantRow]]={
    db.run(Eggplant.result)
  }



  def getByGeneId(geneid:String) : Future[EggplantRow] = {
    db.run(Eggplant.filter(_.geneid === geneid).result.head)
  }



  //  def getByPosition(userid:Int,name:String) : Future[Seq[EggplantRow]] = {
  //    //db.run(Brassicaassembly.filter(_.userid === userid).filter(_.name === name).result)
  //  }
  //
  //  def updateState(id:Int,state:Int) : Future[Unit] = {
  //    //db.run(Brassicaassembly.filter(_.id === id).map(_.state).update(state)).map(_=>())
  //  }
  //
  //  def getByUser(userid:Int) : Future[Seq[EggplantRow]] = {
  //    //db.run(Brassicaassembly.filter(_.userid === userid).result)
  //  }
  //
  //  def getById(id:Int) : Future[EggplantRow] = {
  //    //db.run(Brassicaassembly.filter(_.id === id).result.head)
  //  }
  //
  //  def deleteById(id:Int) : Future[Unit] = {
  //    //db.run(Brassicaassembly.filter(_.id === id).delete).map(_=>())
  //  }

}
