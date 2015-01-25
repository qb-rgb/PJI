import sys.process._
import java.net.URL
import java.io.File

object PDFDownloader {

  private def pdfPath(url: String): String = {
    val parts: List[String] = (url split '/').toList
    val legislature = parts(3)
    val years = parts(5).substring(0, parts(5) lastIndexOf '-')

    "./cri/" + legislature + "/" + years + "/"
  }
  
  def downloadPDF(url: String): Unit = {
    val name = url substring ((url lastIndexOf '/') + 1)  
    val path = this.pdfPath(url)
    val pathDirs = new File(path)

    if (!pathDirs.exists)
      pathDirs.mkdirs

    new URL(url) #> new File(path + name) !!
  }

}
