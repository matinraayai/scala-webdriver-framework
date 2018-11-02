package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session

/**
  * API for the QEP tab in Dashboard. Contains the information tables along with base functionality
  * of all Pages in the Dashboard app.
  * @param session for extracting information from the page.
  * @author Matin Ardakani
  */
class DashboardQEP(protected val session: Session) extends DashboardBasePage {
  private val qepModes: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 0)
  private val shrineDataSteward: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 1)

  //TODO: Add more methods.
}
