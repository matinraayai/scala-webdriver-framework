package dashboard.component

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.component.table.AllJSoupTable
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How

import scala.collection.mutable

/**
  * A section representing the tables used in the Dashboard application.
  * The reason behind the weird constructor is presence of multiple tables on one page next to each
  * other and absence of any distinguishing attribute.
  * @param session for extracting info out of the table
  * @param parentWrapper the Wrapper that includes all the tables located next to each other.
  * @param tableIndex the index of the table in the DOM.
  */
case class DashboardStatusTable(protected val session: Session, parentWrapper: SingleLocatingProcedure, tableIndex: Int) extends Section {

  protected val parentElementProcedure: SingleLocatingProcedure = parentWrapper .--> (JSoupPresence .waitForSingle (How.CSS, s" table:nth-child($tableIndex)"))

  private val table: AllJSoupTable = AllJSoupTable(session, parentElementProcedure)

  def getHeaderRowAsStringBuffer(rowIdx: Int): mutable.Buffer[String] = table.getHeaderRow(rowIdx).map(_.toString)

  def getBodyRowAsStringBuffer(rowIdx: Int): mutable.Buffer[String] = table.getBodyRow(rowIdx).map(_.toString)

  def getFooterRowAsStringBuffer(rowIdx: Int): mutable.Buffer[String] = table.getFooterRow(rowIdx).map(_.toString)
}
