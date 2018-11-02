package tools.component

import tools.infrastructure.browser.Session
import tools.infrastructure.`trait`.DomElementLocator
import org.openqa.selenium.NoSuchElementException


/**
  * Represents a general instance of a user-flow state in a web application that has defined
  * user-activity attached to it.
  */
trait Page extends Model {
  waitUntilVitalElementsAreLoaded()

  protected val session: Session

  override def captureModelDomainElement: DomElementLocator = session

  /**
    * A special version of `waitUntilElementsAreLoaded` method that checks for any infastructure.component that
    * is considered vital to a page's functionality.
    *
    * @throws NoSuchElementException if one of the specified elements is not found.
    */
  def waitUntilVitalElementsAreLoaded(): Unit

}


