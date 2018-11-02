package steward.component

import tools.infrastructure.browser.{JavascriptCode, Session}
import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

/**
  * Represents an abstraction for the pagination for the DSA and Dashboard applications.
  * @param parentElementProcedure Name of the container for the pagination element stored in the `Page`
  *                          object invoking this abstraction.
  * @param session the `Session` object responsible for creating the
  */
//TODO: Documentation for all methods needed.
case class AngularPagination(protected val session: Session,
                             protected val parentElementProcedure: SingleLocatingProcedure)
  extends Section {

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap =
    TrieMap("pagination_content" -> (JSoupPresence waitForMultiple (How.TAG_NAME, "a")))

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap =
    TrieMap("current_active_page" -> (JSoupPresence waitForSingle (How.CSS, "li[class='pagination-page ng-scope active']")))


  def goToFirstPage(): Unit = "pagination_content".<>.head click()

  def goToLastPage(): Unit =  "pagination_content".<>.last click()

  def goToPreviousPage(): Unit = "pagination_content"<> 1 click()

  def goToNextPage(): Unit = "pagination_content"<>("pagination_content".<>.size - 2) click()

  def goToIndex(index: Int): Unit = "pagination_content".<>(index + 1).click()

  def getCurrentPageNumber: Int = "current_active_page".textFromJSoup.toInt
}
