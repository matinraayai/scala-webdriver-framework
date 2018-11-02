package tools.capture

import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator

import scala.collection.mutable

case class OptionalLocatingPath(singleElementLocatingProcedure: Option[SingleLocatingProcedure], finalLocator: OptionalLocatingStep) {
  def setVars(stepNumber: Int, args: Any*) = {
    if(singleElementLocatingProcedure.isDefined) {
      if(stepNumber == singleElementLocatingProcedure.get.length) {
        finalLocator.setVars(args)
      }
      else
        singleElementLocatingProcedure.get.setVars(stepNumber, args)
    }
  }



  def run(firstStep: DomElementLocator): Option[DOMElement] = {
    if (singleElementLocatingProcedure.isEmpty) finalLocator.run(firstStep)
    else finalLocator.run(singleElementLocatingProcedure.get.run(firstStep))
  }
}
