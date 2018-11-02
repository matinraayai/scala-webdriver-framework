package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

private[webclient] case class FindTermsTab (protected val session: Session,
                                            protected val parentElementProcedure: SingleLocatingProcedure)
extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "search_by_names_selector" -> WebDriverVisibility .waitForSingle (How.ID, "ontFindTabName"),
    "search_by_codes_selector" -> WebDriverVisibility .waitForSingle (How.ID, "ontFindTabCode")
  )


  def switchToNamesTab(): SearchByNames = {
    "search_by_names_selector".click()
    SearchByNames(session, parentElementProcedure)
  }

  def switchToCodesTab(): SearchByCodes = {
    "search_by_codes_selector".click()
    SearchByCodes(session, parentElementProcedure)
  }

}
