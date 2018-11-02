package tools.infrastructure.parser

import tools.infrastructure.element.{DOMElement, WebElementFirstDOMElement}
import tools.infrastructure.enums.How
import tools.infrastructure.enums.How.How
import org.openqa.selenium.{By, SearchContext, WebDriverException, WebElement}
import org.openqa.selenium.remote.{RemoteWebDriver, RemoteWebElement}
import org.openqa.selenium.support.pagefactory.ByAll

import scala.collection.mutable
import scala.collection.JavaConverters._

case class SeleniumParsingEngine(private val searchContext: SearchContext,
                                 private val contextCssSelector: String) extends DOMParsingEngine {

  /**
    * Abstraction over the match cased used to convert a [[org.openqa.selenium.WebElement]] to a
    * [[RemoteWebElement]].
    *
    * @param element the [[org.openqa.selenium.WebElement]] instance wished to be converted.
    * @return the same object, safely converted to a [[RemoteWebElement]].
    */
  private implicit def toRemoteWebElement(element: WebElement): RemoteWebElement = {
    element match {
      case r: RemoteWebElement => r
      case _: Any => throw new Exception("For some reason, case match didn't work.\n")
    }
  }

  /**
    * @return A Selenium "By" class containing this instance's information to be used with Selenium
    *         WebDriver.
    * @throws IllegalArgumentException if the input is How.Unset or invalid.
    */
  private[infrastructure] def convertToSeleniumLocator(how: How, locator: String): By = how match {
    case How.ID => By.id(locator)
    case How.CSS => By.cssSelector(locator)
    case How.CLASS_NAME => By.className(locator)
    case How.LINK_TEXT => By.linkText(locator)
    case How.NAME => By.name(locator)
    case How.PARTIAL_LINK_TEXT => By.partialLinkText(locator)
    case How.TAG_NAME => By.tagName(locator)
    case How.ID_OR_NAME => new ByAll(By.id(locator), By.name(locator))
    case _ => throw new IllegalArgumentException("Input is not valid.\n")
  }

  override def findElement(how: How, locator: String): Option[DOMElement] = {
    val by: By = convertToSeleniumLocator(how, locator)
    try {
      val foundElement: RemoteWebElement = searchContext.findElement(by)
      Some(WebElementFirstDOMElement(foundElement, contextCssSelector))
    }
    catch {
      case _: WebDriverException => None
    }
  }

  override def findElement(locatorSeq: (How, String)*): Option[DOMElement] = {
    val foundElement: Option[Option[DOMElement]] = locatorSeq.map(i => findElement(i _1, i _2)).find(_.isDefined)
    foundElement.getOrElse(None)
  }

  override def findElements(how: How, locator: String): mutable.Buffer[DOMElement] = {
    val by: By = convertToSeleniumLocator(how, locator)
    try {
      val foundElements: mutable.Buffer[RemoteWebElement] =
        searchContext.findElements(by).asScala.map(toRemoteWebElement)
      foundElements.map(WebElementFirstDOMElement(_, contextCssSelector))
    }
    catch {
      case _: WebDriverException => mutable.Buffer[DOMElement]()
    }
  }

  /**
    * @return this Element's parent Node from JSoup as `ShrineElement` wrapped in
    *         `Option`. None if the parent Node is null.
    */
  override def parent(): Option[DOMElement] = {
    val parentRemoteWebElement = {
      try
        Option(searchContext.findElement(By.xpath("..")))
      catch {
        case _: WebDriverException => None
      }
    }
    if(parentRemoteWebElement.isDefined)
      Some(WebElementFirstDOMElement(parentRemoteWebElement.get, contextCssSelector))
    else
      None
  }

  /**
    * Get this element's parent and ancestors, up to the document root.
    *
    * @return this element's stack of parents, closest first.
    */
  override def parents(): mutable.Buffer[DOMElement] = {
    val parents = mutable.Buffer[DOMElement]()
    var currentElement: Option[SearchContext] = Some(searchContext)
    var currentCssSelector = new mutable.StringBuilder(contextCssSelector)
    while(currentElement.isDefined) {
      val parentOfCurrentElement = {
        try Some(currentElement.get.findElement(By.xpath("..")))
        catch {
          case _: WebDriverException => None
        }
      }
      if(parentOfCurrentElement.isDefined) {
        currentCssSelector.take(currentCssSelector.lastIndexOf(" > "))
        parents += WebElementFirstDOMElement(parentOfCurrentElement.get, currentCssSelector.toString())
      }
      currentElement = parentOfCurrentElement
    }
    parents
  }

  /**
    * Get sibling elements. If the element has no sibling elements, returns an empty list.
    * An element is not a sibling of itself, so will not be included in the returned list.
    *
    * @return sibling elements
    */
  override def siblings() = ???

  override def sibling(index: Int) = ???

  override def siblingIndex = ???

  override def nextSibling = ???

  override def previousSibling = ???

  override def firstSibling = ???

  /**
    * Gets the last element sibling of this element, wrapped in Option.
    *
    * @return the last sibling that is an element (aka the parent's last element child), None if
    *         nothing is found.
    */
  override def lastSibling  = ???

  override def children() = ???

  /**
    * Get a child element of this element, by its 0-based index number.
    * Note that an element can have both mixed Nodes and Elements as children. This method inspects
    * a filtered list of children that are elements, and the index is based on that filtered list.
    *
    * @param index the index number of the element to retrieve
    * @return the child element wrapped in Option, if it exists, otherwise None
    **/
  override def child(index: Int) = ???
}