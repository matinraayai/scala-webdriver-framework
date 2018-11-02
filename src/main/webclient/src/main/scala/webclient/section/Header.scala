package webclient.section

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.SingleLocatingProcedure

case class Header(protected val session: Session,
                  protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

}
