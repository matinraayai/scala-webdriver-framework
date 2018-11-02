package steward.component

import tools.infrastructure.browser.{JavascriptCode, Session}
import tools.component.table.Table
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.wait.{Conditions, Wait}
import org.openqa.selenium.WebDriverException
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable


/**
  * A simple abstraction over tables in web applications, using all dynamic waits to retrieve its
  * state.
  * @param componentName Name of the element stored in the Page object creating this element.
  * @param session The session object responsible for creating this abstraction.
  */
//TODO: Documentation needed.
case class AllWebDriverTable(protected val session: Session,
                             protected val parentElementProcedure: SingleLocatingProcedure) extends Table {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "header_element" -> (WebDriverVisibility waitForSingle (How.TAG_NAME, "thead")),
    "body_element" -> (WebDriverVisibility waitForSingle (How.TAG_NAME, "tbody")),
    "footer_element" -> (WebDriverVisibility waitForSingle (How.TAG_NAME, "tfoot"))
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "header_element_buffer" -> "header_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr")),
    "body_element_buffer" -> "body_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr")),
    "footer_element_buffer" -> "footer_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr"))
  )

  /**
    * @return Header of the table as a single `ShrineElement` using captureTableElement.
    * @throws WebDriverException if for some reason, the header is not present in the table
    *                                or if table capturing was unsuccessful.
    */
  def headerElement: DOMElement = "header_element"

  /**
    * @return Body of the table as a single `ShrineElement` using captureTableElement.
    * @throws WebDriverException if for some reason, the body is not present in the table
    *                                or if table capturing was unsuccessful.
    */
  def bodyElement: DOMElement = "body_element"

  /**
    * @return Footer of the table as a single `ShrineElement` using captureTableElement.
    * @throws WebDriverException if for some reason, the footer is not present in the table
    *                                or if table capturing was unsuccessful.
    */
  def footerElement: DOMElement = "footer_element"

  //TODO: Expand the locator "relationship" to also cover these scenarios as well.
  def getHeaderRow(rowIdx: Int): mutable.Buffer[DOMElement] =
    Wait.until(Conditions.webElementsAreVisible("header_element_buffer".<>(rowIdx), How.TAG_NAME, "td"))

  def getBodyRow(rowIdx: Int): mutable.Buffer[DOMElement] =
    Wait.until(Conditions.webElementsAreVisible("body_element_buffer".<>(rowIdx), How.TAG_NAME, "td"))

  def getFooterRow(rowIdx: Int): mutable.Buffer[DOMElement] =
    Wait.until(Conditions.webElementsAreVisible("footer_element_buffer".<>(rowIdx), How.TAG_NAME, "td"))

  override def getHeaderRowCount: Int = "header_element_buffer".<>.length

  override def getBodyRowCount: Int = "body_element_buffer".<>.length

  override def getFooterRowCount: Int = "footer_element_buffer".<>.length



}
