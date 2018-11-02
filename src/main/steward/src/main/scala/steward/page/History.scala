package steward.page

import tools.infrastructure.browser.Session
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap


//TODO: Table implementation.
class History(protected val session: Session) extends StewardOutline {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "steward_history_tab_table" -> WebDriverVisibility .waitForSingle (How.TAG_NAME, "table"),
    "steward_history_tab_pagination" -> WebDriverVisibility .waitForSingle (How.CSS,"ul[class='pagination-sm pagination ng-isolate-scope ng-not-empty ng-valid']")
  )

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = ???
}
