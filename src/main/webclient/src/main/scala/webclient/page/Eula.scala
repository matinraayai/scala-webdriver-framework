package webclient.page

import tools.infrastructure.browser.Session
import tools.component.Page
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//TODO: Documentation needed.
case class Eula(protected val session: Session) extends Page {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    ("announcement_body", JSoupPresence .waitForSingle (How.ID, "PM-announcement-body")),
    ("i_agree_button", WebDriverVisibility .waitForSingle (How.ID, "yui-gen0-button")),
    ("i_disagree_button", WebDriverVisibility .waitForSingle (How.ID, "yui-gen1-button")))

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "announcement_body", "i_agree_button", "i_disagree_button")

  def getAgreementText: String = "announcement_body".textFromJSoup

  def agree(): Unit = "i_agree_button".click()

  def disagree(): Unit = "i_disagree_button".click()
}