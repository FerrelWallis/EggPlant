package services

import dao.EggplantDao
import javax.inject.Inject
import play.api.mvc.ControllerComponents
import utils.TableUtils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class onStart @Inject()(eggplantDao:EggplantDao){
  TableUtils.EggplantMap = Await.result(eggplantDao.getAllEggplant, Duration.Inf)
  TableUtils.EggGene=TableUtils.EggplantMap.map(_.geneid).map(_+".1")
  TableUtils.EggChr=TableUtils.EggplantMap.map(_.chr).distinct
}
