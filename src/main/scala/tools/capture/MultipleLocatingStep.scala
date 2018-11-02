package tools.capture

import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator
import tools.infrastructure.enums.How.How

import scala.collection.mutable

case class MultipleLocatingStep(private val how: How, private val locator: String) {

  private var runnable: DomElementLocator => mutable.Buffer[DOMElement] = _

  private[capture] def getLocatorString: String = locator

  private[capture] def setRunnable(newValue: DomElementLocator => mutable.Buffer[DOMElement]): Unit = {
    runnable = newValue
  }

  def setVars(args: Any*): Unit = locator.format(args)

  def run(v1: DomElementLocator) = runnable(v1)

}
