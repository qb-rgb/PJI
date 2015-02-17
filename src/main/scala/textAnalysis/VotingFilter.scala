object VotingFilter {

  /**
   * List of all the local text paths
   */
  val allLocalTextPaths: List[String] =
    PDFConverter.allLocalPDFsPaths map (_.replace("pdf", "txt"))

  // Criterion from which filter the text files
  private def votingFilter(file: String): Boolean =
    (file contains "Annexe au procès-verbal") ||
    (file contains "Annexe au proces-verbal") ||
    (file contains "Annexe au proces verbal") ||
    (file contains "annexe au procès-verbal") ||
    (file contains "annexe au proces-verbal") ||
    (file contains "annexe au proces verbal")


}
