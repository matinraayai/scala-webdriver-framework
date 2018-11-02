package tools.infrastructure.element

import java.util.regex.PatternSyntaxException

import org.jsoup.Jsoup
import org.jsoup.nodes.{Document, Element}
import org.openqa.selenium.{By, NotFoundException, SearchContext}
import org.openqa.selenium.remote.{RemoteWebDriver, RemoteWebElement}
import tools.infrastructure.enums.How
import tools.infrastructure.enums.How.How
import tools.infrastructure.parser.JSoupParsingEngine

/**
  * An implementation of [[tools.infrastructure.element.DOMElement]] where the
  * [[org.openqa.selenium.remote.RemoteWebElement]] representation of this instance is loaded first.
  *
  * @param thisAsRemoteWebElement the captured `RemoteWebElement` representation of this element
  * @param SourceElementCssSelector The CSS Selector of the element that was used to retrieve
  *                                 this element.
  * @author Matin Raayai Ardakani
  */
case class WebElementFirstDOMElement(private val thisAsRemoteWebElement: RemoteWebElement,
                                     private val SourceElementCssSelector: String)
  extends DOMElement {

  private def stringToHow(how: String): How = how match {
    case "id" => How.ID
    case "link text" => How.LINK_TEXT
    case "partial link text" => How.PARTIAL_LINK_TEXT
    case "tag name" => How.TAG_NAME
    case "name" => How.NAME
    case "class name" => How.CLASS_NAME
    case "css selector" => How.CSS
  }

  /**
    * Uses the `toString` method of `thisAsRemoteWebElement` to extract how Selenium has retrieved
    * this element before.
    * @see [[org.openqa.selenium.remote.RemoteWebElement]]'s `foundBy` field and its `toString` method.
    * @see [[org.openqa.selenium.remote.RemoteWebDriver]]'s concrete element locating methods (where
    *     it instantiates elements).
    * @throws PatternSyntaxException if for some reason, the expected format of the `toString` method
    *                                doesn't match the expected pattern.
    */
  private def extractedLocatingInfo: (How, String) = {
    try {
      val infoFromRemoteWebElement: String = thisAsRemoteWebElement.toString
      val how: String = infoFromRemoteWebElement.split("] -> ")(1).split(": ")(0)
      val locator: String = infoFromRemoteWebElement.split("] -> ")(1).split(": ")(1).split("]")(0)
      (stringToHow(how), locator)
    }
    catch {
      case _: PatternSyntaxException => throw new IllegalArgumentException(
        "Failed to extract the locator from the remoteWebElement. Please check org.openqa.remote.RemoteWebElement.")
    }
  }

  /*
  * Uses the source's css selector and the locating info extracted from the WebDriver to locate the
  * JSoup version of this element.
  */
  private lazy val thisAsjSoupElement = {
    val parsedPageSource: Document = Jsoup.parse(getParentRemoteWebDriver.getPageSource)
    val jSoupParser: JSoupParsingEngine = JSoupParsingEngine(parsedPageSource, getParentRemoteWebDriver)
    jSoupParser.findJSoupElement(extractedLocatingInfo _1, extractedLocatingInfo _2).getOrElse(throw new
        NotFoundException("Failed to convert remoteWebElement [%s] to JSoup.".format(thisAsRemoteWebElement)))
  }

  /**
    * <p> A getter operation pointing to the JSoup instance of this element used in internal trait
    * operations when needed.</p>
    * <h5>Important Points:</h5>
    * <p>
    * <ul>
    *   <li>Decouples the internals of the concrete classes from the traits; That means you are
    *   free to implement it however you want in a concrete class (save the instance as a val, var, lazy val,
    *   or even not save it at all).</li>
    *   <li>Only needs to be implemented in the concrete class.</li>
    *   <li> If another trait also needs this getter, all of them need to <b>use the same function
    *   signature</b> or else the same thing will need to be defined more than once in the concrete class.
    * </ul>
    * </p>
    * @return the JSoup [[org.jsoup.nodes.Element]] representation of this trait.
    */
  protected def getThisAsJSoupElement: Element = this.thisAsjSoupElement

  /**
    * @return `thisAsRemoteWebElement`'s `parentRemoteWebDriver`.
    * @throws IllegalArgumentException if it fails to convert the WebDriver interface returned
    *                                  by the remoteWebElement to an instance of [[org.openqa.selenium.remote.RemoteWebDriver]]
    *
    */
  protected def getParentRemoteWebDriver: RemoteWebDriver =
    this.getThisAsRemoteWebElement.getWrappedDriver match {
      case r: RemoteWebDriver => r
      case _: Any => throw new IllegalArgumentException("Failed to convert the WebDriver interface to " +
        "RemoteWebDriver.\n")
    }

  protected[tools] def getThisAsRemoteWebElement: RemoteWebElement = this.thisAsRemoteWebElement

  override def toString: String = "WebElementFirstShrineElement:" + super.toString

  override protected def getThisAsWebDriverSearchContext: SearchContext = thisAsRemoteWebElement
}
