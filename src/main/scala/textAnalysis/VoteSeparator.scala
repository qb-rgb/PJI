import java.util.Date
import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * Extracts vote from a text
 *
 * @constructor creates a new VoteSeparator object from a text
 * @param text text from which extract the votes
 *
 * @author Quentin Baert
 */
class VoteSeparator(val text: String) {

  /**
   * Text area where the votes are
   */
  val votesTextArea: String = {
    val matcher =
      PatternDictionnary.voteTextZoneDelimiterPattern.matcher(this.text)

    matcher.find

    val index = matcher.start

    this.text substring index
  }

  /**
   * Date of the votes
   */
  val date: String = {
    val matcher =
      PatternDictionnary.frenchDatePattern matcher this.votesTextArea

    if (matcher.find)
      matcher.group
    else
      "unknown"
  }

  /**
   * List which contains votes text
   */
  val votesTexts: List[String] = {
    // Finds all the votes indexes
    def buildIndexesList(matcher: Matcher, res: List[Int]): List[Int] =
      if (!matcher.find)
        res
      else
        buildIndexesList(matcher, res :+ matcher.start)

    // Build a list with all the votes in
    def buildResList(indexes: List[Int], res: List[String]): List[String] =
      indexes match {
        case Nil => res
        case x :: Nil => (this.votesTextArea substring x) :: res
        case x :: y => {
          val newElem = this.votesTextArea.substring(x, y.head)
          buildResList(y, res :+ newElem)
        }
      }

    val matcher =
      PatternDictionnary.voteDelimiterPattern matcher this.votesTextArea
    val indexes = buildIndexesList(matcher, Nil)

    buildResList(indexes, Nil)
  }

}
