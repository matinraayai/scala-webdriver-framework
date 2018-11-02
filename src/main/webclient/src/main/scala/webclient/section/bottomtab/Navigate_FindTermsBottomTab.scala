package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

case class Navigate_FindTermsBottomTab(protected val session: Session,
                                       protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "find_terms_frame" -> JSoupPresence   .waitForSingle (How.ID, "ontFindDisp"),
    "navigate_terms_frame" -> JSoupPresence   .waitForSingle (How.ID, "ontNavDisp")
  )

  final val navigateTermsBottomTab: NavTermsTab = NavTermsTab(session, "navigate_terms_frame")

  final val findTermsBottomTab: FindTermsTab = FindTermsTab(session, "find_terms_frame")


}
