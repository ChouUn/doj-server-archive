package doj.play

import java.io._

import javax.inject._
import play.api.db.evolutions._
import play.api.libs.Collections
import play.api.{Environment, Logger}

import scala.io.Codec
import scala.util.matching.Regex

@Singleton
class MyEvolutionsReader @Inject()(environment: Environment) extends EvolutionsReader {
  private def closeQuietly(closeable: Closeable): Unit = {
    try {
      if (closeable != null) {
        closeable.close()
      }
    } catch {
      case e: IOException => Logger.warn("Error closing stream", e)
    }
  }

  private def readStream(stream: InputStream): Array[Byte] = {
    try {
      val buffer = new Array[Byte](8192)
      var len = stream.read(buffer)
      val out = new ByteArrayOutputStream() // Doesn't need closing
      while (len != -1) {
        out.write(buffer, 0, len)
        len = stream.read(buffer)
      }
      out.toByteArray
    } finally closeQuietly(stream)
  }

  private def readStreamAsString(stream: InputStream)(implicit codec: Codec): String = {
    new String(readStream(stream), codec.name)
  }

  /**
    * Read evolution files from the application environment.
    * egs:
    * - 001-user.sql
    * - 002-problem.sql
    */
  def loadResource(db: String, revision: Int): Option[InputStream] = {
    val ptn: Regex = s"^0*$revision(-[^.]+)?\\.sql$$".r

    val dir: Option[File] = environment.getExistingFile(Evolutions.directoryName(db))

    dir
      .flatMap {
        _.list().filter {
          ptn.findFirstIn(_).isDefined
        } match {
          case Array(only) => Some(only)
          case _ => None
        }
      }
      .map(filename => s"${Evolutions.directoryName(db)}/$filename")
      .flatMap(environment.getExistingFile)
      .map(f => java.nio.file.Files.newInputStream(f.toPath))
  }

  def evolutions(db: String): Seq[Evolution] = {
    val upsMarker = """^--\s*!Ups.*$""".r
    val downsMarker = """^--\s*!Downs.*$""".r

    val UPS = "UPS"
    val DOWNS = "DOWNS"
    val UNKNOWN = "UNKNOWN"

    val mapUpsAndDowns: PartialFunction[String, String] = {
      case upsMarker() => UPS
      case downsMarker() => DOWNS
      case _ => UNKNOWN
    }

    val isMarker: PartialFunction[String, Boolean] = {
      case upsMarker() => true
      case downsMarker() => true
      case _ => false
    }

    Collections.unfoldLeft(1) { revision =>
      loadResource(db, revision).map { stream =>
        (revision + 1, (revision, readStreamAsString(stream)(Codec.UTF8)))
      }
    }.sortBy(_._1).map {
      case (revision, script) =>
        val parsed = Collections.unfoldLeft(("", script.split('\n').toList.map(_.trim))) {
          case (_, Nil) => None
          case (context, lines) =>
            val (some, next) = lines.span(l => !isMarker(l))
            Some((next.headOption.map(c => (mapUpsAndDowns(c), next.tail)).getOrElse("" -> Nil),
              context -> some.mkString("\n")))
        }.reverse.drop(1).groupBy(i => i._1).mapValues {
          _.map(_._2).mkString("\n").trim
        }

        Evolution(
          revision,
          parsed.getOrElse(UPS, ""),
          parsed.getOrElse(DOWNS, ""))
    }
  }
}
