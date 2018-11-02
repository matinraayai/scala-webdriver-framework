package tools.infrastructure.wait

import java.util.regex.Pattern

import tools.infrastructure.browser.Session
import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator
import org.openqa.selenium.{Alert, StaleElementReferenceException}
import tools.infrastructure.enums.How.How
import tools.infrastructure.enums.ParserEngine

import scala.collection.mutable

/**
  * Holds commonly used wait conditions to be used with the [[tools.infrastructure.wait.Wait]] object. When requested,
  * The [[tools.infrastructure.wait.ConditionWaiter]] applies this condition until it either gets a result or
  * times out.
  * Each condition is an instance of [[Function0]] with an optional `toString` method describing what the condition
  * does for debugging purposes.
  * The conditions have to explicitly specify which exceptions should be ignored by catching them and returning null.
  * This closely resembles the [[org.openqa.selenium.support.ui.ExpectedConditions]], but using the current
  * implementation, the user has been decoupled from the need to pass in a [[Function1]] with a [[org.openqa.selenium.WebDriver]]
  * input argument.
  */
object Conditions {
  //__________________________________Session Exclusive Conditions:________________________________//


  /**
    * @param session Session object of interest.
    * @param title the expected page title.
    * @return A runnable that checks whether the session's current title is equals to the title
    *         string passed to it.
    */
  def pageTitleBecomes(session: Session, title: String): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = session.getCurrentPageTitle == title

    override def toString(): String =
      s"Current page title becomes [$title] in [$session]."
  }

  /**
    * @param session Session object of interest.
    * @param title the expected page title.
    * @return A runnable that checks whether the session's current title contains the title
    *         string passed to it.
    */
  def pageTitleContains(session: Session, title: String): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = session.getCurrentPageTitle.contains(title)

    override def toString(): String = s"Current page title contains [$title] in [$session]."

  }

  /**
    * @param session Session object of interest.
    * @param url the expected page url.
    * @return A runnable that checks whether the session's current url matches the url
    *         string passed to it.
    */
  def urlBecomes(session: Session, url: String): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = session.currentURLFromWebDriver == url

    override def toString(): String = s"Current URL becomes [$url] in [$session]."
  }

  /**
    * @param session Session object of interest.
    * @param url the expected page url.
    * @return A runnable that checks whether the session's current url contains the url
    *         string passed to it.
    */
  def urlContains(session: Session, url: String): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = session.currentURLFromWebDriver.contains(url)

    override def toString(): String = s"Current URL contains [$url] in [$session]."
  }

  /**
    * @param session Session object of interest.
    * @param regex the expected regular expression to appear in the URL.
    * @return A runnable that checks whether the session's current url contains the passed regular
    *         expressions.
    */
  def urlMatches(session: Session, regex: String): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = {
      val currentUrl = session.currentURLFromWebDriver
      val pattern = Pattern.compile(regex)
      val matcher = pattern.matcher(currentUrl)
      matcher.find
    }

    override def toString(): String = s"Current URL matches [$regex] in [$session]."
  }

  //TODO: Add window conditions here.

  /**
    * @param session Session object of interest.
    * @param index the e0 based index of the frame to be switched to.
    * @return A runnable that attempts to switch to the frame using the given index.
    */
  def frameAppearsAndGetsSwitchedTo(session: Session, index: Int): () => Boolean = new (() => Boolean) {
    override def apply(): Boolean = session.switchToFrame(index)

    override def toString(): String = s"Frame with index [$index] exists and gets switched to by [$session]."
  }

  /**
    * @param session Session object of interest.
    * @return A runnable that attempts to switch to the Javascript alert.
    */
  def alertAppearsAndGetsSwitchedTo(session: => Session): () => Alert = new (() => Alert) {
    override def apply(): Alert = session.switchToAlert.orNull

    override def toString(): String = s"Browser alert appears and gets switched to in [$session]."
  }

  //__________________________________WebDriver Locating Exclusive Conditions:_____________________//

  /**
    * @param context Context of the search as [[tools.infrastructure.`trait`.DomElementLocator]].
    * @param how     method of element look-up.
    * @param locator locator information string to be used with the method.
    * @return A runnable that attempts to locate an element with the given information using the WebDriver.
    */
  def webElementIsPresent(context: => DomElementLocator, how: How, locator: String): () => DOMElement =
    new (() => DOMElement) {
      override def apply(): DOMElement =context.findElement(ParserEngine.WEBDRIVER, how, locator).orNull

      override def toString(): String = s"Element with ($how, $locator) is present in [$context]."
  }

  /**
    * @param context  Context of the search as [[tools.infrastructure.`trait`.DomElementLocator]].
    * @param locators A series of locating information to be used for locating the element, similar to
    *                 [[org.openqa.selenium.support.pagefactory.ByAll]]
    * @return A runnable that attempts to locate an element with the given information, using the WebDriver.
    */
  def webElementIsPresent(context: => DomElementLocator, locators: (How, String)*): () => DOMElement = () => {
    context.findElement(ParserEngine.WEBDRIVER, locators: _*).orNull
  }

  /**
    * @param context Context of the search as [[tools.infrastructure.`trait`.DomElementLocator]].
    * @param how     method of element look-up.
    * @param locator locator information string to be used with the method.
    * @return A runnable that attempts to locate multiple elements with the given information using the WebDriver.
    */
  def webElementsArePresent(context: => DomElementLocator, how: How, locator: String): () => mutable.Buffer[DOMElement] =
    () => {
      val presentElements = context.findElements(ParserEngine.WEBDRIVER, how, locator)
      if(presentElements.nonEmpty) presentElements else null
  }

  def webElementIsVisible(context: => DomElementLocator, how: How, locator: String): () => DOMElement = () => {
    val element = context.findElement(ParserEngine.WEBDRIVER, how, locator).filter(_.isDisplayed)
    element.get
  }

  def webElementsAreVisible(context: => DomElementLocator, how: How, locator: String): () => mutable.Buffer[DOMElement] = () => {
    val outcome = context.findElements(ParserEngine.WEBDRIVER, how, locator)
    if(outcome.nonEmpty) outcome else null
  }

  def webElementIsAbsent(context: => DomElementLocator, how: How, locator: String): () => Boolean = () => {
    context.findElement(ParserEngine.WEBDRIVER, how, locator).isEmpty
  }

  def webElementInvisibility(context: => DomElementLocator, how: How, locator: String): () => Boolean = () => {
    !context.findElement(ParserEngine.WEBDRIVER, how, locator).exists(_.isDisplayed)
  }

  def elementIsEnabled(element: => DOMElement): () => Boolean = () => {
    val output = element.isEnabled && element.isDisplayed
    output
  }

  def elementIsDisabled(element: => DOMElement, how: How, locator: String): () => Boolean = () => {
    !element.isEnabled && element.isDisplayed
  }

  def elementSelectionStateToBe(element: => DOMElement, how: How, locator: String, selected: Boolean): () => Boolean =
    () => {
    element.isSelected == selected
  }

  def textToBePresentInWebElement(element: => DOMElement, text: String): () => Boolean = () => {
    element.getTextFromWebDriver.contains(text)
  }

  def textOfWebElementBecomes(element: => DOMElement, text: String): () => Boolean = () => {
    element.getTextFromWebDriver == text
  }

  def attributeOfWebElementBecomes(element: => DOMElement, attribute: String, text: String): () => Boolean = () => {
    element.getAttributeFromWebDriver(attribute) == text
  }

  def numberOfWebElementsPresentToBeMoreThan(context: => DomElementLocator, how: How, locator: String, value: Int):
  () => mutable.Buffer[DOMElement] = () => {
    val outcome = context.findElements(ParserEngine.WEBDRIVER, how, locator)
    if(outcome.length > value) outcome else null
  }

  def numberOfWebElementsPresentToBeLessThan(context: => DomElementLocator, how: How, locator: String, value: Int):
  () => mutable.Buffer[DOMElement] = () => {
    val outcome = context.findElements(ParserEngine.WEBDRIVER, how, locator)
    if(outcome.length < value) outcome else null
  }

  def numberOfWebElementsPresentToBeEquals(context: => DomElementLocator, how: How, locator: String, value: Int):
  () => mutable.Buffer[DOMElement] = () => {
    val outcome = context.findElements(ParserEngine.WEBDRIVER, how, locator)
    if(outcome.length == value) outcome else null
  }

  def webElementTextMatches(element: => DOMElement, pattern: Pattern): () => Boolean = () => {
    pattern.matcher(element.getTextFromWebDriver).find
  }

  def webElementAttributeContains(element: => DOMElement, attribute: String, value: String): () => Boolean = () => {
    element.getAttributeFromWebDriver(attribute).contains(value)
  }

  def webElementBecomesVisible(element: => DOMElement): () => Boolean = () => {
    element.isDisplayed
  }

  def webElementBecomesInvisible(element: => DOMElement): () => Boolean = () => {
    !element.isDisplayed
  }

  def webElementBecomesStale(element: DOMElement): () => Boolean = () => {
    try { // Calling any method forces a staleness check
      element.isEnabled
      false
    } catch {
      case expected: StaleElementReferenceException => true
    }
  }




  //____________________________________________JSOUP___________________________________________________________________

  def JSoupElementIsPresent(context: => DomElementLocator, how: How, locator: String): () => DOMElement = () => {
    val output = context.findElement(ParserEngine.JSOUP, how, locator)
    output.get
  }

  def JSoupElementIsPresent(context: => DomElementLocator, locators: (How, String)*): () => DOMElement = () => {
    val outcome = locators.map(i => context.findElement(ParserEngine.JSOUP, i _1, i _2))
    outcome.find(_.isDefined).get.get
  }

  def JSoupElementsArePresent(context: => DomElementLocator, how: How, locator: String): () => mutable.Buffer[DOMElement] =
    () => {
      val presentElements = context.findElements(ParserEngine.JSOUP, how, locator)
      if(presentElements.nonEmpty) presentElements else null
    }

  def childrenAreLoaded(context: => DomElementLocator): () => mutable.Buffer[DOMElement] = () => {
    val children = context.children(ParserEngine.JSOUP)
    if(children.nonEmpty) children else null
  }

  def numberOfChildrenToBeLessThan(context: => DomElementLocator, value: Int):() => Boolean = () => {
    val children = context.children(ParserEngine.JSOUP)
    children.length < value
  }

  def numberOfChildrenToBeMoreThan(context: => DomElementLocator, value: Int):() => Boolean = () => {
    val children = context.children(ParserEngine.JSOUP)
    children.length > value
  }

  def numberOfChildrenToBeEqualTo(context: => DomElementLocator, value: Int): () => Boolean = () => {
    val children = context.children(ParserEngine.JSOUP)
    children.length == value
  }
}
