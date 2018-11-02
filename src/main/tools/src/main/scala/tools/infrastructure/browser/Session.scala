package tools.infrastructure.browser

import java.net.URL

import tools.infrastructure.`trait`._
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.SearchContext
import org.openqa.selenium.remote.service.DriverService
import org.openqa.selenium.remote.RemoteWebDriver
import tools.infrastructure.browser.builder._

/**
  * Controls a browsing session of the Desired type. Combines the functionality of a
  * [[RemoteWebDriver]] and [[org.jsoup.nodes.Document]] in a unified interface.
  * @author Matin Raayai Ardakani
  */
trait Session extends WebDriverSpecifics with
  JSoupSpecifics with SessionWebDriverActionBuilder with DomElementLocator {

  protected val parentRemoteWebDriver: RemoteWebDriver

  protected val remoteWebDriverService: DriverService

  protected def getParentRemoteWebDriver: RemoteWebDriver = this.parentRemoteWebDriver

  protected def getThisAsJSoupElement: Document =
    Jsoup.parse(getParentRemoteWebDriver.getPageSource)

  override def toString: String = "%s browser Session.\nWebDriver Information: [%s]\n".
    format(parentRemoteWebDriver.getCapabilities.getBrowserName, getParentRemoteWebDriver)

  protected def getThisAsWebDriverSearchContext: SearchContext = parentRemoteWebDriver

  protected def getWebDriverService: DriverService = this.remoteWebDriverService
}

/**
  * Companion Object for creating an instance of a session object.
  */
object Session {

  def ie(): IESessionBuilder = new IESessionBuilder

  def edge(): EdgeSessionBuilder = new EdgeSessionBuilder

  def chrome(): ChromeSessionBuilder = new ChromeSessionBuilder

  def fireFox(): FireFoxSessionBuilder = new FireFoxSessionBuilder

  def safari(): SafariSessionBuilder = new SafariSessionBuilder

  def opera(): Unit = ???

}