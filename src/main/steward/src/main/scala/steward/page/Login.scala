package steward.page

import java.util.NoSuchElementException

import tools.infrastructure.browser.Session
import tools.component.Page
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.WebDriverPresence
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

//Done
/**
  * Provides an abstraction over the Data Steward Login Page.
  * @param session Session object for capturing elements on the page.
  */
case class Login(protected val session:Session) extends Page {



  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "steward_login_username_text_box",
    "steward_login_password_text_box",
    "steward_login_submit_button")

  def login(username: String, password: String): Unit = {
    setUsername(username)
    setPassword(password)
    pressSubmitButton()
  }

  def setUsername(content: String): Unit = {
    val usernameElement: DOMElement = "steward_login_username_text_box"
    usernameElement.clear()
    usernameElement.sendKeys(content)
  }

  def setPassword(content: String): Unit = {
    val passwordElement: DOMElement = "steward_login_password_text_box"
    passwordElement.clear()
    passwordElement.sendKeys(content)
  }

  def isSubmitButtonEnabled: Boolean = "steward_login_submit_button".getCssValue("disabled")== null

  def pressSubmitButton(): Unit = "steward_login_submit_button".click()

  def evaluatePostSubmitEvents(): String = ???
    //val result: Any =
    //session.until(() => {session.findElementUsingWebDriver("steward_home_page_header")},
      //() => {session.findElementUsingWebDriver("steward_login_error_message_text")})
    //result match {
     // case e: Option[ShrineElement] => e.get.getTextFromWebDriver match {
     //   case "invalid login" => "Login failed, correct message displayed."
      //  case "Logout" => "Login successful"
      //  case _: String => throw new NoSuchElementException("No expected behavior was observed.")
     // }
     // case a: Any => throw new NoSuchElementException("No expected behavior was observed. Class found: %s".format(a.getClass))
   // }
  //}

  def isRequiredFieldTextVisible: Boolean = "steward_login_required_field_text".isDisplayed

  override protected final lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "steward_login_form_wrapper" -> (WebDriverPresence waitForSingle (How.CSS,"#page-wrapper > div > div > div:nth-child(2) > div > div")),
    "steward_login_username_text_box" -> (WebDriverPresence waitForSingle (How.ID, "username")),
    "steward_login_username_label" -> (WebDriverPresence waitForSingle (How.CSS, "#page-wrapper > div > div > div:nth-child(2) > div > div > form > fieldset > div:nth-child(1) > span")),
    "steward_login_password_text_box" -> (WebDriverPresence waitForSingle (How.ID, "password")),
    "steward_login_password_label" -> (WebDriverPresence waitForSingle (How.CSS,"#page-wrapper > div > div > div:nth-child(2) > div > div > form > fieldset > div:nth-child(2) > span")),
    "steward_login_submit_button" -> (WebDriverPresence waitForSingle (How.CSS,"button[type='submit']")),
    "steward_login_error_message_text" -> (WebDriverPresence waitForSingle (How.CSS,"label[class='control-label'")),
    "steward_login_required_field_text" -> (WebDriverPresence waitForSingle (How.CSS,"#page-wrapper > div > div > div:nth-child(2) > div > div > form > fieldset > div:nth-child(1) > label")),
    "steward_home_page_header" -> (WebDriverPresence waitForSingle (How.CSS,"body > div > nav > ul > li.dropdown.open > ul > li:nth-child(4) > a")))
}
