/**
 * Represents a vote decision
 *
 * @author Quentin Baert
 */
object VoteDecision extends Enumeration {

  type VoteDecision = Value

  val For = Value
  val Against = Value
  val Abstention = Value

}
