/**
 * Main object, run the main program.
 * This program allow to download the PDF one hundred by one hundred.
 *
 * @author Quentin Baert
 */
object Main {

  // Downloads the PDF pointed by the URL and print a trace
  private def printAndDownload(pdfURL: String): Unit = {
    PDFDownloader.downloadPDF(pdfURL)
    println("Downloaded : " + pdfURL)
  }

  /**
   * Main method
   *
   * The PDF URLs are sorted and grouped by 100.
   * In this way, the program take a number n and download the nth groupe of 100 PDFs.
   */
  def main(args: Array[String]): Unit = {
    val groupNb = args(0).toInt
    val sortedURLs = URLManager.pdfURLs sortWith (_ < _)
    val groupedURLs = (sortedURLs grouped 100).toList

    groupedURLs(groupNb) foreach printAndDownload
  }

}
