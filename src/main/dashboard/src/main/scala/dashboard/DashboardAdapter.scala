package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session

/**
  * API for the Adaptor tab in the dashboard application. Mostly contains information tables and
  * the basic functionality provided by the Base Page trait.
  * @param session for manipulating the page.
  * @author Matin Ardakani
  */
case class DashboardAdapter(protected val session: Session) extends DashboardBasePage {

  private val adapterConfig: DashboardStatusTable = DashboardStatusTable(session, "dashboard_table_wrapper", 0)

  private val adapterQueryTest: DashboardStatusTable = DashboardStatusTable(session, "dashboard_table_wrapper", 1)

  private val mappingFiles: DashboardStatusTable = DashboardStatusTable(session, "dashboard_table_wrapper", 2)

  //TODO: Add specific methods for extracting information.
}
