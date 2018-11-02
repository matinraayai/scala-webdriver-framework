package tools.infrastructure.`trait`

import tools.infrastructure.element.{JSoupFirstDOMElement, DOMElement, WebElementFirstDOMElement}
import org.jsoup.nodes._
import org.jsoup.select._
import org.openqa.selenium.remote.RemoteWebDriver

import scala.collection.JavaConverters._
import scala.collection.mutable


/**
  * Contains all the capabilities specific to [[org.jsoup.nodes.Element]]
  */
trait JSoupSpecifics {

  //TODO Add more responsibilities from a JSoup element

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
  protected def getThisAsJSoupElement: Element

  protected def getParentRemoteWebDriver: RemoteWebDriver

  def baseUri: String = getThisAsJSoupElement.baseUri()

  def childNodeSize(): Int = getThisAsJSoupElement.childNodeSize()

  def textNodes: mutable.Buffer[TextNode] = getThisAsJSoupElement.textNodes.asScala

  def dataNodes: mutable.Buffer[DataNode] = getThisAsJSoupElement.dataNodes.asScala

  def is(cssQuery: String): Boolean = getThisAsJSoupElement.is(cssQuery)

  def is(evaluator: Evaluator): Boolean = getThisAsJSoupElement.is(evaluator)

  def getAllElements: mutable.Buffer[DOMElement] = {
    val allElements: mutable.Buffer[Element] = getThisAsJSoupElement.getAllElements.asScala
    allElements.map(i => JSoupFirstDOMElement(i, getParentRemoteWebDriver))
  }

  def textFromJSoup: String = getThisAsJSoupElement.text

  def ownTextFromJSoup: String = getThisAsJSoupElement.ownText

  def hasText: Boolean = getThisAsJSoupElement.hasText

  def data: String = getThisAsJSoupElement.data

  def className: String = getThisAsJSoupElement.className

  def classNames: mutable.Set[String] = getThisAsJSoupElement.classNames().asScala

  def hasClass(className: String): Boolean = getThisAsJSoupElement.hasClass(className)

  def value: String = getThisAsJSoupElement.`val`

  def html: String = getThisAsJSoupElement.html

  def attributeFromJSoup(attributeKey: String): String = getThisAsJSoupElement.attr(attributeKey)

  def attributesFromJSoup: Attributes = getThisAsJSoupElement.attributes

  def hasAttributeFromJSoup(attributeKey: String): Boolean =
    getThisAsJSoupElement.hasAttr(attributeKey)

}
