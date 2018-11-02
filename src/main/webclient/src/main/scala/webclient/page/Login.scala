package webclient.page

import java.util.concurrent.TimeUnit

import tools.infrastructure.browser.Session
import tools.component.{Page, SelectDropDown}
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.infrastructure.wait.{Conditions, Wait}
import org.openqa.selenium.Alert
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap

/**
  * Web Client Login page API.
  * @param session Session instance for manipulating the page.
  */
case class Login(protected val session: Session) extends Page {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "web_client_login_username_text_box" -> WebDriverVisibility .waitForSingle (How.ID, "loginusr"),
    "web_client_login_password_text_box" -> WebDriverVisibility .waitForSingle (How.ID, "loginpass"),
    "web_client_login_domain_drop_down" -> WebDriverVisibility .waitForSingle  (How.ID, "logindomain"),
    "web_client_login_submit_button" -> WebDriverVisibility .waitForSingle (How.CSS, "input[type='button']"),
    "web_client_login_dialogue_box_header"-> WebDriverVisibility .waitForSingle (How.ID, "i2b2_login_modal_dialog_h"),
    "web_client_eula_dialogue_box_title" -> WebDriverVisibility .waitForSingle (How.ID, "PM-announcement-title")
  )

  private val shrineHostSelect: SelectDropDown =
    SelectDropDown(session, "web_client_login_domain_drop_down")

  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "web_client_login_username_text_box",
    "web_client_login_password_text_box",
    "web_client_login_submit_button",
    "web_client_login_dialogue_box_header")

  def dragLoginBoxToCoordinates(xOffset: Int, yOffset: Int): Unit = {
    "web_client_login_dialogue_box_header".dragAndDropBy(xOffset, yOffset).perform()
  }

  def setCredentials(username: String, password: String): Unit = {
    val usernameElement: DOMElement = "web_client_login_username_text_box"
    val passwordElement: DOMElement = "web_client_login_password_text_box"
    usernameElement.clear()
    usernameElement.sendKeys(username)
    passwordElement.clear()
    passwordElement.sendKeys(password)
  }

  def setHostDropdown(host: String): Unit = shrineHostSelect.selectByVisibleText(host)

  def setHostDropdown(index: Int): Unit = shrineHostSelect.selectByIndex(index)

  def setPasswordTo(password: String): Unit = {
    val passwordElement: DOMElement = "web_client_login_password_text_box"
    passwordElement.clear()
    passwordElement.sendKeys(password)
  }

  def setUsernameTo(username: String): Unit = {
    val usernameElement: DOMElement = "web_client_login_username_text_box"
    usernameElement.clear()
    usernameElement.sendKeys(username)
  }

  def submitLogin(): Unit = "web_client_login_submit_button".click()

  def login(username: String, password: String, host: String = ""): Unit = {
    setCredentials(username, password)
    if(!host.isEmpty) {
      shrineHostSelect.selectByValue(host)
    }
    submitLogin()
  }

  def evaluateSubmitEvents(): String = {
    val waitResult = Wait.withTimeOut(1, TimeUnit.HOURS)untilOneReturnsValidResult(
      Conditions.alertAppearsAndGetsSwitchedTo(session), () => {locateSingleElementUsingMap("web_client_eula_dialogue_box_title")}
    )
    waitResult _2 match {
       case a: Alert => "Login Failed, the following alert message appeared: %s".format(a.getText)
       case e: DOMElement => if (e.getTextFromWebDriver == "SHRINE Announcements")
         "Login Successful" else "Shrine Announcements is not appearing."
       case _: Any => throw new IllegalStateException("Something went wrong.")
    }
  }

  def captureAndAcceptTheCurrentAlert(): Unit =
    Wait.until(Conditions.alertAppearsAndGetsSwitchedTo(session)).accept()

}
