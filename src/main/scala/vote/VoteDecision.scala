/**
 * Represents a vote decision
 *
 * @author Quentin Baert
 */
trait VoteDecision

object For extends VoteDecision {

  override def toString: String = "For"

}

object Against extends VoteDecision {

  override def toString: String = "Against"

}

object Abstention extends VoteDecision {

  override def toString = "Abstention"

}
