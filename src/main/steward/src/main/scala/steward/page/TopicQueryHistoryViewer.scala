package steward.page

import tools.infrastructure.browser.Session
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.wait.{Conditions, Wait}
import steward.component.{AllWebDriverTable, AngularPagination, TopicRepresentation}
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.immutable.HashMap
import scala.collection.mutable

case class TopicQueryHistoryViewer(protected val session: Session) extends TopicViewer {

  SINGLE_ELEMENT_LOCATOR_MAP ++: HashMap[String, SingleLocatingProcedure](
    "table" -> WebDriverVisibility .waitForSingle (How.TAG_NAME, "table"),
    "pagination" -> WebDriverVisibility   .waitForSingle (How.CLASS_NAME, "pagination-sm")
  )

  protected val table: AllWebDriverTable = AllWebDriverTable(session, "table")

  //Pagination for navigating the exposed data.
  protected val pagination: AngularPagination = AngularPagination(session, "pagination")

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
      bodyAsBufferOfElements.map(i =>
        Wait.until(Conditions.webElementsAreVisible(i, How.TAG_NAME, "td")))
    bodyAsTwoDimBufferOfElement.map(i => TopicRepresentation(i.head.getTextFromWebDriver.toInt,
      i(1).getTextFromWebDriver, i(2).getTextFromWebDriver, i(3).getTextFromWebDriver,
      i(4).getTextFromWebDriver, i(5).getTextFromWebDriver))
  }

  def viewTopicDetailsFromRowIdx(rowIdx: Int): TopicDescriptionViewer = {
    table.bodyElement.findElements(ParserEngine.JSOUP, How.LINK_TEXT, "view")(rowIdx).click()
    TopicDescriptionViewer(session)
  }

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = ???
}
