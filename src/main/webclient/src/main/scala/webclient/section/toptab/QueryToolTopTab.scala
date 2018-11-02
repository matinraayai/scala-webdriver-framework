package webclient.section.toptab

import tools.infrastructure.browser.Session
import tools.capture.SingleLocatingProcedure

/**
  * Represents the Top tab for the Query Tool Section.
  * @param session The Session instance in charge of creating the Component.
  * @param componentName Name of the infastructure.component as a string, as stated in the invoked page object.
  */
//TODO: Add documentation.
case class QueryToolTopTab(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure)
  extends WebClientTopTab {

  def openXMLViewer(): Unit = clickOnIcon(0)

  def openShowOptionsMenu(): Unit = clickOnIcon(1)

  def pressResizeWindowButton(): Unit = clickOnIcon(2)


}
