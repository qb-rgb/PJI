/**
 * Represents a voter
 *
 * @constructor constructs a new voter
 * @param firstName first name of the voter
 * @param lastName last name of the voter
 * @param party political party of the voter
 *
 * @author Quentin Baert
 */
class Voter(val firstName: String, val lastName: String, val party: String) {

  override def toString: String =
    firstName + " " + lastName + " : " + party

}

/**
 * Represents an anonymous voter
 *
 * @constructor constructs a new anonymus voter
 * @param party political party of the anonymous voter
 */
class AnonymousVoter(override val party: String)
extends Voter("anonyme", "anonyme", party)
