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
    Pattern.compile("\\d{1,2}\\s+\\w+\\s+\\d{4}")

}
