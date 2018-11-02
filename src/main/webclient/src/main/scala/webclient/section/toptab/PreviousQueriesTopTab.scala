package webclient.section.toptab

import tools.infrastructure.browser.Session
import tools.capture.SingleLocatingProcedure

/**
  * Abstraction over the functionality of Previous Queries' top tab.
  * @param session Session instance in charge of abstraction creation
  * @param componentName Name of the infastructure.component as stated in the locator maps inside the `Home` page
  *                      object.
  */
private[webclient] case class PreviousQueriesTopTab(protected val session: Session,
                                 protected val parentElementProcedure: SingleLocatingProcedure)
  extends WebClientTopTab {

  def openXMLViewer(): Unit = clickOnIcon(0)

  def pressRefreshAll(): Unit = clickOnIcon(1)

  def openShowOptionsMenu(): Unit = clickOnIcon(2)

  def pressResizeWindowButton(): Unit = clickOnIcon(3)
}
