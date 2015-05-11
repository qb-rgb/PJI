import scala.io.Source

/**
 * Object that manage the generation of the PDF URLs to download.
 *
 * @author Quentin Baert
 */
object URLManager {

  // Prefix of all the URLs
  private val prefix = "http://archives.assemblee-nationale.fr"

  // URLs of the index pages
  private val indexes = for (leg <- 1 to 11) yield (this.prefix + "/" + leg + "/cri/index.asp")

  // Loads the HTML page pointed by the URL in a string
  private def urlContentToString(url: String): String = {
    val html = Source.fromURL(url, "iso-8859-1")
    html.mkString
  }

  // Higher order function that catch an URL from a HTML line
  private def catchURL(
    from: String,
    linesFilter: (String) => (Boolean),
    urlMapping: (String) => (String)): List[String] = {

    val html = urlContentToString(from)
    val linesToKeep = (html split '\n') filter linesFilter

    (linesToKeep map urlMapping).toList
  }

  // Isolates a session URL
  private def catchSessionURL(line: String): String =
    line.substring((line indexOf "href=\"") + 6, (line indexOf "Compte rendu") - 2)

  // Generates all the session URLs from a index page
  private def sessionsURL(index: String): List[String] = {
    this.catchURL(
      index,
      {x: String => x contains "Compte rendu"},
      {x: String => this.prefix + this.catchSessionURL(x)})
  }

  // Isolates a PDF URLs
  private def catchPDFURL(line: String): String =
    line.substring((line indexOf "href=\"") + 6, (line indexOf ".pdf") + 4)

  // Generates all the PDF URLs from a session page
  private def pdfURL(session: String): List[String] = {
    this.catchURL(
      session,
      {x: String => x contains ".pdf"},
      {x: String => {
        val splitSession = session split '/'
        val leg = splitSession(3)

        this.prefix + "/" +
        leg +
        "/cri/" +
        this.catchPDFURL(x)
      }})
  }

  /**
   * List of all the PDF URLs to download
   */
  val pdfURLs = for {
    index <- this.indexes
    session <- this sessionsURL index
    pdf <- this pdfURL session
  } yield pdf

}
