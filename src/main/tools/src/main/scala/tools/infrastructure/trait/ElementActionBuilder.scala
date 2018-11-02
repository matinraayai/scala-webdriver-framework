package tools.infrastructure.`trait`


import org.openqa.selenium.Keys
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.remote.{RemoteWebDriver, RemoteWebElement}

/**
  * Enables its subclass to build various kinds of element specific actions.
  * Is a wrapper around [[org.openqa.selenium.interactions.Actions]].
  * One can build a chain of action using this class and perform them by calling the `perform()` method.
  * Documentation is pasted from Selenium with additional info if needed.
  */
trait ElementActionBuilder {

  protected def getParentRemoteWebDriver: RemoteWebDriver

  protected[tools] def getThisAsRemoteWebElement: RemoteWebElement
  
  private lazy val actions: Actions = new Actions(getParentRemoteWebDriver)

  /**
    * Performs a <b>modifier</b> key press. Does not release the modifier key - subsequent interactions
    * may assume it's kept pressed.
    * Note that the modifier key is <b>never</b> released implicitly - either
    * `keyUp(theKey)` or `endKeys(Keys.NULL)` must be called to release the modifier.
    *
    * @param key Either [[org.openqa.selenium.Keys.SHIFT]], [[org.openqa.selenium.Keys.ALT]] or 
    *            [[org.openqa.selenium.Keys.CONTROL]]. 
    * @throws IllegalArgumentException If the provided key is none of those.
    * @return A self reference.
    */
  def modifierKeyDown(key: Keys): ElementActionBuilder = {
    actions.keyDown(getThisAsRemoteWebElement, key)
    this
  }
  
  /**
    * Performs a modifier key release after focusing on self. Equivalent to:
    * <i>click(self).sendKeys(theKey);</i>
    *
    * @see #keyUp(CharSequence) on behaviour regarding non-depressed modifier keys.
    * @param key Either { @link Keys#SHIFT}, { @link Keys#ALT} or { @link Keys#CONTROL}.
    * @return A self reference.
    */
  def keyUp(key: CharSequence): ElementActionBuilder = {
    actions.keyUp(this.getThisAsRemoteWebElement, key)
    this
  }

  /**
    * Equivalent to calling:
    * <i>Actions.click(element).sendKeys(keysToSend).</i>
    * This method is different from {@link WebElement#sendKeys(CharSequence...)} - see
    * {@link #sendKeys(CharSequence...)} for details how.
    *
    * @see #sendKeys(java.lang.CharSequence[])
    * @param keys   The keys.
    * @return A self reference.
    */
  def lefClickOnSelfThenSendKeys(keys: CharSequence*): ElementActionBuilder = {
    keys.map(actions.sendKeys(this.getThisAsRemoteWebElement, _))
    this
  }

  /**
    * Clicks (without releasing) in the middle of the given element. This is equivalent to:
    * <i>Actions.moveToElement(onElement).clickAndHold()</i>
    * @return A self reference.
    */
  def clickAndHold(): ElementActionBuilder = {
    actions.clickAndHold(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * Releases the depressed left mouse button, in the middle of the given element.
    * This is equivalent to:
    * <i>Actions.moveToElement(onElement).release()</i>
    *
    * Invoking this action without invoking {@link #clickAndHold()} first will result in
    * undefined behaviour.
    * @return A self reference.
    */
  def release(): ElementActionBuilder = {
    actions.release(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * Clicks in the middle of the given element. Equivalent to:
    * <i>Actions.moveToElement(onElement).click()</i>
    *
    * @param target Element to click.
    * @return A self reference.
    */
  def clickWithAction(): ElementActionBuilder = {
    actions.click(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * Performs a double-click at middle of the given element. Equivalent to:
    * <i>Actions.moveToElement(element).doubleClick()</i>
    *
    * @param target Element to move to.
    * @return A self reference.
    */
  def doubleClick(): ElementActionBuilder = {
    actions.doubleClick(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * Moves the mouse to the middle of the element. The element is scrolled into view and its
    * location is calculated using getBoundingClientRect.
    *
    * @param target element to move to.
    * @return A self reference.
    */
  def moveToThis(): ElementActionBuilder = {
    actions.moveToElement(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * Moves the mouse to an offset from the top-left corner of the element.
    * The element is scrolled into view and its location is calculated using getBoundingClientRect.
    *
    * @param target  element to move to.
    * @param xOffset Offset from the top-left corner. A negative value means coordinates left from
    *                the element.
    * @param yOffset Offset from the top-left corner. A negative value means coordinates above
    *                the element.
    * @return A self reference.
    */
  def moveToThisWithOffset(xOffset: Int, yOffset: Int): ElementActionBuilder = {
    actions.moveToElement(this.getThisAsRemoteWebElement, xOffset, yOffset)
    this
  }



  /**
    * Performs a context-click at middle of the given element. First performs a mouseMove
    * to the location of the element.
    * @return A self reference.
    */
  def contextClick(): ElementActionBuilder = {
    actions.contextClick(this.getThisAsRemoteWebElement)
    this
  }

  /**
    * A convenience method that performs click-and-hold at the location of the self,
    * moves by a given offset, then releases the mouse.
    * @param xOffset horizontal move offset.
    * @param yOffset vertical move offset.
    * @return A self reference.
    */
  def dragAndDropBy(xOffset: Int, yOffset: Int): ElementActionBuilder = {
    actions.dragAndDropBy(this.getThisAsRemoteWebElement, xOffset, yOffset)
    this
  }

  /**
    * A convenience method for performing the actions without calling build() first.
    */
  def perform(): Unit = actions.perform()

}
