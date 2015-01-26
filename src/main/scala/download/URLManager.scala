import scala.io.Source

object URLManager {

  private val prefix = "http://archives.assemblee-nationale.fr"

  private val indexes = for (leg <- 1 to 11) yield (this.prefix + "/" + leg + "/cri/index.asp")

  private def urlContentToString(url: String): String = {
    val html = Source.fromURL(url, "iso-8859-1")
    html.mkString
  }

  private def catchURL(
    from: String,
    linesFilter: (String) => (Boolean),
    urlMapping: (String) => (String)): List[String] = {

    val html = urlContentToString(from)
    val linesToKeep = (html split '\n') filter linesFilter

    (linesToKeep map urlMapping).toList
  }

  private def catchSessionURL(line: String): String =
    line.substring((line indexOf "href=\"") + 6, (line indexOf "Compte rendu") - 2)

  private def sessionsURL(index: String): List[String] = {
    this.catchURL(
      index,
      {x: String => x contains "Compte rendu"}, 
      {x: String => this.prefix + this.catchSessionURL(x)})
  }

  private def catchPDFURL(line: String): String =
    line.substring((line indexOf "href=\"") + 6, (line indexOf ".pdf") + 4)

  private def pdfURL(session: String): List[String] = {
    this.catchURL(
      session,
      {x: String => x contains ".pdf"},
      {x: String => this.prefix + "/" + (session charAt (this.prefix.length + 1)) + "/cri/" + this.catchPDFURL(x)})
  }

  val pdfURLs = for {
    index <- this.indexes
    session <- this sessionsURL index
    pdf <- this pdfURL session
  } yield pdf

}
