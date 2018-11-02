package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session

/**
  * API for the Summary tab in the Dashboard application. Contains only tables and basic
  * functionality inherited from the `DashboardBasePage` and <b>Not</b> the scenario where
  * there are errors in the system.
  * @param session for extracting information off the web page.
  * @author Matin Ardakani
  */
case class DashboardSummary(protected val session: Session) extends DashboardBasePage {
  private val versionInfoTable: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 0)
  private val systemHealthTable: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 1)

  //TODO: Add more APIs.
}