import java.util.regex.Pattern

/**
 * Object to stock all necessary pattern to work on votes
 *
 * @author Quentin Baert
 */
object PatternDictionnary {

  /**
   * Pattern that determines if a text contains votes or not
   */
  val voteTextZoneDelimiterPattern: Pattern =
    Pattern.compile(
      "annexes au proc[eèé]s[\\s*\\-]verbal",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern that determines delimiters between votes in a file
   */
  val voteDelimiterPattern: Pattern =
    Pattern.compile(
      "scrutin\\s*\\(n[o°\\s+]\\s*(\\d+)\\)",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern that reprsents a french date
   */
  val frenchDatePattern: Pattern =
    Pattern compile "\\d{1,2}\\s+\\w+\\s+\\d{4}"

  /**
   * Pattern to find a vote subject
   */
  val subjectPattern: Pattern =
    Pattern.compile("(sur.+?\\.)\\n", Pattern.DOTALL)

  // Build string to capture recap number
  private def buildRecapString(recap: String): String =
    recap.replace(" ", "\\s+") + "\\s+\\.+\\s+(\\d+)"

  /**
   * Pattern to find the number of voters
   */
  val nbOfVotersPattern: Pattern =
    Pattern.compile(
      this buildRecapString "Nombre de votants",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern to find the number of expressed votes
   */
  val nbOfExpressedVotesPattern: Pattern =
    Pattern.compile(
      this buildRecapString "Nombre de suffrages exprim[ée]s",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern to find the absolute majority
   */
  val absoluteMajorityPattern: Pattern =
    Pattern.compile(
      this buildRecapString "Majorit[ée] absolue",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern to find the for votes number
   */
  val forNbPattern: Pattern =
    Pattern.compile(
      this buildRecapString "Pour l’adoption",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )

  /**
   * Pattern to find the against votes number
   */
  val againstNbPattern: Pattern =
    Pattern.compile(
      this buildRecapString "Contre",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    )


}
