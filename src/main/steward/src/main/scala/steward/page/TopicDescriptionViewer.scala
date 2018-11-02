package steward.page

import tools.infrastructure.browser.Session
import tools.component.SelectDropDown
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How

import scala.collection.immutable.HashMap


//TODO: Ask James about the text fields and how to fetch them without relying too much on CSS.
//TODO: Add Documentation.
case class TopicDescriptionViewer(protected val session: Session) extends TopicViewer {

  SINGLE_ELEMENT_LOCATOR_MAP ++: HashMap[String, SingleLocatingProcedure](
    "topic_viewer_edit_button" -> WebDriverVisibility .waitForSingle (How.CSS, "button[type='select']"),
    "topic_viewer_name_text_box"-> WebDriverVisibility .waitForSingle (How.ID, "topicname"),
    "topic_viewer_description_text_box" -> WebDriverVisibility .waitForSingle (How.ID, "topicdescription"),
    "topic_viewer_status_dropdown" -> WebDriverVisibility .waitForSingle (How.CSS, "select[ng-model='detail.topicState']"),
    "topic_viewer_edit_topic" -> WebDriverVisibility .waitForSingle (How.CSS, "button[type='submit']"),
    "topic_viewer_description_tab_topic_id" -> JSoupPresence .waitForSingle (How.CSS, "form > div > div:nth-child(2)"),
    "topic_viewer_description_tab_date_created_text" -> JSoupPresence .waitForSingle (How.CSS, "form > div > div:nth-child(3)"),
    "topic_viewer_description_tab_date_updated_text" -> JSoupPresence .waitForSingle (How.CSS, "form > div > div:nth-child(4)"),
    "topic_viewer_description_tab_status_text" -> JSoupPresence .waitForSingle (How.CSS, "form > div > div:nth-child(5)"),
    "topic_viewer_description_tab_topic_name_text" -> JSoupPresence .waitForSingle (How.CSS, "body > div.modal.fade.ng-isolate-scope.in > div > div > div > div > div:nth-child(3) > form > div > div:nth-child(7) > label"),
    "topic_viewer_description_tab_topic_name_text" -> JSoupPresence .waitForSingle (How.CSS, "body > div.modal.fade.ng-isolate-scope.in > div > div > div > div > div:nth-child(3) > form > div > div:nth-child(8) > label"),
    "topic_viewer_description_tab_required_field_text" -> JSoupPresence .waitForSingle (How.CSS, "body > div.modal.fade.ng-isolate-scope.in > div > div > div > div > div:nth-child(3) > form > div > label")
  )

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "topic_viewer_name_text_box",
    "topic_viewer_description_text_box",
    "topic_viewer_edit_button"
  )

  private val topicStatusDropDown: SelectDropDown = SelectDropDown(session, "topic_viewer_status_dropdown")

  def getTopicID: Int = "topic_viewer_description_tab_topic_id".textFromJSoup.split(": ")(1).toInt

  def getDateCreated: String = "topic_viewer_description_tab_date_created_text".textFromJSoup.split(": ")(1)

  def getDateUpdated: String = "topic_viewer_description_tab_date_updated_text".textFromJSoup.split(": ")(1)

  def getTopicStatus: String = "topic_viewer_description_tab_status_text".textFromJSoup.split(": ")(1)

  def getTopicNameText: String = "topic_viewer_description_tab_topic_name_text".textFromJSoup

  def getTopicIntentText: String = "topic_viewer_description_tab_topic_name_text".textFromJSoup

  def getRequiredFieldText: String = "topic_viewer_description_tab_required_field_text".textFromJSoup

  def getTopicNameTextBoxContent: String = "topic_viewer_name_text_box".getTextFromWebDriver

  def setTopicNameField(content: String): Unit = {
    val topicNameTextBoxElement: DOMElement = "topic_viewer_name_text_box"
    topicNameTextBoxElement.clear()
    topicNameTextBoxElement.sendKeys(content)
  }

  def getDescription: String = "topic_viewer_description_text_box".getTextFromWebDriver

  def setDescription(content: String): Unit = {
    val topicDescriptionTextBox: DOMElement = "topic_viewer_description_text_box"
    topicDescriptionTextBox.clear()
    topicDescriptionTextBox.sendKeys(content)
  }

  def enableTopicEdit(): Unit = "topic_viewer_edit_button".click()

  def setTopicStatus(topicStatus: String): Unit = topicStatusDropDown.selectByValue(topicStatus)

  def getCurrentlySelectedTopicStatusInDropDown: String =
    topicStatusDropDown.getFirstSelectedOption.getTextFromWebDriver
}