import sys.process._
import java.net.URL
import java.io.File

/**
 * Object to download a PDF from the french National Assembly website :
 * http://archives.assemblee-nationale.fr/X/cri
 * (with X the legislature number between 1 and 11)
 *
 * @author Quentin Baert
 */
object PDFDownloader {

  // Gives the local path of the PDF from its URL
  private def pdfPath(url: String): String = {
    val parts: List[String] = (url split '/').toList
    val legislature = parts(3)
    val part = parts(5)
    val years = part.substring(0, part lastIndexOf '-')
    val kind = part.substring((part lastIndexOf '-') + 1, part.length)

    "./cri/" + legislature + "/" + years + "/" + kind + "/"
  }
  
  /**
   * Downloads a PDF from the given URL
   *
   * @param url
   *          URL of the PDF
   */
  def downloadPDF(url: String): Unit = {
    val name = url substring ((url lastIndexOf '/') + 1)  
    val path = this.pdfPath(url)
    val pathDirs = new File(path)
    val pdfURL = new URL(url)

    if (pdfURL.openConnection.getContentLength != -1) {
      if (!pathDirs.exists)
        pathDirs.mkdirs

      pdfURL #> new File(path + name) !!
    }
  }

}
