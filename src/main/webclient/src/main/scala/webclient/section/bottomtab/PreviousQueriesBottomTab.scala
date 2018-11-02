package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How
import webclient.section.component.TreeItem

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//TODO: Documentation
private[webclient] case class PreviousQueriesBottomTab(protected val session: Session,
                                    protected val parentElementProcedure: SingleLocatingProcedure) extends Section {
  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "list_of_queries_wrapper" -> JSoupPresence .waitForSingle (How.CLASS_NAME, "ygtvchildren")
  )

  override protected lazy val MULTIPLE_ELEMENT_LOCATOR_MAP: MMap = TrieMap(
    "list_of_queries" -> "list_of_queries_wrapper" .--> (JSoupPresence .waitForMultiple (How.CLASS_NAME, "ygtvitem"))
  )

  def getListOfItems: mutable.Buffer[TreeItem] =
    "list_of_queries".<>.map(i => TreeItem(session, i.getIDAttribute))

}
