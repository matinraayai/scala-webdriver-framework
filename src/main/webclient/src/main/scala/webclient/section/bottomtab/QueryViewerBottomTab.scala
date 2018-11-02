package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import tools.component.Section
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

case class QueryViewerBottomTab(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "node_statuses" -> WebDriverVisibility   .waitForMultiple(How.TAG_NAME, "node-status"),
    "node_results_wait" -> WebDriverVisibility   .waitForMultiple(How.TAG_NAME, "node-result")
  )

}
