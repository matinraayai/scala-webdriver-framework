package webclient.section.component

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import org.openqa.selenium.NotFoundException
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//TODO: Add Documentation.
//TODO: Add more appropriate methods
case class QueryToolSinglePanel(protected val session: Session,
                                protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "clear_button" -> JSoupPresence   .waitForSingle  (How.CLASS_NAME, "qryPanelClear"),
    "date_button" -> JSoupPresence   .waitForSingle (How.CLASS_NAME, "qryButtonDate"),
    "exclude_button" -> JSoupPresence   .waitForSingle (How.CLASS_NAME, "qryButtonExclude"),
    "panel_title" -> JSoupPresence   .waitForSingle  (How.CLASS_NAME, "qryPanelTitle"),
    "panel_timing_dropdown" -> JSoupPresence   .waitForSingle (How.TAG_NAME, "button"),
    "panel_timing_dropdown_item_list" -> JSoupPresence   .waitForSingle (How.TAG_NAME, "ul"),
    "query_panel" -> JSoupPresence   .waitForSingle (How.CLASS_NAME, "queryPanel"),
    "query_panel_current_items_wrapper" -> JSoupPresence   .waitForSingle (How.CLASS_NAME, "ygtvchildren")
  )
  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "items" -> JSoupPresence  .waitForMultiple(How.CLASS_NAME, "ygtvitem")
  )

  private[webclient] def getQueryPanelElement: DOMElement = "query_panel"

  def getTitle: String = "panel_title".textFromJSoup

  def clearPanel(): Unit = "clear_button".click()

  def openDatePanel(): Unit = "date_button".click()

  def excludeItemsInPanel(): Unit = "exclude_button".click()

  def selectPanelTimingDropDownItem(itemName: String): Unit = {
    "panel_timing_dropdown".click()
    "panel_timing_dropdown_item_list".findElement(ParserEngine.WEBDRIVER, How.LINK_TEXT, itemName).
      getOrElse(throw new NotFoundException(s"Failed to find the item in the list called $itemName")).click()
  }

  def getPanelContent: mutable.Buffer[TreeItem] = {
    "items".<>.map(i => TreeItem(session, i.getIDAttribute))
  }
}
