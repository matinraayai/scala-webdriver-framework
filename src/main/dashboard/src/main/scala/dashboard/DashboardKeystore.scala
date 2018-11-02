package dashboard

import dashboard.component.DashboardStatusTable
import tools.infrastructure.browser.Session


/**
  * API for the Keystore tab in the Dashboard application. It includes information tables along with
  * base functionality inherited from `DashboardBasePage`.
  * @param session for extracting information off the page.
  * @author Matin Ardakani
  */
case class DashboardKeystore(protected val session: Session) extends DashboardBasePage {
  private val keystoreFile: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 0)
  private val querySigningCertificate: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 1)
  private val caCertificateInfo: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 2)
  private val certificateValidation: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 3)
  private val keystoreContents: DashboardStatusTable =
    DashboardStatusTable(session, "dashboard_table_wrapper", 4)

  def keystoreFileDirectory: String = keystoreFile.getBodyRowAsStringBuffer(0)(1)

  def keystoreFilePassword: String = keystoreFile.getBodyRowAsStringBuffer(1)(1)

  def querySigningCertificateAlias: String = querySigningCertificate.getBodyRowAsStringBuffer(0)(1)

  def querySigningCertificateOwner: String = querySigningCertificate.getBodyRowAsStringBuffer(1)(1)

  def querySigningCertificateIssuer: String = querySigningCertificate.getBodyRowAsStringBuffer(2)(1)

  def querySigningCertificateExpireTime: String = querySigningCertificate.getBodyRowAsStringBuffer(3)(1)

  def querySigningCertificateMD5Signature: String = querySigningCertificate.getBodyRowAsStringBuffer(4)(1)

  def querySigningCertificateSHA256Signature: String = querySigningCertificate.getBodyRowAsStringBuffer(5)(1)

  def caCertInfoAlias: String = caCertificateInfo.getBodyRowAsStringBuffer(0)(1)

  def caCertInfoMD5Signature: String = caCertificateInfo.getBodyRowAsStringBuffer(1)(1)

  def certificateMatchesHub: String = certificateValidation.getBodyRowAsStringBuffer(0)(1)

  //TODO: Add more methods

}
