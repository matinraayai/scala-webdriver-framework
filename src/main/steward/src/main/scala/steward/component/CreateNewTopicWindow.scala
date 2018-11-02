package steward.component

import tools.infrastructure.browser.Session
import tools.component.Page
import tools.capture.CaptureSchemes.WebDriverPresence
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

//Done for now.
//TODO: Documentation needed.
case class CreateNewTopicWindow(protected val session: Session) extends Page {

  override def waitUntilVitalElementsAreLoaded(): Unit = ???

  protected override lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "create_new_topic_topic_name_input" -> (WebDriverPresence waitForSingle (How.ID,"topicname")),
    "create_new_topic_topic_description_input" -> (WebDriverPresence waitForSingle (How.ID,"topicdescription")),
    "create_new_topic_submit_button" -> (WebDriverPresence waitForSingle (How.TAG_NAME, "button")),
    "create_new_topic_close_button" -> (WebDriverPresence waitForSingle (How.CLASS_NAME, "shrine_close"))
  )

  def closeWindow(): Unit = "create_new_topic_close_button".click()

  def enterTopicName(topicName: String) : Unit = "create_new_topic_topic_name_input".sendKeys(topicName)

  def enterDescription(description: String): Unit = "create_new_topic_description_input".sendKeys(description)

  def createTopic(): Unit = "create_new_topic_submit_button".click()
}