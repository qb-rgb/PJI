/**
 * Represents a vote decision
 *
 * @author Quentin Baert
 */
trait VoteDecision

object For extends VoteDecision {

  override def toString: String = "pour"

}

object Against extends VoteDecision {

  override def toString: String = "contre"

}

object Abstention extends VoteDecision {

  override def toString: String = "abstention"

}

object NonVoting extends VoteDecision {

  override def toString: String = "non-votant"

}
