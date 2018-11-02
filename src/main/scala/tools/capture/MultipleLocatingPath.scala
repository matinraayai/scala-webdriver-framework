package tools.capture
import tools.infrastructure.element.DOMElement
import tools.infrastructure.`trait`.DomElementLocator

import scala.collection.mutable

case class MultipleLocatingPath(singleElementLocatingProcedure: Option[SingleLocatingProcedure], finalLocator: MultipleLocatingStep) {

  def setVars(stepNumber: Int, args: Any*) = {
    if(singleElementLocatingProcedure.isDefined) {
      if(stepNumber == singleElementLocatingProcedure.get.length) {
        finalLocator.setVars(args)
      }
      else
        singleElementLocatingProcedure.get.setVars(stepNumber, args)
    }
  }

  def run(firstStep: DomElementLocator): mutable.Buffer[DOMElement] = {
    if (singleElementLocatingProcedure.isEmpty) finalLocator.run(firstStep)
    else finalLocator.run(singleElementLocatingProcedure.get.run(firstStep))
  }

}
