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
      if (closeable != null)
        closeable.close()
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
    } finally {
      closeQuietly(stream)
    }
  }

  private def readStreamAsString(stream: InputStream)(implicit codec: Codec): String = {
    new String(readStream(stream), codec.name)
  }

  /**
    * Read evolution files from the application environment.
    * egs:
    * - 001-user.sql     - OK
    * - 2-role.sql       - OK
    * - 003-problem.sql  - CONFLICT with 003-contest.sql
    * - 003-contest.sql  - CONFLICT with 003-problem.sql
    * - 005-status.sql   - SKIPPED due to previous failure
    * - t06-perms.sql    - IGNORED due to unknown pattern
    */
  def loadResource(db: String, revision: Int): Option[InputStream] = {
    val ptn: Regex = s"^0*$revision(-[^.]+)?\\.sql$$".r

    environment.getExistingFile(Evolutions.directoryName(db))
      .flatMap { dir =>
        dir.list().toList.filter { filename =>
          ptn.findFirstIn(filename).isDefined
        } match {
          case Nil => None // has no more evolutions
          case filename :: Nil => Some(filename) // has the next evolution
          case filenames => // has a conflict between evolutions
            val str = filenames.map(str => s""""$str"""").mkString(", ")
            throw new AssertionError(s"CONFLICT: more than one $revision exists. There are $str.")
        }
      }
      .map(filename => s"${Evolutions.directoryName(db)}/$filename")
      .flatMap(environment.getExistingFile)
      .map(file => java.nio.file.Files.newInputStream(file.toPath))
  }

  def evolutions(db: String): Seq[Evolution] = {
    val upsMarker = """^--\s*!Ups\s*$""".r
    val downsMarker = """^--\s*!Downs\s*$""".r

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
        val script = readStreamAsString(stream)(Codec.UTF8)
        (revision + 1, (revision, script))
      }
    }.sortBy(_._1).map { case (revision, script) =>
      val parsed = Collections.unfoldLeft("" -> script.split('\n').toList.map(_.trim)) {
        case (_, Nil) => None
        case (upOrDown, below) =>
          val (lines, rest) = below.span(line => !isMarker(line))
          val nextLoop = rest.headOption match {
            case Some(marker) => mapUpsAndDowns(marker) -> rest.tail
            case None => "" -> Nil
          }
          Some((nextLoop, upOrDown -> lines.mkString("\n")))
      }.reverse.drop(1).groupBy(_._1).mapValues { tuples =>
        tuples.map(_._2).mkString("\n").trim
      }

      Evolution(
        revision,
        parsed.getOrElse(UPS, ""),
        parsed.getOrElse(DOWNS, ""))
    }
  }
}
