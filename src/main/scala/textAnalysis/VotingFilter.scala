import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

import scala.io.Source

object VotingFilter {

  /**
   * List of all the local text paths
   */
  val allLocalTextPaths: List[String] = {
    def findTxtFiles(root: File): List[String] = 
      if (root.isFile)
        if (root.getName endsWith "txt")
          List(root.getCanonicalPath)
        else
          List()
      else
        (root.listFiles).toList flatMap findTxtFiles

    val root = new File("./critxt")

    findTxtFiles(root)
  }
  
  // Criterion from which filter the text files
  private def votingFilter(file: String): Boolean =
    (file contains "Annexe au procès-verbal") ||
    (file contains "Annexe au proces-verbal") ||
    (file contains "Annexe au proces verbal") ||
    (file contains "annexe au procès-verbal") ||
    (file contains "annexe au proces-verbal") ||
    (file contains "annexe au proces verbal")


}
