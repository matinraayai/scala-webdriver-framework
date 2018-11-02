package tools.infrastructure.element

import tools.infrastructure.`trait`._
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.openqa.selenium.{TimeoutException, WebDriverException}
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.support.How

import scala.collection.JavaConverters._
import scala.collection.mutable

/**
  * The main handle for elements in this infrastructure. Combines both JSoup Element and
  * RemoteWebElement capabilities into one object. The method description should be
  * the same for all concrete implementations; However, the way the element's JSoup and WebDriver
  * representations get retrieved are different depending on the implementation.
  */
trait DOMElement extends JSoupSpecifics with WebElementTrait with ElementActionBuilder with DomElementLocator {

  def nodeName: String = getThisAsJSoupElement.nodeName

  def isBlock: Boolean = getThisAsJSoupElement.isBlock

  def getIDAttribute: String = getThisAsJSoupElement.id

  def dataSet: mutable.Map[String, String] = getThisAsJSoupElement.dataset().asScala

  def cssSelector: String = getThisAsJSoupElement.cssSelector()

  override def toString: String = {
    "SourceElementCSS: [%s]\nJSoupElement:[%s]\nRemoteWebElement:[%s]".
      format(selfCssSelector, getThisAsJSoupElement, getThisAsJSoupElement)
  }
}
