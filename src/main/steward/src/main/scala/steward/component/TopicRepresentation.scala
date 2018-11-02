package steward.component

import java.time._
import codes.reactive.scalatime._


//TODO prepare this for use with tests. Currently Low priority.
//TODO Documentation needed.
case class TopicRepresentation(id: Int, topicName: String, username: String,
                               private val dateCreatedInString: String,
                               private val lastUpdatedInString: String,
                               state: String) {
  final val dateCreated: LocalDate = TopicRepresentation.convertStringToLocalDate(dateCreatedInString)
  final val lastUpdated: LocalDate = TopicRepresentation.convertStringToLocalDate(lastUpdatedInString)
}

object TopicRepresentation {
  def convertStringToLocalDate(input: String): LocalDate = {
    val delimString = input.split("/")
    LocalDate.of(delimString(2).toInt, delimString(0).toInt, delimString(1).toInt)
  }
}
