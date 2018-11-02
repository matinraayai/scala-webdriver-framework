package tools.infrastructure.`trait`

import org.openqa.selenium.interactions.internal.Coordinates
import org.openqa.selenium.remote.RemoteWebElement
import org.openqa.selenium.support.How
import org.openqa.selenium._

import scala.collection.mutable
import scala.collection.JavaConverters._

/**
  * As the name implies, represents a trait with all the expected responsibilities from an instance
  * of a `RemoteWebElement`. If there are any similarities between a responsibility in here and a
  * responsibility in a `JSoupElementResponsibilities`, they have been explicitly mentioned in the
  * method's name.
  */
trait WebElementTrait {

  protected val selfCssSelector: String

  //TODO Confirm all the methods are delegated.
  //TODO Use the toString method in webElement to get rid of passing in the locator descriptor.

  protected[tools] def getThisAsRemoteWebElement: RemoteWebElement

  def getRemoteWebElementId: String = getThisAsRemoteWebElement.getId

  def setRemoteWebId(id: String): Unit = getThisAsRemoteWebElement.setId(id)

  def click(): Unit = getThisAsRemoteWebElement.click()

  def submit(): Unit = getThisAsRemoteWebElement.submit()

  def sendKeys(keysToSend: String): Unit =
    getThisAsRemoteWebElement.sendKeys(keysToSend)

  def clear(): Unit = getThisAsRemoteWebElement.clear()

  def isSelected: Boolean = getThisAsRemoteWebElement.isSelected

  def isEnabled: Boolean = getThisAsRemoteWebElement.isEnabled

  def getTextFromWebDriver: String = getThisAsRemoteWebElement.getText

  def getCssValue(propertyName: String): String = getThisAsRemoteWebElement.getCssValue(propertyName)

  def isDisplayed: Boolean = getThisAsRemoteWebElement.isDisplayed

  def getLocation: Point = getThisAsRemoteWebElement.getLocation

  def getSize: Dimension = getThisAsRemoteWebElement.getSize

  def getRect: Rectangle = getThisAsRemoteWebElement.getRect

  def getCoordinates: Coordinates = getThisAsRemoteWebElement.getCoordinates

  def getScreenshotAs[X](outputType: OutputType[X]): X = getThisAsRemoteWebElement.getScreenshotAs(outputType)

  def getAttributeFromWebDriver(name: String): String = getThisAsRemoteWebElement.getAttribute(name)

  def getTagNameFromWebDriver: String = getThisAsRemoteWebElement.getTagName
}
