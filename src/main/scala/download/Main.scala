object Main {

  private def printAndDownload(pdfURL: String): Unit = {
    PDFDownloader.downloadPDF(pdfURL)
    println("Downloaded : " + pdfURL)
  }

  def main(args: Array[String]): Unit = {
    val groupNb = args(0).toInt
    val sortedURLs = URLManager.pdfURLs sortWith (_ < _)
    val groupedURLs = (sortedURLs grouped 100).toList

    groupedURLs(groupNb) foreach printAndDownload
  }

}
