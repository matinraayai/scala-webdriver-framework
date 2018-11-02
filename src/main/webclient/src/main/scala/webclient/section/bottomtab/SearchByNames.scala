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

case class SearchByNames(protected val session: Session,
                         protected val parentElementProcedure: SingleLocatingProcedure) extends Section {
  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "form" -> JSoupPresence .waitForSingle (How.ID, "ontFormFindName"),
    "results" -> JSoupPresence .waitForSingle (How.ID, "ontSearchNamesResults"),
    "search_strategy" -> WebDriverVisibility .waitForSingle(How.NAME, "ontFindStrategy"),
    "find_category" -> WebDriverVisibility .waitForSingle (How.ID, "ontFindCategory"),
    "search_text_box" -> WebDriverVisibility .waitForSingle (How.NAME, "ontFindNameMatch"),
    "find_button" -> WebDriverVisibility .waitForSingle (How.ID, "ontFindButton"),
    "search_results_wrapper" -> WebDriverVisibility .waitForSingle (How.ID, "ontFindNameMatch")
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "currently_loaded_children" -> "search_results_wrapper" .--> (JSoupPresence  .waitForMultiple (How.TAG_NAME, "ygtvitem"))
  )

  private val searchPhraseModifierDropDown: SelectDropDown =
    SelectDropDown(session, "search_strategy")

  private val searchCategoryModifierDropDown: SelectDropDown =
    SelectDropDown(session, "find_category")

  def getAllOntologyFindStrategyDropdownFields: mutable.Buffer[String] = {
    searchPhraseModifierDropDown.getOptions.map(_.getTextFromWebDriver)
  }

  def getCurrentlySelectedOntologyFindStrategyFromDropDown: String = {
    searchPhraseModifierDropDown.getFirstSelectedOption.getTextFromWebDriver
  }

  def selectOntologyFindStrategyFromDropDown(option: String): Unit = {
    searchPhraseModifierDropDown.selectByValue(option)
  }

  def selectOntologyFindStrategyFromDropDown(option: Int): Unit = {
    searchPhraseModifierDropDown.selectByIndex(option)
  }

  def setFindNamesTextBoxTo(value: String): Unit = {
    val textBox: DOMElement = "search_text_box"
    textBox.clear()
    textBox.sendKeys(value)
  }

  def getAllOntologyFindCategoryDropdownFields: mutable.Buffer[String] = {
    searchCategoryModifierDropDown.getOptions.map(_.getTextFromWebDriver)
  }

  def getCurrentlySelectedOntologyFindCategoryFromDropDown: String = {
    searchCategoryModifierDropDown.getFirstSelectedOption.getTextFromWebDriver
  }

  def selectOntologyFindCategoryFromDropDown(option: String): Unit = {
    searchCategoryModifierDropDown.selectByValue(option)
  }

  def selectOntologyFindCategoryFromDropDown(option: Int): Unit = {
    searchCategoryModifierDropDown.selectByIndex(option)
  }

  def pressFindNamesSearchButton(): Unit = "find_button".click()

  //TODO: Use Condition Wait
  def evaluateNameFindingOutCome: String = ""

  def getCurrentlyShownSearchByNamesResults: mutable.Buffer[TreeItem] = {
    val treeItemIDs: mutable.Buffer[String] = "currently_loaded_children".<>.map(_.getIDAttribute)
    treeItemIDs.map(TreeItem(session, _))
  }

}
