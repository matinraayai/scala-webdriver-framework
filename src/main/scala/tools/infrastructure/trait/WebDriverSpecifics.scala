package tools.infrastructure.`trait`

import java.io.File
import java.util
import java.util.logging.Level

import tools.infrastructure.element.{DOMElement, WebElementFirstDOMElement}
import org.openqa.selenium.WebDriver.{Navigation, Window}
import org.openqa.selenium.interactions.{Keyboard, Mouse}
import org.openqa.selenium._
import org.openqa.selenium.remote._
import org.openqa.selenium.remote.service.DriverService
import org.openqa.selenium.support.How

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * Includes all desired functionality delegated to the [[RemoteWebDriver]] in Selenium, excluding finding elements.
  * When adding to this trait, make sure to indicate that the method is getting the output from the remoteWebDriver
  * in the function signatures.
  * Also prefer concrete return classes to interfaces; See [[RemoteWebDriver]] source to confirm the actual return type.
  * This will make sure the API writer gets all the functionality embedded in RemoteWebDriver without the need of
  * type matching or type casting.
  * Also keep in mind that any field representing the objects stored in the concrete implementation must match names
  * with other traits implemented by the concrete implementation. For example, the field "selfCssSelector" must be
  * identical across all classes.
  * @author Matin Raayai Ardakani
  */
trait WebDriverSpecifics extends JavascriptExecutor {

  protected val selfCssSelector: String

  protected def getParentRemoteWebDriver: RemoteWebDriver

  protected def getWebDriverService: DriverService

  /**
    * Exposes the Capabilities instance inside the parent RemoteWebDriver, which consists of a map for
    *         browser parameters. Should be identical to the passed Capabilities object to the RemoteWebDriver
    *         in `Session`'s companion object when starting a session.
    * @see [[Capabilities]]
    * @see [[DesiredCapabilities]]
    * @see [[ImmutableCapabilities]]
    * @return The capabilities object stored in the parent remoteWebDriver.
    * @author Matin Raayai Ardakani
    */
  def webDriverCapabilities: Capabilities = getParentRemoteWebDriver.getCapabilities

  /**
    * @return the source of the page using the parentRemoteWebDriver, which is done by executing the
    *         [[DriverCommand.GET_PAGE_SOURCE]] in the command Executor of the RemoteWebDriver.
    * @see [[org.openqa.selenium.WebDriver]]
    * @see [[RemoteWebDriver]]
    * @author Matin Ardakani
    */
  def PageSourceFromWebDriver: String = getParentRemoteWebDriver.getPageSource

  /**
    * @return Current url returned from the `parentRemoteWebDriver`.
    */
  def currentURLFromWebDriver: String = getParentRemoteWebDriver.getCurrentUrl

  /**
    * Return an opaque handle to this window that uniquely identifies it within this driver instance.
    * This can be used to switch to this window at a later date
    * @return the current Window Handle.
    */
  def currentWindowHandleFromWebDriver: String = getParentRemoteWebDriver.getWindowHandle

  //TODO: Test the screenshot methods, document differences properly.

  /**
    * Captures a screenshot as Base64 data encoded in a string. The domain of the screenshot is not documented in
    * [[RemoteWebDriver]] properly, but based on the interface [[TakesScreenshot]], this method attempts a best
    * effort to screenshot in order of preference:
    * <ul>
    *   <li>Entire page</li>
    *   <li>Current window</li>
    *   <li>Visible portion of the current frame</li>
    *   <li>The screenshot of the entire display containing the browser</li>
    * </ul>
    * @return
    */
  @Beta
  def screenshotAsBase64UsingWebDriver: String = getParentRemoteWebDriver.getScreenshotAs(OutputType.BASE64)

  @Beta
  def screenshotAsRawBytesUsingWebDriver: Array[Byte] = getParentRemoteWebDriver.getScreenshotAs(OutputType.BYTES)

  @Beta
  def screenshotAsFileUsingWebDriver: File = getParentRemoteWebDriver.getScreenshotAs(OutputType.FILE)

  /**
    * Closes the currently open window. Quits the Session if there are no other windows left open.
    */
  def closeCurrentWindowUsingWebDriver(): Unit = getParentRemoteWebDriver.close()

  def quit(): Unit = {
    getParentRemoteWebDriver.quit()
    getWebDriverService.stop()
  }



  def getAllOpenWindowHandles: mutable.Set[String] = getParentRemoteWebDriver.getWindowHandles.asScala

  override def executeScript(script: String, args: AnyRef*): AnyRef =
    getParentRemoteWebDriver.executeScript(script, args.asJava)

  override def executeAsyncScript(script: String, args: AnyRef*): AnyRef =
    getParentRemoteWebDriver.executeAsyncScript(script, args.asJava)

  //TODO Check for usefulness of window switching, expose more switch to functionality.
  def switchToWindow(nameOrHandle: String): Unit = getParentRemoteWebDriver.switchTo().window(nameOrHandle)

  def switchToAlert: Option[Alert] = {
    try
      Some(getParentRemoteWebDriver.switchTo().alert())
    catch{
      case _: Throwable => None
    }
  }

  /**
    * Attempts to select a frame by its (zero-based) index. Selecting a frame by index is equivalent to the
    * JS expression window.frames[index] where "window" is the DOM window represented by the
    * current context.
    * @param index (zero-based) index
    * @return true if the switch has been successful, false otherwise.
    */
  def switchToFrame(index: Int): Boolean = {
    try {
      getParentRemoteWebDriver.switchTo().frame(index)
      true
    }
    catch {
      case _: NoSuchFrameException => false
    }
  }

  def switchToFrame(element: DOMElement): WebDriverSpecifics = {
    getParentRemoteWebDriver.switchTo().frame(element.getThisAsRemoteWebElement)
    this
  }

  def switchToParentFrame(): WebDriverSpecifics = {
    getParentRemoteWebDriver.switchTo().parentFrame()
    this
  }

  def switchToDefaultContent(): WebDriverSpecifics = {
    getParentRemoteWebDriver.switchTo().defaultContent()
    this
  }

  @Beta
  def getActiveElement: DOMElement = {
    val activeElement: RemoteWebElement =
      getParentRemoteWebDriver.switchTo().activeElement() match {
        case r: RemoteWebElement => r
      }
    WebElementFirstDOMElement(activeElement, selfCssSelector)
  }

  def navigate(): Navigation = getParentRemoteWebDriver.navigate()

  def manage: WebDriver.Options = getParentRemoteWebDriver.manage()

  def setLogLevel(level: Level): Unit = getParentRemoteWebDriver.setLogLevel(level)

  def getCurrentPageTitle: String = getParentRemoteWebDriver.getTitle


}
