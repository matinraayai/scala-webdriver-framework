package tools.capture
import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator
import tools.infrastructure.enums.How.How

case class SingleLocatingStep private[capture](private val how: How, private val locator: String) {

  private var runnable: DomElementLocator => DOMElement = _

  def getLocatorString: String = locator

  def setRunnable(newValue: DomElementLocator => DOMElement): Unit = {
    runnable = newValue
  }

  def setVars(args: Any*): Unit = locator.format(args)

  def run(v1: DomElementLocator) = runnable(v1)
}
