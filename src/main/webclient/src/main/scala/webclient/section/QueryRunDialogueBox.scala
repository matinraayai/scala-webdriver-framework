package webclient.section

import tools.infrastructure.browser.Session
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import tools.component.Section
import tools.infrastructure.element.DOMElement
import tools.infrastructure.enums.{How, ParserEngine}

import scala.collection.concurrent.TrieMap

case class QueryRunDialogueBox(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "query_input_name" -> (WebDriverVisibility waitForSingle  (How.TAG_NAME, "input")),
    "number_of_patients_field" -> (WebDriverVisibility waitForSingle   (How.ID, "crcDlgResultOutputPATIENT_COUNT_XML")),
    "age_breakdown_field" -> (WebDriverVisibility waitForSingle   (How.ID, "crcDlgResultOutputPATIENT_AGE_COUNT_XML")),
    "race_breakdown_field" -> (WebDriverVisibility waitForSingle   (How.ID, "crcDlgResultOutputPATIENT_RACE_COUNT_XML")),
    "gender_breakdown_field" -> (WebDriverVisibility waitForSingle   (How.ID, "crcDlgResultOutputPATIENT_GENDER_COUNT_XML")),
    "vital_breakdown_field" -> (WebDriverVisibility waitForSingle   (How.ID, "crcDlgResultOutputPATIENT_VITALSTATUS_COUNT_XML")),
    "run_query_button" -> (WebDriverVisibility waitForSingle   (How.ID, "yui-gen7-button")),
    "cancel_query_button" -> (WebDriverVisibility waitForSingle   (How.ID, "yui-gen8-button")),
    "close_dialogue" -> (WebDriverVisibility waitForSingle  (How.CLASS_NAME, "container-close"))
  )

  def setQueryName(newValue: String): Unit = {
    "query_input_name".>>.clear()
    "query_input_name".sendKeys(newValue)
  }

  def currentQueryName: String = "query_input_name".getTextFromWebDriver

  private def toggleCheckBox(field: DOMElement): Unit = {
    field.findElement(ParserEngine.WEBDRIVER, How.TAG_NAME, "input").get.click()
  }

  def toggleAgeBreakdown(): Unit = toggleCheckBox("age_breakdown_field")

  def toggleRaceBreakdown(): Unit = toggleCheckBox("race_breakdown_field")

  def toggleGenderBreakdown(): Unit = toggleCheckBox("gender_breakdown_field")

  def toggleVitalBreakdown(): Unit = toggleCheckBox("vital_breakdown_field")

  def pressRun(): Unit = "run_query_button".click()

  def pressCancel(): Unit = "cancel_query_button".click()

  def pressClose(): Unit = "close_dialogue".click()

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded("number_of_patients_field")
}
