package dashboard.component

import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap


/**
  * A component representing the header section of both dashboard and steward applications.
  */
trait AngularHeaderSection extends Section {

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "header_section_as_buffer" -> (JSoupPresence waitForMultiple (How.TAG_NAME, "li"))
  )

  def clickOnDropDownPanel(): Unit = "header_section_as_buffer".<>(1).click()

  def getGreetingMessage: String = "header_section_as_buffer".<>.head.textFromJSoup

  def logOut(): Unit  = "header_section_as_buffer".<>.last.click()
}
