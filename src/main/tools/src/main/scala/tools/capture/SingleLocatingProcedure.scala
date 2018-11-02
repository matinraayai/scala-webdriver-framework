package tools.capture

import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator
import org.openqa.selenium.NotFoundException

import scala.collection.mutable


/**
  * Represents a "Single Element Locating procedure or "SELP" for short. Provides user with a sequence of
  * "Locating Steps"(locating a [[DOMElement]] off of a [[DomElementLocator]]) that can be used to capture a
  * single element.
 *
  * @param firstStep for capturing the element.
  */
case class SingleLocatingProcedure(firstStep: SingleLocatingStep) {

  //Steps for reaching the element.
  private lazy val steps: Seq[SingleLocatingStep] = Seq(firstStep)

  /**
    * Adds the rhs to the [[steps]].
 *
    * @param rhs A single element locating step in the form of [[DomElementLocator]] => [[DOMElement]].
    * @return self
    */
  def -->(rhs: SingleLocatingStep): SingleLocatingProcedure = {
    steps :+ rhs
    this
  }

  /**
    * Creates a [[MultipleLocatingPath]] or a "Multiple Element Locating Procedure" using the [[steps]] and rhs.
    *
    * @param rhs A Multiple Element Locating Step or [[DomElementLocator]] => mutable.Buffer[[DOMElement]]
    * @return An [[MultipleLocatingPath]]
    */
  def -->(rhs: MultipleLocatingStep): MultipleLocatingPath = {
    MultipleLocatingPath(Option(this), rhs)
  }

  /**
    * Creates an [[OptionalLocatingPath]] that uses [[steps]] to locate the [[DomElementLocator]] right before the last step
    * and then uses rhs as its last step.
 *
    * @param rhs an Optional Element Locating Step in the form of [[DomElementLocator]] => Option[[DOMElement]]
    * @return an [[OptionalLocatingPath]]
    */
  def -->(rhs: OptionalLocatingStep): OptionalLocatingPath = {
    OptionalLocatingPath(Option(this), rhs)
  }

  def setVars(stepNumber: Int, args: Any*): SingleLocatingProcedure = {
    steps(stepNumber).setVars(args)
    this
  }

  def length: Int = steps.length

  /**
    * Walks through the sequence of steps to retrieve the desired element.
 *
    * @param source the strating [[DomElementLocator]] instance.
    * @return the desired [[DOMElement]]
    * @throws NotFoundException in case one of the steps fails for some reason.
    */
  def run(source: DomElementLocator): DOMElement = {
    steps.foldLeft(source)((elementFinder, current) => current.run(elementFinder)) match {
      case s: DOMElement => s
    }
  }
}
