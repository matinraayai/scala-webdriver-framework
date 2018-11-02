package steward.enums

object TopicStatus extends Enumeration {
  type TopicStatus = Value
  val PENDING, APPROVED, REJECTED = Value
}