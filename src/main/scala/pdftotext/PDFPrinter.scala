import java.io.File
import java.io.PrintStream

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper

object PDFPrinter {

  def print(path: String): Unit = {
    val file = new File(path)
    val doc = PDDocument.load(file)
    val textStripper = new PDFTextStripper()
    val p = new PrintStream(System.out, false, "UTF-8");

    p.println(textStripper.getText(doc))

    doc.close
  }

  def main(args: Array[String]): Unit = {
    val path = args(0)

    PDFPrinter.print(path)
  }

}
