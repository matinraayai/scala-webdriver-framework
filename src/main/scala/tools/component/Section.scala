package tools.component

import tools.infrastructure.browser.Session
import tools.infrastructure.element.DOMElement
import tools.capture.SingleLocatingProcedure


trait Section extends Model {
  protected val parentElementProcedure: SingleLocatingProcedure

  protected val session: Session

  override def captureModelDomainElement: DOMElement = parentElementProcedure.run(session)

}
