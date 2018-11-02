package tools.component.table

import tools.component.Section
import tools.infrastructure.element.DOMElement

import scala.collection.mutable


/**
  * A simple abstraction over tables in web applications. Note that this abstraction is not
  * supposed to and will not satisfy all expectations from all webTables and it is meant to decouple
  * the ugly process of instantiating and extracting general information from the table.
  * More specifically, it only lets the user access the three general parts of a web table
  * (header, footer and body) and extracts them as single element. It will not expose <td> elements
  * or won't get a single row <tr> from different parts of the table due to different table designs
  * and range check complexity. If there is a need for detailed information from the table
  * (which is almost always the case), first create a concrete implementation of this trait and
  * then create all the extra functionality in another class that has the concrete implementation of
  * this trait (Composition pattern).
  * */
trait Table extends Section {

  def headerElement: DOMElement

  def bodyElement: DOMElement

  def footerElement: DOMElement

  def getHeaderRowCount: Int

  def getBodyRowCount: Int

  def getFooterRowCount: Int

  def getHeaderRow(rowIdx: Int): mutable.Buffer[DOMElement]

  def getBodyRow(rowIdx: Int): mutable.Buffer[DOMElement]

  def getFooterRow(rowIdx: Int): mutable.Buffer[DOMElement]
}
