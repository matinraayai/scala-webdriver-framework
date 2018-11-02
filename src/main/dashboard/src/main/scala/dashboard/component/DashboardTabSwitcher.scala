package dashboard.component

import tools.infrastructure.browser.Session
import tools.component.AngularTabSwitcher
import tools.capture.SingleLocatingProcedure

//TODO: Documentation
case class DashboardTabSwitcher(protected val session: Session,
                                protected val parentElementProcedure: SingleLocatingProcedure)
  extends AngularTabSwitcher {

  def switchToSummaryTab(): Unit = clickOnTab("Summary")

  def switchToI2B2ConnectionsTab(): Unit = clickOnTab("i2b2 Connections")

  def switchToKeystoreTab(): Unit = clickOnTab("Keystore")

  def switchToAdaptorTab(): Unit = clickOnTab("Adaptor")

  def switchToQEPTab(): Unit = clickOnTab("QEP")

  def switchToConfigTab(): Unit = clickOnTab("Config")

  def switchToProblemLogTab(): Unit = clickOnTab("Problem Log")

}
