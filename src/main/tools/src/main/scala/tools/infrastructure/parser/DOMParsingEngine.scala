package tools.infrastructure.parser

import tools.infrastructure.element.DOMElement
import tools.infrastructure.enums.How.How
import org.openqa.selenium.By
import org.openqa.selenium.support.pagefactory.ByAll

import scala.collection.mutable

trait DOMParsingEngine {

  def findElement(how: How, locator: String): Option[DOMElement]

  def findElement(locatorSeq: (How, String)*): Option[DOMElement]

  def findElements(how: How, locator: String): mutable.Buffer[DOMElement]

  /**
    * @return this Element's parent Node from JSoup as `ShrineElement` wrapped in
    *         `Option`. None if the parent Node is null.
    */
  def parent(): Option[DOMElement]

  /**
    * Get this element's parent and ancestors, up to the document root.
    * @return this element's stack of parents, closest first.
    */
  def parents(): mutable.Buffer[DOMElement]

  /**
    * Get sibling elements. If the element has no sibling elements, returns an empty list.
    * An element is not a sibling of itself, so will not be included in the returned list.
    * @return sibling elements
    */
  def siblings(): mutable.Buffer[DOMElement]

  def sibling(index: Int): Option[DOMElement]

  def siblingIndex: Int

  def nextSibling: Option[DOMElement]

  def previousSibling: Option[DOMElement]

  def firstSibling: Option[DOMElement]

  /**
    * Gets the last element sibling of this element, wrapped in Option.
    * @return the last sibling that is an element (aka the parent's last element child), None if
    *         nothing is found.
    */
  def lastSibling: Option[DOMElement]

  def children(): mutable.Buffer[DOMElement]

  /**
    * Get a child element of this element, by its 0-based index number.
    * Note that an element can have both mixed Nodes and Elements as children. This method inspects
    * a filtered list of children that are elements, and the index is based on that filtered list.
    * @param index the index number of the element to retrieve
    * @return the child element wrapped in Option, if it exists, otherwise None
    **/
  def child(index: Int): Option[DOMElement]

}
