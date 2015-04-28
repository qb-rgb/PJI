import scala.io.Source
import java.io.PrintWriter
import java.io.File

object CSVBuilder {

  // Modifies a vote path to a csv path
  private def modifyPath(oldPath: String): (String, String) = {
    val fields = oldPath split "/"
    val fileName = fields.last.replace("txt", "csv")
    val usefullFields = fields.tail.init.toList

    (("csv" :: usefullFields) mkString "/", fileName)
  }

  /**
   * Builds a CSV file from a vote text file
   *
   * @param path path of the vote text file
   */
  def buildCSVFileFrom(path: String, legislature: Int): Unit = {
    val text = (Source fromFile path).mkString
    val vs = new VoteSeparator(text)
    val votes = vs.votesTexts map (new VoteBuilder(_, legislature, vs.date).build)
    val csv = (votes map (_.toCSV)) mkString "\n"
    val (newPath, newFileName) = this modifyPath path
    val dirs = new File(newPath)

    if (!dirs.exists)
      dirs.mkdirs

    val pw = new PrintWriter(new File(newPath + "/" + newFileName))

    pw write csv

    pw.close
  }

}
