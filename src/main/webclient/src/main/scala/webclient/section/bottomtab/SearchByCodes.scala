package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.component.{Section, SelectDropDown}
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How
import webclient.section.component.TreeItem

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

case class SearchByCodes(protected val session: Session,
                    protected val parentElementProcedure: SingleLocatingProcedure)
  extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "form" -> WebDriverVisibility .waitForSingle(How.ID, "ontFormFindCode"),
    "results" -> WebDriverVisibility .waitForSingle(How.ID, "ontSearchCodesResults"),
    "coding_system_dropdown" -> WebDriverVisibility .waitForSingle (How.ID,"ontFindCoding"),
    "search_text_box" -> WebDriverVisibility .waitForSingle (How.ID,"ontFindCodeMatch"),
    "find_button" -> WebDriverVisibility .waitForSingle (How.CLASS_NAME, "ontFindButton"),
    "results_wrapper" -> WebDriverVisibility .waitForSingle (How.ID,"ontSearchNamesResults")
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "currently_shown_results" -> "results_wrapper" .--> (JSoupPresence .waitForMultiple (How.CLASS_NAME, "ygtvitem"))
  )

  private val codesTabCodingSystemDropDown: SelectDropDown =
    SelectDropDown(session, "coding_system_dropdown")

  def setSearchByCodesTextBox(value: String): Unit = {
    val textBox: DOMElement = "search_text_box"
    textBox.clear()
    textBox.sendKeys(value)
  }

  def selectOntologyFindCodingFromDropDown(option: String): Unit = {
    codesTabCodingSystemDropDown.selectByValue(option)
  }

  def selectOntologyFindCodingFromDropDown(option: Int): Unit = {
    codesTabCodingSystemDropDown.selectByIndex(option)
  }

  def pressFindCodesSearchButton(): Unit = "find_button".click()

  //TODO: Use Condition Wait.
  def evaluateCodeFindingOutCome: String = ""

  def getCurrentlyShownSearchByCodesResults: mutable.Buffer[TreeItem] = {
    val treeItemIDs: mutable.Buffer[String] =
      "currently_shown_results".<>.map(_.getIDAttribute)
    treeItemIDs.map(TreeItem(session, _))
  }

}
