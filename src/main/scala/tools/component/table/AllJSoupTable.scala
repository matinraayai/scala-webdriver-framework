package tools.component.table

import tools.infrastructure.browser.Session
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//TODO: Documentation
case class AllJSoupTable(protected val session: Session,
                         protected val parentElementProcedure: SingleLocatingProcedure) extends Table {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "header_element" -> (JSoupPresence waitForSingle (How.TAG_NAME, "thead")),
    "body_element" -> (JSoupPresence waitForSingle (How.TAG_NAME, "tbody")),
    "footer_element" -> (JSoupPresence waitForSingle (How.TAG_NAME, "tfoot"))
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "header_element_buffer" -> "header_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr")),
    "body_element_buffer" -> "body_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr")),
    "footer_element_buffer" -> "footer_element" .--> (WebDriverVisibility waitForMultiple (How.TAG_NAME, "tr"))
  )

  def headerElement: DOMElement = "header_element"

  def bodyElement: DOMElement = "body_element"

  def footerElement: DOMElement = "footer_element"

  override def getHeaderRowCount: Int = "header_element_buffer".length

  override def getBodyRowCount: Int = "body_element_buffer".length

  override def getFooterRowCount: Int = "footer_element_buffer".length

  override def getHeaderRow(rowIdx: Int): mutable.Buffer[DOMElement] = "header_element_buffer".<>(rowIdx).findElements(ParserEngine.JSOUP, How.TAG_NAME, "td")

  override def getBodyRow(rowIdx: Int): mutable.Buffer[DOMElement] = "body_element_buffer".<>(rowIdx).findElements(ParserEngine.JSOUP, How.TAG_NAME, "td")

  override def getFooterRow(rowIdx: Int): mutable.Buffer[DOMElement] = "footer_element_buffer".<>(rowIdx).findElements(ParserEngine.JSOUP, How.TAG_NAME, "td")
}
