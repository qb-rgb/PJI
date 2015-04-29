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
      (matcher group 1).replace("-\n", "").replace("\n", " ")
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

  // Builds the list of the named voters
  private def buildNamedList(votersString: String, group: String): List[Voter] =
    if (votersString.isEmpty)
      List()
    else {
      val punctuation = List('.', '/', ';', '\\', ':', ''', '"', '(', ')', '!',
                             '?', '_', '−')
      // Set of words to remove
      val toRemove = List("M", "MM", "Mme", "", "Assemblée", "Nationale",
                          "assemblée", "nationale", " ", "président",
                          "séance")
      // String without the polluting words
      val cleanVotersString =
        votersString.
        filterNot(punctuation.contains).
        split("[ \n]").
        filterNot(toRemove.contains).
        dropWhile(word => !(('A' to 'Z') contains word.head)).
        reverse.
        dropWhile(word => !(('A' to 'Z') contains word.head)).
        reverse.
        mkString(" ")

      // Names of the voters
      val votersNames =
        cleanVotersString.
        split("\\bet\\b|,").
        filter(name => name exists (('A' to 'Z') contains _))

      // Build a Voter object from a name
      def nameToVoter(name: String): Voter = {
        val splitName = name.trim split " "

        new Voter(splitName.head, splitName.tail mkString " ", group)
      }

      // List of named voters
      (votersNames map nameToVoter).toList
    }

  // Builds the list of the unnamed voters
  private def buildUnnamedList(namedNb: Int, nb: Int, group: String): List[Voter] =
    (for (i <- namedNb until nb) yield new AnonymousVoter(group)).toList

  // Builds the for and against voters list
  private def analyseForAgainstVotersString(
    votersString: String,
    nb: Int,
    group: String
  ): List[Voter] = {
    val named = this.buildNamedList(votersString, group)

    named ++ this.buildUnnamedList(named.size, nb, group)
  }

  // Analyses a group vote text
  private def groupAnalyse(group: String): List[(Voter, VoteDecision)] = {
    // Group Name
    val groupNameMatcher = PatternDictionnary.groupNamePattern matcher group
    val groupName =
      if (groupNameMatcher.find)
        groupNameMatcher group 1
      else "unknown"

    // Against
    val againstMatcher = PatternDictionnary.againstLinePattern matcher group
    val (againstNb, againstStringVoters) =
      if (againstMatcher.find)
        ((againstMatcher group 1).toInt, againstMatcher group 2)
      else (0, "")

    val againstVoters =
      this.analyseForAgainstVotersString(againstStringVoters, againstNb, groupName)
    val againstVotes = againstVoters map (v => v -> Against)

    // For
    val forMatcher = PatternDictionnary.forLinePattern matcher group
    val (forNb, forStringVoters) =
      if (forMatcher.find)
        ((forMatcher group 1).toInt, forMatcher group 2)
      else (0, "")
    val forVoters = this.analyseForAgainstVotersString(forStringVoters, forNb, groupName)
    val forVotes = forVoters map (v => v -> For)

    // Abstention
    val absMatcher = PatternDictionnary.abstentionLinePattern matcher group
    val (absNb, absStringVoters) =
      if (absMatcher.find)
        ((absMatcher group 1).toInt, absMatcher group 2)
      else (0, "")
    val absVoters = this.analyseForAgainstVotersString(absStringVoters, absNb, groupName)
    val absVotes = absVoters map (v => v -> Abstention)

    // Non-Voting
    val nonVotingMatcher = PatternDictionnary.nonVotingLinePattern matcher group
    val nonVotingStringVoters =
      if (nonVotingMatcher.find)
        nonVotingMatcher group 1
      else
        ""
    val nonVotingVoters = this.buildNamedList(nonVotingStringVoters, groupName)
    val nonVotingVotes = nonVotingVoters map (v => v -> NonVoting)

    forVotes ++ againstVotes ++ absVotes ++ nonVotingVotes
  }

  /**
   * Builds a vote from the vote text
   */
  def build: Vote = {
    val nb = this.getNb
    val subject = this.getSubject
    val nbOfVoters = this.getNbOfVoters
    val nbOfExpressedVotes = this.getNbOfExpressedVotes
    val absoluteMajority = this.getAbsoluteMajority
    val forNb = this.getForNb
    val againstNb = this.getAgainstNb
    val adopted = forNb >= absoluteMajority

    val groupsTexts =
      this.voteText.
      split("(?=Groupe)").
      filter(_ startsWith "Groupe")

    val voters = groupsTexts.foldLeft(List[(Voter, VoteDecision)]())(
      (acc: List[(Voter, VoteDecision)], group: String) =>
        acc ++ this.groupAnalyse(group)
    )

    new Vote(
      this.legislature,
      this.date,
      nb,
      subject,
      nbOfVoters,
      nbOfExpressedVotes,
      absoluteMajority,
      forNb,
      againstNb,
      adopted,
      voters
    )

  }

}
