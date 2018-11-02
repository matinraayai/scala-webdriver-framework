package tools.infrastructure.parser

import tools.infrastructure.element.{DOMElement, JSoupFirstDOMElement}
import tools.infrastructure.enums.How
import tools.infrastructure.enums.How.How
import org.jsoup.nodes.Element
import org.openqa.selenium.remote.RemoteWebDriver

import scala.collection.mutable
import scala.collection.JavaConverters._

case class
JSoupParsingEngine(searchContext: Element, parentRemoteWebDriver: RemoteWebDriver)
  extends DOMParsingEngine {

  /**
    * Searches a JSoup `Element` for an Element with this instance's locating information. Uses a
    * case match on this instance's `how` field and takes the equivalent JSoup action.
    * Only returns the first found element. See JSoup's `Element` class for more information on how
    * this functionality is implemented.
    * Note that this is intended to be used inside infrastructure package only.
    * @param searchContext the search area (can be both a JSoup Element or a Document)
    * @return the desired JSoup element, if found in the context; null if nothing is found.
    * @throws IllegalArgumentException If this instance's `how` value is not supported.
    */
  private[infrastructure] def findJSoupElement(how: How, locator: String): Option[Element] = {
    how match {
      case How.ID => Option(searchContext.getElementById(locator))
      case How.CSS =>
        val foundElements = searchContext.select(locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.CLASS_NAME =>
        val foundElements = searchContext.getElementsByClass(locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.LINK_TEXT => val foundElements = searchContext.getElementsMatchingOwnText(locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.NAME => val foundElements = searchContext.getElementsByAttributeValueMatching("name", locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.PARTIAL_LINK_TEXT => val foundElements = searchContext.getElementsContainingOwnText(locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.TAG_NAME => val foundElements = searchContext.getElementsByTag(locator)
        if(foundElements.size() > 0) {
          if(foundElements.get(0).outerHtml() == searchContext.outerHtml())
            if(foundElements.size() > 1)
              Option(foundElements.get(1))
            else None
          else Option(foundElements.get(0))
        }
        else
          None
      case How.ID_OR_NAME => {
        if(Option(searchContext.getElementById(locator)).isDefined) {
          Option(searchContext.getElementById(locator))
        }
        else {
          Option(searchContext.getElementsByAttributeValueMatching("name", locator).get(0))
        }
      }
    }
  }


  /**
    * Searches a JSoup `Element` for Elements with this instance's locating information. Uses a
    * case match on this instance's `how` field and takes the equivalent JSoup action. See JSoup's
    * `Element` class for more information on how this functionality is implemented.
    * Note that this is intended to be used inside infrastructure package only.
    * @param searchContext the search area (can be both a JSoup Element or a Document)
    * @return the desired JSoup elements as a Buffer, if found in the context; An empty Buffer if
    *         nothing is found.
    * @throws IllegalArgumentException If this instance's `how` value is not supported.
    */
  private def findJSoupElements(how: How, locator: String): mutable.Buffer[Element] = {
    how match {
      case How.ID => throw new IllegalArgumentException("Cannot return multiple elements with an ID.\n")
      case How.CSS => searchContext.select(locator).asScala
      case How.CLASS_NAME => searchContext.getElementsByClass(locator).asScala
      case How.LINK_TEXT => searchContext.getElementsMatchingText(locator).asScala
      case How.NAME => searchContext.getElementsByAttributeValueMatching("name", locator).asScala
      case How.PARTIAL_LINK_TEXT => searchContext.getElementsContainingText(locator).asScala
      case How.TAG_NAME => searchContext.getElementsByTag(locator).asScala
      case How.ID_OR_NAME => throw new IllegalArgumentException("Locator currently not supported by JSoup.\n")
    }
  }

  override def findElement(how: How, locator: String): Option[DOMElement] = {
    val foundElementAsOption: Option[Element] = findJSoupElement(how, locator)
    if (foundElementAsOption.isEmpty)
      None
    else
      Some(JSoupFirstDOMElement(foundElementAsOption.get, parentRemoteWebDriver))
  }

  override def findElement(locatorSeq: (How, String)*): Option[DOMElement] = {
    val foundElement: Option[Option[DOMElement]] = locatorSeq.map(i => findElement(i _1, i _2)).find(_.isDefined)
    foundElement.getOrElse(None)
  }

  override def findElements(how: How, locator: String): mutable.Buffer[DOMElement] = {
    findJSoupElements(how, locator).map(JSoupFirstDOMElement(_, parentRemoteWebDriver))
  }

  override def parent(): Option[DOMElement] = {
    val parentJSoupElement: Option[Element] = Option(searchContext.parent())
    if(parentJSoupElement.isDefined)
      Some(JSoupFirstDOMElement(parentJSoupElement.get, parentRemoteWebDriver))
    else
      None
  }

  override def parents(): mutable.Buffer[DOMElement] = {
    searchContext.parents().asScala.map(JSoupFirstDOMElement(_, parentRemoteWebDriver))
  }

  override def siblings(): mutable.Buffer[DOMElement] = {
    val siblingJSoupElements = searchContext.siblingElements().asScala
    siblingJSoupElements.map(i => JSoupFirstDOMElement(i, parentRemoteWebDriver))
  }

  override def sibling(index: Int): Option[DOMElement] = {
    if(siblings().length > index)
      Some(siblings()(index))
    else
      None
  }

  override def children(): mutable.Buffer[DOMElement] = {
    val childrenJSoupElements: mutable.Buffer[Element] = searchContext.children().asScala
    childrenJSoupElements.map(i => {JSoupFirstDOMElement(i, parentRemoteWebDriver)
    })
  }

  override def nextSibling: Option[DOMElement] = {
    val nextElementSibling = Option(searchContext.nextElementSibling())
    if (nextElementSibling.isEmpty)
      None
    else Option(JSoupFirstDOMElement(nextElementSibling.get, parentRemoteWebDriver))
  }

  override def previousSibling: Option[DOMElement] = {
    val previousElementSibling =
      Option(searchContext.previousElementSibling())
    if (previousElementSibling.isEmpty)
      None
    else
      Option(JSoupFirstDOMElement(previousElementSibling.get, parentRemoteWebDriver))
  }

  override def firstSibling: Option[DOMElement] = {
    val firstElementSibling = Option(searchContext.firstElementSibling())
    if (firstElementSibling.isEmpty)
      None
    else
      Option(JSoupFirstDOMElement(firstElementSibling.get, parentRemoteWebDriver))
  }

  override def lastSibling = {
    val lastElementSibling = Option(searchContext.lastElementSibling())
    if(lastElementSibling.isEmpty)
      None
    else
      Option(JSoupFirstDOMElement(lastElementSibling.get, parentRemoteWebDriver))
  }

  override def child(index: Int): Option[DOMElement] = {
    try {
      val childJSoupElement: Element = searchContext.child(index)
      Option(JSoupFirstDOMElement(childJSoupElement, parentRemoteWebDriver))
    }
    catch {
      case _: IndexOutOfBoundsException => None
    }

  }

  override def siblingIndex = searchContext.elementSiblingIndex()
}
