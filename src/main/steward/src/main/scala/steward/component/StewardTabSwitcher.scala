package steward.component

import tools.infrastructure.browser.Session
import tools.component.AngularTabSwitcher
import tools.capture.SingleLocatingProcedure

//Done for now.
//TODO: Documentation needed.
case class StewardTabSwitcher(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure)
  extends AngularTabSwitcher {

  def switchToTopicsTab(): Unit = clickOnTab("Topics")

  def switchToHistoryTab(): Unit = clickOnTab("History")

  def switchToStatisticsTab(): Unit = clickOnTab("Statistics")

}


