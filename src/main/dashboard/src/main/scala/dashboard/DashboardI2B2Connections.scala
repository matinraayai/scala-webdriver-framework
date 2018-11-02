package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session

/**
  * The I2B2 Tab Api in dashboard. Contains mostly tables along with functionality inherited from
  * the `DashboardBasePage` trait.
  * @param session for manipulating the page
  * @author Matin Ardakani
  */
case class DashboardI2B2Connections(protected val session: Session) extends DashboardBasePage {

  private val endpointURLs: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 0 )
  private val hiveCredentials: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 1)

  def pmCellUrl: String = endpointURLs.getBodyRowAsStringBuffer(0)(1)

  def crcCellUrl: String = endpointURLs.getBodyRowAsStringBuffer(1)(1)

  def ontUrl: String = endpointURLs.getBodyRowAsStringBuffer(2)(1)

  def i2b2Domain: String = hiveCredentials.getBodyRowAsStringBuffer(0)(1)

  def username: String = hiveCredentials.getBodyRowAsStringBuffer(1)(1)

  def crcProject: String = hiveCredentials.getBodyRowAsStringBuffer(2)(1)

  def ontProject: String = hiveCredentials.getBodyRowAsStringBuffer(3)(1)

}
