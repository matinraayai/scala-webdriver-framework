package tools.component

import tools.infrastructure.browser.Session
import tools.infrastructure.element.{DOMElement, WebElementFirstDOMElement}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.`trait`.DomElementLocator
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.support.How
import org.openqa.selenium.support.ui.Select

import scala.collection.JavaConverters._
import scala.collection.mutable

case class SelectDropDown(protected val session: Session, protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  private def createSelect: Select = new Select(captureModelDomainElement.getThisAsRemoteWebElement)

  def deselectByValue(value: String): Unit = createSelect.deselectByValue(value)

  def selectByVisibleText(text: String): Unit = createSelect.selectByVisibleText(text)

  def deselectByIndex(index: Int): Unit = createSelect.deselectByIndex(index)

  def deselectByVisibleText(text: String): Unit = createSelect.deselectByVisibleText(text)

  def selectByValue(value: String): Unit = createSelect.selectByValue(value)

  def getAllSelectedOptions: mutable.Buffer[DOMElement] = {
    createSelect.getAllSelectedOptions.asScala.map {
      case e: RemoteWebElement => WebElementFirstDOMElement(e, captureModelDomainElement.cssSelector)
    }
  }

  def deselectAll(): Unit = createSelect.deselectAll()

  def isMultiple: Boolean = createSelect.isMultiple

  def selectByIndex(index: Int): Unit = createSelect.selectByIndex(index)

  def getFirstSelectedOption: DOMElement = createSelect.getFirstSelectedOption match {
    case e: RemoteWebElement =>
      WebElementFirstDOMElement(e, captureModelDomainElement.cssSelector)
  }

  def getOptions: mutable.Buffer[DOMElement] = {
    createSelect.getOptions.asScala.map {
      case e: RemoteWebElement => WebElementFirstDOMElement(e, captureModelDomainElement.cssSelector)
    }
  }
}
