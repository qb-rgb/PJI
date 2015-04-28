import java.util.regex.Pattern
import java.util.regex.Matcher

/**
 * Creates Vote objects from a vote text
 *
 * @constructor constructs a VoteBuilder
 * @param voteText text of the vote
 *
 * @author Quentin Baert
 */
class VoteBuilder(val voteText: String, val legislature: Int, val date: String) {

  // Build a matcher from the vote text
  private def buildMatcher(pattern: Pattern): Matcher =
    pattern matcher this.voteText

  // Gives the number of the vote
  private def getNb: Int = {
    val matcher = this buildMatcher PatternDictionnary.voteDelimiterPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  // Gives the subject of the vote
  private def getSubject: String = {
    val matcher = this buildMatcher PatternDictionnary.subjectPattern

    if (matcher.find)
      matcher group 1
    else
      "unknown"
  }

  // Gives the number of voters of the vote
  private def getNbOfVoters: Int = {
    val matcher = this buildMatcher PatternDictionnary.nbOfVotersPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  // Get the number of expressed votes
  private def getNbOfExpressedVotes: Int = {
    val matcher = this buildMatcher PatternDictionnary.nbOfExpressedVotesPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  // Gives the absolute majority
  private def getAbsoluteMajority: Int = {
    val matcher = this buildMatcher PatternDictionnary.absoluteMajorityPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  // Gives the number of votes which are "For"
  private def getForNb: Int = {
    val matcher = this buildMatcher PatternDictionnary.forNbPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  // Gives the number of votes which are "Against"
  private def getAgainstNb: Int = {
    val matcher = this buildMatcher PatternDictionnary.againstNbPattern

    if (matcher.find)
      (matcher group 1).toInt
    else
      -1
  }

  def build: Unit = {
    val nb = this.getNb
    val subject = this.getSubject
    val nbOfVoters = this.getNbOfVoters
    val nbOfExpressedVotes = this.getNbOfExpressedVotes
    val absoluteMajority = this.getAbsoluteMajority
    val forNb = this.getForNb
    val againstNb = this.getAgainstNb
    val adopted = forNb >= absoluteMajority
    println(nb)
    println(subject)
    println(nbOfVoters)
    println(nbOfExpressedVotes)
    println(absoluteMajority)
    println(forNb)
    println(againstNb)
    println(adopted)
  }

}
