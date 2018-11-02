package steward.component

import dashboard.component.AngularHeaderSection
import tools.infrastructure.browser.Session
import tools.capture.SingleLocatingProcedure

//Done for now.
//TODO Write documentation with all the appropriate tags.
case class StewardHeaderSection(override protected val session: Session,
                                override protected val parentElementProcedure: SingleLocatingProcedure)
  extends AngularHeaderSection {

  def getRoleText: String = "header_section_as_buffer".<>(3).textFromJSoup

  def openHelpWindow(): Unit = "header_section_as_buffer".<>(5).click()

}
