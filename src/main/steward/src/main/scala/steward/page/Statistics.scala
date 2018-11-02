package steward.page

import tools.infrastructure.browser.Session
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import steward.component.AllWebDriverTable
import org.openqa.selenium.NoSuchElementException
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//Done
//TODO: Add documentation with more methods.
class Statistics(protected val session: Session) extends StewardOutline {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "steward_statistics_query_topic_by_status_table" -> JSoupPresence .waitForSingle (How.TAG_NAME, "table"),
    "steward_statistics_stats_container" -> WebDriverVisibility .waitForSingle (How.CLASS_NAME, "stats-graph-container"),
    "steward_statistics_apply_range_button" -> WebDriverVisibility .waitForSingle (How.CSS, "button[type='submit']"),
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "steward_statistics_calendar_text_boxes" -> WebDriverVisibility .waitForMultiple (How.CSS, "input[type='text']"),
    "steward_statistics_calendar_buttons" -> WebDriverVisibility .waitForMultiple (How.CSS, "button[type='button']")
  )

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "steward_statistics_query_topic_by_status_table",
    "steward_statistics_stats_container", "steward_statistics_apply_range_button")

  final private val queryTopicByStatus: AllWebDriverTable = AllWebDriverTable(session, "steward_statistics_query_topic_by_status_table")

  def setStartDateTo(year: Int, month: Int, day: Int): Unit = {
    val startDateElement: DOMElement = "steward_statistics_calendar_text_boxes".<>.head
    startDateElement.clear()
    startDateElement.sendKeys("%2d/%2d/%4d".format(month, day, year))
  }

  def setEndDateTo(year: Int, month: Int, day: Int): Unit = {
    val endDateElement: DOMElement = "steward_statistics_calendar_text_boxes".<>.last
    endDateElement.clear()
    endDateElement.sendKeys("%2d/%2d/%4d".format(month, day, year))
  }

  def applyDateRange(): Unit = "steward_statistics_apply_range_button".click()

  def getQueryCountsByUser: mutable.Buffer[(String, Int)] = {
    "steward_statistics_stats_container".children.map(
      i =>(i.children.head.getTextFromWebDriver.split("\n").head, i.children.head.getTextFromWebDriver.split("\n")(1).split(" ").head.toInt))
  }

  private def getNumberOfTopicsWithStatus(status: String): Int = {
    val rowElement: DOMElement = queryTopicByStatus.bodyElement.findElement(ParserEngine.WEBDRIVER, How.LINK_TEXT, status).
      getOrElse(throw new NoSuchElementException(s"Failed to find the element associated with $status elements"))
    rowElement.firstSibling(ParserEngine.JSOUP).get.textFromJSoup.toInt
  }

  def getNumberOfRejectedTopics: Int = getNumberOfTopicsWithStatus("Rejected")

  def getNumberOfPendingTopics: Int = getNumberOfTopicsWithStatus("Pending")

  def getNumberOfApprovedTopics: Int = getNumberOfTopicsWithStatus("Approved")

  def getTotalNumberOfTopics: Int =
    queryTopicByStatus.footerElement.findElements(ParserEngine.WEBDRIVER, How.TAG_NAME, "td")(1).
      getTextFromWebDriver.toInt
}
