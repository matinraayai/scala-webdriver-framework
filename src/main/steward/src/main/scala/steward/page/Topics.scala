package steward.page

import tools.infrastructure.browser.{JavascriptCode, Session}
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.WebDriverPresence
import tools.capture.{SingleLocatingStep, _}
import tools.infrastructure.`trait`.DomElementLocator
import tools.infrastructure.wait.{Conditions, Wait}
import steward.component._
import tools.infrastructure.enums.How.How
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Represents the Topics Page in the Steward App.
  */
//TODO: Finalize Documentation
case class Topics(protected final val session: Session) extends StewardOutline {

  object ScrollToBeginning extends CaptureScheme {

    override def waitForSingle(how: How, locator: String) = {
      val step: SingleLocatingStep = SingleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) => {
        session.executeScript(JavascriptCode.SCROLL_TO_BEGINNING_OF_DOCUMENT)
        Wait.until(Conditions.webElementIsVisible(elementFinder, how, step.getLocatorString))
      }
      step.setRunnable(runnable)
      step
    }

    override def waitForMultiple(how: How, locator: String)= ???

    override def captureSingle(how: How, locator: String) = ???

    override def captureMultiple(how: How, locator: String): MultipleLocatingStep = ???
  }

  object ScrollToEnd extends CaptureScheme {
    override def waitForSingle(how: How, locator: String)= {
      val step: SingleLocatingStep = SingleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) => {
        session.executeScript(JavascriptCode.SCROLL_TO_END_OF_DOCUMENT)
        Wait.until(Conditions.webElementIsVisible(elementFinder, how, step.getLocatorString))
      }
      step.setRunnable(runnable)
      step
    }

    override def waitForMultiple(how: How, locator: String) = ???

    override def captureSingle(how: How, locator: String) = ???

    override def captureMultiple(how: How, locator: String) = ???
  }

  SINGLE_ELEMENT_LOCATOR_MAP ++: TrieMap[String, SingleLocatingProcedure](
      "steward_topics_tab_table" -> (ScrollToBeginning waitForSingle (How.TAG_NAME,"table")),
      "steward_topics_tab_pagination" -> (ScrollToEnd waitForSingle (How.CLASS_NAME, "pagination-sm")),
      "steward_topics_tab_page_number_text" -> (ScrollToEnd waitForSingle (How.CSS, "#page-wrapper > div > div > div > div > div > div > div > div")),
      "steward_topics_tab_topic_status_tab" -> (ScrollToBeginning waitForSingle (How.CLASS_NAME, "col-sm-12")))

  final override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "steward_home_topic_status_button" -> WebDriverPresence .waitForMultiple (How.CSS, "#page-wrapper > div > div > div > div > div > span > div > div > button.btn.btn-default.shrine-btn-default.shrine-btn-on"),
    "steward_home_currently_shown_topic" -> "steward_topics_tab_topic_status_tab" .--> (WebDriverPresence .waitForMultiple (How.CLASS_NAME, "btn btn-default shrine-btn-default shrine-btn-on")))

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit =
    waitUntilElementsAreLoaded("steward_topics_tab_table",
      "steward_topics_tab_pagination",
      "steward_topics_tab_topic_status_tab")

  protected val table: AllWebDriverTable = AllWebDriverTable(session, "steward_topics_tab_table")

  //Pagination for navigating the exposed data.
  protected val pagination: AngularPagination = AngularPagination(session, "steward_topics_tab_pagination")

  def getHeaderContentAsStringBuffer: mutable.Buffer[String] =
    table.getHeaderRow(0).map(_.getTextFromWebDriver)


  def changeSortStatus(by: String): Unit =
    Wait.until(Conditions.webElementIsVisible(table.headerElement, How.LINK_TEXT, by)).click()

  //TODO: Rewrite this. It's too slow.
  def getSortStatus: (String, String) = {
    var sortState: (String, String) = ("", "")
    val tableHeaderTextContents: mutable.Buffer[String] = getHeaderContentAsStringBuffer
    val sortTokens: mutable.Buffer[DOMElement] =
      Wait.until(Conditions.webElementsAreVisible(table.headerElement, How.TAG_NAME, "span"))
    var i: Int = 0
    for(element <- sortTokens) {
      if (!element.getAttributeFromWebDriver("class").contains("ng-hide")) {
        if(!element.getAttributeFromWebDriver("class").contains("down")) {
          sortState = (tableHeaderTextContents(i), "Ascending")
        }
        else {
          sortState = (tableHeaderTextContents(i), "Descending")
        }
      }
      i = i + 1
    }
    sortState
  }

  def getTableBodyRowCount: Int = table.getBodyRowCount

  /**
    * @return The contents of the table as a Buffer of `TopicRepresentation`
    */
  def getCurrentTableContents: mutable.Buffer[TopicRepresentation] = {
    val bodyAsSingleElement: DOMElement = table.bodyElement
    val bodyAsBufferOfElements: mutable.Buffer[DOMElement] =
      Wait.until(Conditions.webElementsAreVisible(bodyAsSingleElement, How.TAG_NAME, "tr"))
    val bodyAsTwoDimBufferOfElement: mutable.Buffer[mutable.Buffer[DOMElement]] =
      bodyAsBufferOfElements.map(i => Wait.until(Conditions.webElementsAreVisible(i, How.TAG_NAME, "td")))
    bodyAsTwoDimBufferOfElement.map(i => TopicRepresentation(i.head.getTextFromWebDriver.toInt,
      i(1).getTextFromWebDriver, i(2).getTextFromWebDriver, i(3).getTextFromWebDriver,
      i(4).getTextFromWebDriver, i(5).getTextFromWebDriver))
  }

  def viewTopicDetailsFromRowIdx(rowIdx: Int): TopicDescriptionViewer = {
    table.bodyElement.findElements(ParserEngine.JSOUP, How.LINK_TEXT, "view")(rowIdx).click()
    TopicDescriptionViewer(session)
  }

  def pressCreateNewTopic(): CreateNewTopicWindow = {
    table.footerElement.findElement(ParserEngine.JSOUP, How.TAG_NAME, "button").get.click()
    CreateNewTopicWindow(session)
  }

  def getCurrentPageNumberFromPaginationStatus: Int =
    "steward_topics_tab_page_number_text".textFromJSoup.split(": ")(1).split(" / ")(0).toInt

  def getTotalNumberOfPagesFromPaginationStatus: Int = "steward_topics_tab_page_number_text".textFromJSoup.split(": ")(1).split(" / ")(1).toInt

  def switchTopicStatus(topicStatus: String): Unit = "steward_topics_tab_topic_status_tab".
    findElement(ParserEngine.JSOUP, How.LINK_TEXT, topicStatus).
      getOrElse(throw new NoSuchElementException(s"Failed to find the desired topic status named: $topicStatus")).click()

  def getCurrentlyShownTopicStatus: String = "steward_home_currently_shown_topic".textFromJSoup
}