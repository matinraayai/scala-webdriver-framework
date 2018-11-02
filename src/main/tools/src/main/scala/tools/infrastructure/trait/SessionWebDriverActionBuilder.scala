package tools.infrastructure.`trait`

import java.time.Duration

import org.openqa.selenium.Keys
import tools.infrastructure.element.DOMElement
import org.openqa.selenium.interactions.{Actions, MoveTargetOutOfBoundsException}
import org.openqa.selenium.remote.RemoteWebDriver


/**
  * Includes any kind of selenium Actions related to a Session object. A user can cascade
  * actions on top of each other and then call the `perform()` method to send it to the browser.
  * @author Matin Raayai Ardakani
  */
trait SessionWebDriverActionBuilder {

  protected def getParentRemoteWebDriver: RemoteWebDriver

  private lazy val actions: Actions = new Actions(getParentRemoteWebDriver)

  /**
    * Performs a <u>modifier key press</u>. Does not release the modifier key - subsequent interactions
    * may assume it's kept pressed.
    * Note that the modifier key is <b>never</b> released implicitly - either
    * <i>keyUp(theKey)</i> or <i>sendKeys(Keys.NULL)</i>
    * must be called to release the modifier.
    *
    * @param key Either { @link Keys#SHIFT}, { @link Keys#ALT} or { @link Keys#CONTROL}. If the
    *                           provided key is none of those, { @link IllegalArgumentException} is thrown.
    * @return A self reference.
    */
  def keyDown(key: Keys): SessionWebDriverActionBuilder = {
    actions.keyDown(key)
    this
  }

  /**
    * Performs a modifier key release. Releasing a non-depressed modifier key will yield undefined
    * behaviour.
    *
    * @param key Either { @link Keys#SHIFT}, { @link Keys#ALT} or { @link Keys#CONTROL}.
    * @return A self reference.
    */
  def keyUp(key: Keys): SessionWebDriverActionBuilder = {
    actions.keyUp(key)
    this
  }

  /**
    * Sends keys to the active element. This differs from calling
    * {@link WebElement#sendKeys(CharSequence...)} on the active element in two ways:
    * <ul>
    * <li>The modifier keys included in this call are not released.</li>
    * <li>There is no attempt to re-focus the element - so sendKeys(Keys.TAB) for switching
    * elements should work. </li>
    * </ul>
    *
    * @see WebElement#sendKeys(CharSequence...)
    * @param keys The keys.
    * @return A self reference.
    */
  def sendKeys(keys: CharSequence*): SessionWebDriverActionBuilder = {
    keys.map(actions.sendKeys(_))
    this
  }

  /**
    * Clicks (without releasing) at the current mouse location.
    *
    * @return A self reference.
    */
  def clickAndHold: SessionWebDriverActionBuilder = {
    actions.clickAndHold()
    this
  }

  /**
    * Releases the depressed left mouse button at the current mouse location.
    *
    * @see #release(org.openqa.selenium.WebElement)
    * @return A self reference.
    */
  def release: SessionWebDriverActionBuilder = {
    actions.release()
    this
  }

  /**
    * Clicks at the current mouse location. Useful when combined with
    * {@link #moveToElement(org.openqa.selenium.WebElement, int, int)} or
    * {@link #moveByOffset(int, int)}.
    *
    * @return A self reference.
    */
  def click: SessionWebDriverActionBuilder = {
    actions.click()
    this
  }

  /**
    * Performs a double-click at the current mouse location.
    *
    * @return A self reference.
    */
  def doubleClick: SessionWebDriverActionBuilder = {
    actions.doubleClick()
    this
  }

  /**
    * Moves the mouse from its current position (or 0,0) by the given offset. If the coordinates
    * provided are outside the viewport (the mouse will end up outside the browser window) then
    * the viewport is scrolled to match.
    *
    * @param xOffset horizontal offset. A negative value means moving the mouse left.
    * @param yOffset vertical offset. A negative value means moving the mouse up.
    * @return A self reference.
    * @throws MoveTargetOutOfBoundsException if the provided offset is outside the document's
    *                                        boundaries.
    */
  def moveByOffset(xOffset: Int, yOffset: Int): SessionWebDriverActionBuilder = {
    actions.moveByOffset(xOffset, yOffset)
    this
  }

  /**
    * Performs a context-click at the current mouse location.
    *
    * @return A self reference.
    */
  def contextClick: SessionWebDriverActionBuilder = {
    actions.contextClick()
    this
  }

  /**
    * A convenience method that performs click-and-hold at the location of the source element,
    * moves to the location of the target element, then releases the mouse.
    *
    * @param source element to emulate button down at.
    * @param target element to move to and release the mouse at.
    * @return A self reference.
    */
  def dragAndDrop(source: DOMElement, target: DOMElement): SessionWebDriverActionBuilder = {
    actions.dragAndDrop(source.getThisAsRemoteWebElement, target.getThisAsRemoteWebElement)
    this
  }

  def pause(duration: Duration): SessionWebDriverActionBuilder = {
    actions.pause(duration)
    this
  }

  /**
    * Should be called after cascading the desired WebDriver Actions.
    */
  def perform(): Unit = actions.build.perform()
}
