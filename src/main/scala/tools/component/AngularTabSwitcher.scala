package tools.component

import tools.infrastructure.browser.JavascriptCode
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap


/**
  * Represents the tab switcher currently present in the `Steward` and `Dashboard` app.
  */
trait AngularTabSwitcher extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "currently_selected_tab_element" -> WebDriverVisibility .waitForSingle (How.CLASS_NAME, "shrine-on"))

  /**
    * Only clicks on the destinationTabName on the DOM. This is different from the method
    * switchTabsAndGivePage, which not only switches tabs, but would also return the current Page
    * the application is on.
    * @param destinationTabName the desired tab name.
    */
  protected def clickOnTab(destinationTabName: String): Unit = {
    session.executeScript(JavascriptCode.SCROLL_TO_BEGINNING_OF_DOCUMENT)
    captureModelDomainElement.findElement(ParserEngine.WEBDRIVER, How.LINK_TEXT, destinationTabName).
      getOrElse(throw new NoSuchElementException("Unable to locate the desired tab.\n")).click()
  }

  def getNameOfCurrentlySelectedTab: String = "currently_selected_tab_element".textFromJSoup

}


