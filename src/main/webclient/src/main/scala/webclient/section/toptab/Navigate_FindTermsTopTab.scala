package webclient.section.toptab

import tools.infrastructure.browser.Session
import tools.capture.SingleLocatingProcedure

/**
  * Represents an abstraction over the Top tab of the Ontology section in the Webclient.
  * @param session The session instance in charge of fetching the required elements.
  * @param componentName Name of the infastructure.component as a string, as stated in the page object invoking
  *                      this.
  */
//TODO: Update Documentation
case class Navigate_FindTermsTopTab(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure)
  extends WebClientTopTab {

  def openXMLViewer(): Unit = clickOnIcon(0)

  def openShowOptionsMenu(): Unit = clickOnIcon(1)

  def pressResizeWindowButton(): Unit = clickOnIcon(2)

}
