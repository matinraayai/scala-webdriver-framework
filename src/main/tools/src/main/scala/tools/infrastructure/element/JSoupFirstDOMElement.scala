package tools.infrastructure.element

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.openqa.selenium.{By, SearchContext}
import org.openqa.selenium.remote.{RemoteWebDriver, RemoteWebElement}
import org.openqa.selenium.support.How
import org.openqa.selenium.support.pagefactory.ByAll
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import tools.infrastructure.parser.SeleniumParsingEngine

/**
  * An implementation of a DOMElement where the JSoup representation of the element on the DOM is
  * loaded first, usually in functions where it is stated that JSoup is used to find the element.
  * @param thisAsJSoupElement the found JSoup representation of the element.
  * @param parentRemoteWebDriver A reference to the RemoteWebDriver instance created by `Session`
  */
case class JSoupFirstDOMElement(private val thisAsJSoupElement: Element,
                                private val parentRemoteWebDriver: RemoteWebDriver)
  extends DOMElement {

  private lazy val thisAsRemoteWebElement: RemoteWebElement = {
    try {
      getParentRemoteWebDriver.findElement(By.cssSelector(selfCssSelector)) match {
        case r: RemoteWebElement => r
      }
    }
    catch {
      case e: Exception => throw e
    }
  }

  /**
    * @return representation of the element on the DOM as a JSoup Element.
    */
  protected def getThisAsJSoupElement: Element = {
    this.thisAsJSoupElement
  }

  /**
    * @return the instance of `RemoteWebDriver` to be used to get the 'RemoteWebElement'
    *         representation of the element.
    */
  protected def getParentRemoteWebDriver: RemoteWebDriver = this.parentRemoteWebDriver

  /**
    * @return element's representation as a `RemoteWebElement`.
    *         WARNING: This method doesn't always work, since JSoup doesn't always parse the DOM
    *         correctly.
    */
  protected[tools] def getThisAsRemoteWebElement: RemoteWebElement = thisAsRemoteWebElement

  override def toString: String = "JSoupFirstDOMElement\n" + super.toString

  override protected def getThisAsWebDriverSearchContext: SearchContext = thisAsRemoteWebElement
}
