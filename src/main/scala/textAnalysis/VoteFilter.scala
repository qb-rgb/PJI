import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

import scala.io.Source

object VoteFilter {

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

  /**
   * Filters all the local text files to find the voting ones
   */
  def filterAllTxtFiles: Unit = {

    // Determines if a file is a voting file or not
    def isScrutinPath(path: String): Boolean = {
      val source = Source.fromFile(path)("UTF-8")
      val res =
        (PatternDictionnary.voteTextZoneDelimiterPattern matcher source.mkString).find
      source.close
      res
    }

    // Change the local .txt file path to an other
    def txtPathToScrutinPath(path: String): String =
      path.replace("critxt", "scrutins")

    // Copies a file in an other directory (local critxt folder to local scrutin folder)
    def cpTxtFile(path: String): Unit = {
      val newPath = txtPathToScrutinPath(path)
      val newPathWithoutFile = newPath.substring(0, (newPath lastIndexOf '/'))
      val newPathDirs = new File(newPathWithoutFile)

      if (!newPathDirs.exists)
        newPathDirs.mkdirs

      val oldFile = new File(path)
      val newFile = new File(newPath)

      (new FileOutputStream(newFile).getChannel).transferFrom(new FileInputStream(oldFile).getChannel, 0, Long.MaxValue)
      println(path + " => " + newPath)
    }

    val scrutinTxtPaths = this.allLocalTextPaths filter isScrutinPath

    scrutinTxtPaths foreach cpTxtFile

  }

}
