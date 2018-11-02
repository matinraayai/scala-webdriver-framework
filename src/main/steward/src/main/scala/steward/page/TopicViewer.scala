package steward.page

import tools.component.Page
import tools.capture.CaptureSchemes.WebDriverVisibility
import org.openqa.selenium.NoSuchElementException
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Base functionality for the Topic Viewer page
  */
//Done. //TODO: WRITE DOCUMENTATION.
trait TopicViewer extends Page {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "topic_viewer_tab_selector" -> WebDriverVisibility .waitForSingle (How.CSS, "div[class='btn-group']"),
      "topic_viewer_close_button" -> WebDriverVisibility .waitForSingle (How.CSS, "i[class='shrine-close']")
  )

  protected def changeTab(tabName: String): Unit =
    "topic_viewer_tab_selector".findElement(ParserEngine.WEBDRIVER, How.LINK_TEXT, tabName).
      getOrElse(throw new NoSuchElementException(s"Failed to find the tab handle with name: $tabName")).click()

  def switchToDescriptionTab(): Unit = changeTab("Description")

  def switchToQueryHistoryTab(): Unit = changeTab("Query History")

  def closeWindow(): Unit = "topic_viewer_close_button".click()
}