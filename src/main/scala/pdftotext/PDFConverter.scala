import java.io.File
import java.io.PrintWriter

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

object PDFConverter {

  def txtFilePath(path: String): String = {
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

}
