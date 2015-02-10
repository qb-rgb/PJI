import java.io.File
import java.io.PrintWriter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

object PDFConverter {

  private def txtFilePath(path: String): String = {
    val pathWithoutPrefix = path substring ((path indexOf "cri/") + 4)
    val pathWithoutSuffix =
      pathWithoutPrefix.substring(0, (pathWithoutPrefix lastIndexOf '/') + 1)
    
    "./critxt/" + pathWithoutSuffix
  }

  def convert(path: String): Unit = {
    val pdfFile = new File(path)

    if (!pdfFile.isDirectory && pdfFile.exists) {
      // To read the PDF document
      val doc = PDDocument.load(pdfFile)
      val textStripper = new PDFTextStripper()
      val pdfText = textStripper.getText(doc)

      doc.close()

      // To write the text file
      val nameTxt = (path substring ((path lastIndexOf '/') + 1)).replace(".pdf", ".txt")
      val pathTxt = this.txtFilePath(path)
      val pathDirs = new File(pathTxt)

      if (!pathDirs.exists)
        pathDirs.mkdirs

      val pw = new PrintWriter(new File(pathTxt + nameTxt), "UTF-8")

      pw.write(pdfText)
      pw.close
    }
    else
      throw new IllegalArgumentException("File do not exists")
  }

  /**
   * Convert all the local PDF files to text files
   */
  def convertAll: Unit = {
    val urls = URLManager.pdfURLs

    def urlToLocalPath(url: String): String = {
      val name = url substring ((url lastIndexOf '/') + 1)

      PDFDownloader.pdfPath(url) + name
    }

    val localPath = urls map urlToLocalPath

    def convertOrPrint(path: String): Unit = try {
      this.convert(path)
      println("Converted : " + path)
    } catch {
      case e: Exception => println("File " + path + " not found. Conversion aborted.")
    }

    localPath foreach convertOrPrint
  }

}
