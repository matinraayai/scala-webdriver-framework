package tools.capture


import tools.infrastructure.`trait`.DomElementLocator
import tools.infrastructure.enums.How.How
import tools.infrastructure.enums.ParserEngine
import tools.infrastructure.wait.{Conditions, Wait}

import scala.collection.mutable

/**
  * Includes common [[CaptureScheme]]s to be used in the [[tools.component.Model]] object maps,
  * [[tools.component.Model.SMap]], [[tools.component.Model.MMap]] and
  * [[tools.component.Model.OMap]].
  */
object CaptureSchemes {

  object WebDriverPresence extends CaptureScheme {

    def waitForSingle(how: How, locator: String): SingleLocatingStep = {
      val step: SingleLocatingStep = SingleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) =>
        Wait.until(Conditions.webElementIsPresent(elementFinder, how, step.getLocatorString))
      step.setRunnable(runnable)
      step
    }

    def waitForMultiple(how: How, locator: String): MultipleLocatingStep = {
      val step: MultipleLocatingStep = MultipleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) =>
        Wait.until(Conditions.webElementsArePresent(elementFinder, how, step.getLocatorString))
      step.setRunnable(runnable)
      step
    }

    def captureSingle(how: How, locator: String): OptionalLocatingStep = {
      val step: OptionalLocatingStep = OptionalLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) =>
        elementFinder.findElement(ParserEngine.WEBDRIVER, how, step.getLocatorString)
      step.setRunnable(runnable)
      step
    }

    def captureMultiple(how: How, locator: String): MultipleLocatingStep = {
      val step: MultipleLocatingStep = MultipleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) =>
        elementFinder.findElements(ParserEngine.WEBDRIVER, how, step.getLocatorString)
      step.setRunnable(runnable)
      step
    }

  }

  object WebDriverVisibility extends CaptureScheme {
      def waitForSingle(how: How, locator: String): SingleLocatingStep = {
        val step: SingleLocatingStep = SingleLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          Wait.until(Conditions.webElementIsVisible(elementFinder, how, step.getLocatorString))
        step.setRunnable(runnable)
        step
      }

      def waitForMultiple(how: How, locator: String): MultipleLocatingStep = {
        val step: MultipleLocatingStep = MultipleLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          Wait.until(Conditions.webElementsArePresent(elementFinder, how, step.getLocatorString))
        step.setRunnable(runnable)
        step
      }

      def captureSingle(how: How, locator: String): OptionalLocatingStep = {
        val step: OptionalLocatingStep = OptionalLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          elementFinder.findElement(ParserEngine.WEBDRIVER, how, step.getLocatorString).filter(_.isDisplayed)
        step.setRunnable(runnable)
        step
      }

      override def captureMultiple(how: How, locator: String): MultipleLocatingStep = {
        val step: MultipleLocatingStep = MultipleLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          elementFinder.findElements(ParserEngine.WEBDRIVER, how, step.getLocatorString).filter(_.isDisplayed)
        step.setRunnable(runnable)
        step
      }
    }

  object JSoupPresence extends CaptureScheme {
      override def waitForSingle(how: How, locator: String): SingleLocatingStep = {
        val step: SingleLocatingStep = SingleLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          Wait.until(Conditions.JSoupElementIsPresent(elementFinder, how, step.getLocatorString))
        step.setRunnable(runnable)
        step
      }

      override def waitForMultiple(how: How, locator: String): MultipleLocatingStep = {
        val step: MultipleLocatingStep = MultipleLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          Wait.until(Conditions.JSoupElementsArePresent(elementFinder, how, step.getLocatorString))
        step.setRunnable(runnable)
        step
      }

      override def captureSingle(how: How, locator: String): OptionalLocatingStep = {
        val step: OptionalLocatingStep = OptionalLocatingStep(how, locator)
        val runnable = (elementFinder: DomElementLocator) =>
          elementFinder.findElement(ParserEngine.JSOUP, how, step.getLocatorString)
        step.setRunnable(runnable)
        step
      }

      override def captureMultiple(how: How, locator: String) = ???
    }

  object Clickable extends CaptureScheme {
    override def waitForSingle(how: How, locator: String): SingleLocatingStep = {
      val step: SingleLocatingStep = SingleLocatingStep(how, locator)
      val runnable = (elementFinder: DomElementLocator) => {
        val element = Wait.until(Conditions.webElementIsPresent(elementFinder, how, step.getLocatorString))
        Wait.until(Conditions.elementIsEnabled(element))
        element
      }
      step.setRunnable(runnable)
      step
    }

    override def waitForMultiple(how: How, locator: String): MultipleLocatingStep = ???

    override def captureSingle(how: How, locator: String): OptionalLocatingStep = ???

    override def captureMultiple(how: How, locator: String): MultipleLocatingStep = ???
  }

}
