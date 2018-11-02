package dashboard.component

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.SingleLocatingProcedure

case class DashboardHeader(protected val session: Session,
                           protected val parentElementProcedure: SingleLocatingProcedure) extends AngularHeaderSection {}
