object Main {

  private def printAndDownload(pdfURL: String): Unit = {
    PDFDownloader.downloadPDF(pdfURL)
    println("Downloaded : " + pdfURL)
  }

  def main(args: Array[String]): Unit = {
    URLManager.pdfURLs foreach printAndDownload
  }

}
