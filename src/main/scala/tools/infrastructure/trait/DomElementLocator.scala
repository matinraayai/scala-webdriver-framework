package tools.infrastructure.`trait`

import tools.infrastructure.element.{DOMElement, JSoupFirstDOMElement}
import tools.infrastructure.enums.How.How
import tools.infrastructure.enums.ParserEngine
import tools.infrastructure.enums.ParserEngine.ParserEngine
import tools.infrastructure.parser.{DOMParsingEngine, JSoupParsingEngine, SeleniumParsingEngine}
import org.jsoup.nodes.Element
import org.openqa.selenium._
import org.openqa.selenium.remote.{RemoteWebDriver, RemoteWebElement}

import scala.collection.mutable
import scala.collection.JavaConverters._


/**
  * Enables its inheritor to locate elements in an HTML context. An idea borrowed from the
  * [[SearchContext]] interface.
  * Any element finding ability from [[org.openqa.selenium]] or [[org.jsoup]] should be included
  * here.
  * @author Matin Raayai Ardakani
  */
trait DomElementLocator {

  /**
    * @return The domain of this element (a [[RemoteWebElement]] or a [[RemoteWebElement]]) as a
    *         WebDriver [[SearchContext]]. Since [[SearchContext]] is implemented by both
    *         [[RemoteWebElement]] and [[RemoteWebDriver]] and this interface contains the famous
    *         functions <code>findElement(by: By)</code> and <code>findElements(by: By)</code>, this
    *         interface can be used to generify the process of locating a [[RemoteWebElement]].
    */
  protected def getThisAsWebDriverSearchContext: SearchContext

  protected def getParentRemoteWebDriver: RemoteWebDriver

  protected def getThisAsJSoupElement: Element

  protected lazy val selfCssSelector: String = getThisAsJSoupElement.cssSelector()

  private def getParser(parser: ParserEngine): DOMParsingEngine = parser match {
    case ParserEngine.WEBDRIVER => SeleniumParsingEngine(getThisAsWebDriverSearchContext, selfCssSelector)
    case ParserEngine.JSOUP => JSoupParsingEngine(getThisAsJSoupElement, getParentRemoteWebDriver)
  }

  private def getParserPriorityList: mutable.Seq[DOMParsingEngine] =
    ParserEngine.getPriorityList.map(getParser)

  def findElement(how: How, locator: String): Option[DOMElement] = {
    getParserPriorityList.collectFirst(
      {
        case i: DOMParsingEngine if i.findElement(how, locator).isDefined =>
          i.findElement(how, locator).get
      })
  }

  def findElement(locatingTupleSeq: (How, String)*): Option[DOMElement] = {
    getParserPriorityList.collectFirst(
      {
        case i: DOMParsingEngine if i.findElement(locatingTupleSeq: _*).isDefined =>
        i.findElement(locatingTupleSeq: _*).get
      })
  }

  def findElement(parser: ParserEngine, how: How, locator: String): Option[DOMElement] =
    getParser(parser).findElement(how, locator)

  def findElement(parser: ParserEngine, locatingTupleSeq: (How, String)*): Option[DOMElement] =
    getParser(parser).findElement(locatingTupleSeq: _*)

  def findElements(how: How, locator: String): mutable.Buffer[DOMElement] = {
    getParserPriorityList.collectFirst(
      {
        case i: DOMParsingEngine if i.findElements(how, locator).nonEmpty =>
          i.findElements(how, locator)
      }).getOrElse(mutable.Buffer())
  }

  def findElements(parser: ParserEngine, how: How, locator: String): mutable.Buffer[DOMElement] =
    getParser(parser).findElements(how, locator)

  def parent(parser: ParserEngine): Option[DOMElement] =
    getParser(parser).parent()

  def parent(): Option[DOMElement] =
    getParserPriorityList.collectFirst({
      case i: DOMParsingEngine if i.parent().isDefined =>
        i.parent().get
    })

  def parents(parser: ParserEngine): mutable.Buffer[DOMElement] =
    getParser(parser).parents()

  def parents(): mutable.Buffer[DOMElement] = ???

  def child(parser: ParserEngine, index: Int): Option[DOMElement] = getParser(parser).child(index)

  def child(index: Int): Option[DOMElement] = ???

  def children(parser: ParserEngine): mutable.Buffer[DOMElement] = getParser(parser).children()

  def children(): mutable.Buffer[DOMElement] = ???

  def sibling(parser: ParserEngine, index: Int): Option[DOMElement] = getParser(parser).sibling(index)

  def siblings(parser: ParserEngine): mutable.Buffer[DOMElement] = getParser(parser).siblings()

  def firstSibling(parser: ParserEngine): Option[DOMElement] = getParser(parser).firstSibling

  def lastSibling(parser: ParserEngine): Option[DOMElement] = getParser(parser).lastSibling

}
