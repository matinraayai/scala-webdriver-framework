package tools.infrastructure.enums

import tools.infrastructure.enums

import scala.collection.mutable

object ParserEngine extends Enumeration {
  type ParserEngine = Value
  val WEBDRIVER, JSOUP = Value

  def getPriorityList: mutable.Buffer[ParserEngine] = mutable.Buffer(WEBDRIVER, JSOUP)
}
