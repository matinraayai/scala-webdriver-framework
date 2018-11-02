package webclient.section.component

import tools.infrastructure.browser.Session
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.component.Section
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

case class NodeStatus(protected val session: Session, parentWrapper: SingleLocatingProcedure, index: Int) extends Section {
  override protected val parentElementProcedure: SingleLocatingProcedure = parentWrapper .--> (WebDriverVisibility .waitForSingle (How.CSS, s" node-status:nth-child($index)"))

}
