package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Eggplant.schema ++ Mode.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Eggplant
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param start Database column start SqlType(TEXT)
   *  @param end Database column end SqlType(TEXT)
   *  @param strand Database column strand SqlType(TEXT)
   *  @param keggNum Database column kegg_num SqlType(TEXT)
   *  @param keggDes Database column kegg_des SqlType(TEXT)
   *  @param pfamNum Database column pfam_num SqlType(TEXT)
   *  @param pfamDes Database column pfam_des SqlType(TEXT)
   *  @param nrDes Database column nr_des SqlType(TEXT)
   *  @param swissDes Database column swiss_des SqlType(TEXT)
   *  @param goNum Database column go_num SqlType(TEXT)
   *  @param goDes Database column go_des SqlType(TEXT) */
  case class EggplantRow(id: Int, geneid: String, chr: String, start: String, end: String, strand: String, keggNum: String, keggDes: String, pfamNum: String, pfamDes: String, nrDes: String, swissDes: String, goNum: String, goDes: String)
  /** GetResult implicit for fetching EggplantRow objects using plain SQL queries */
  implicit def GetResultEggplantRow(implicit e0: GR[Int], e1: GR[String]): GR[EggplantRow] = GR{
    prs => import prs._
    EggplantRow.tupled((<<[Int], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table eggplant. Objects of this class serve as prototypes for rows in queries. */
  class Eggplant(_tableTag: Tag) extends profile.api.Table[EggplantRow](_tableTag, Some("eggplant"), "eggplant") {
    def * = (id, geneid, chr, start, end, strand, keggNum, keggDes, pfamNum, pfamDes, nrDes, swissDes, goNum, goDes) <> (EggplantRow.tupled, EggplantRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(geneid), Rep.Some(chr), Rep.Some(start), Rep.Some(end), Rep.Some(strand), Rep.Some(keggNum), Rep.Some(keggDes), Rep.Some(pfamNum), Rep.Some(pfamDes), Rep.Some(nrDes), Rep.Some(swissDes), Rep.Some(goNum), Rep.Some(goDes))).shaped.<>({r=>import r._; _1.map(_=> EggplantRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column start SqlType(TEXT) */
    val start: Rep[String] = column[String]("start")
    /** Database column end SqlType(TEXT) */
    val end: Rep[String] = column[String]("end")
    /** Database column strand SqlType(TEXT) */
    val strand: Rep[String] = column[String]("strand")
    /** Database column kegg_num SqlType(TEXT) */
    val keggNum: Rep[String] = column[String]("kegg_num")
    /** Database column kegg_des SqlType(TEXT) */
    val keggDes: Rep[String] = column[String]("kegg_des")
    /** Database column pfam_num SqlType(TEXT) */
    val pfamNum: Rep[String] = column[String]("pfam_num")
    /** Database column pfam_des SqlType(TEXT) */
    val pfamDes: Rep[String] = column[String]("pfam_des")
    /** Database column nr_des SqlType(TEXT) */
    val nrDes: Rep[String] = column[String]("nr_des")
    /** Database column swiss_des SqlType(TEXT) */
    val swissDes: Rep[String] = column[String]("swiss_des")
    /** Database column go_num SqlType(TEXT) */
    val goNum: Rep[String] = column[String]("go_num")
    /** Database column go_des SqlType(TEXT) */
    val goDes: Rep[String] = column[String]("go_des")

    /** Primary key of Eggplant (database name eggplant_PK) */
    val pk = primaryKey("eggplant_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Eggplant */
  lazy val Eggplant = new TableQuery(tag => new Eggplant(tag))

  /** Entity class storing rows of table Mode
   *  @param id Database column id SqlType(INT), PrimaryKey
   *  @param test Database column test SqlType(TINYTEXT), Length(255,true) */
  case class ModeRow(id: Int, test: String)
  /** GetResult implicit for fetching ModeRow objects using plain SQL queries */
  implicit def GetResultModeRow(implicit e0: GR[Int], e1: GR[String]): GR[ModeRow] = GR{
    prs => import prs._
    ModeRow.tupled((<<[Int], <<[String]))
  }
  /** Table description of table mode. Objects of this class serve as prototypes for rows in queries. */
  class Mode(_tableTag: Tag) extends profile.api.Table[ModeRow](_tableTag, Some("eggplant"), "mode") {
    def * = (id, test) <> (ModeRow.tupled, ModeRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), Rep.Some(test))).shaped.<>({r=>import r._; _1.map(_=> ModeRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column test SqlType(TINYTEXT), Length(255,true) */
    val test: Rep[String] = column[String]("test", O.Length(255,varying=true))
  }
  /** Collection-like TableQuery object for table Mode */
  lazy val Mode = new TableQuery(tag => new Mode(tag))
}
