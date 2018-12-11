package doj.util

import com.github.tminglei.slickpg._
import io.circe.{Json, parser => jsonParser}
import slick.basic.Capability
import slick.jdbc.JdbcCapabilities

trait MyPostgresProfile extends ExPostgresProfile
  with PgArraySupport
  with PgDate2Support
  with PgRangeSupport
  with PgHStoreSupport
  with PgCirceJsonSupport
  with PgSearchSupport
  with PgNetSupport
  with PgLTreeSupport {

  override val pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[Capability] =
    super.computeCapabilities + JdbcCapabilities.insertOrUpdate

  override val api: MyAPI.type = MyAPI

  object MyAPI extends API
    with ArrayImplicits
    with DateTimeImplicits
    with JsonImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {

    implicit val strListTypeMapper: DriverJdbcType[List[String]] =
      new SimpleArrayJdbcType[String]("text").to(_.toList)

    implicit val jsonArrayTypeMapper: DriverJdbcType[List[Json]] =
      new AdvancedArrayJdbcType[Json](
        pgjson,
        (s: String) => utils.SimpleArrayUtils.fromString[Json](jsonParser.parse(_).getOrElse(Json.Null))(s).orNull,
        (v: Seq[Json]) => utils.SimpleArrayUtils.mkString[Json](_.toString)(v)
      ).to(_.toList)

  }

}

object MyPostgresProfile extends MyPostgresProfile
