package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session

/**
  * API for the Hub tab in the Dashboard application. It includes an information table along with
  * the basic functionality of the `DashboardBasePage` trait.
  * @param session
  * @author Matin Ardakani
  */
case class DashboardHub(protected val session: Session) extends DashboardBasePage {
  private val downStreamNodes: DashboardStatusTable = DashboardStatusTable(session, "dashboard_table_wrapper", 0)

  //TODO: Add methods for grabbing information off the table.
}
