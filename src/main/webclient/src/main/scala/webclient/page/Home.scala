package webclient.page

import tools.infrastructure.browser.Session
import tools.component.Page
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverVisibility}
import tools.infrastructure.enums.How
import webclient.section.{Header, QueryRunDialogueBox}
import webclient.section.component._
import webclient.section.bottomtab.{Navigate_FindTermsBottomTab, PreviousQueriesBottomTab, QueryToolBottomTab, QueryViewerBottomTab}
import webclient.section.toptab.{Navigate_FindTermsTopTab, PreviousQueriesTopTab, QueryToolTopTab, QueryViewerTopTab}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * API for the home page of the WebClient. This object contains all the 5 main [[tools.component.Section]]s present in
  * the WebClient (Nav/Find Terms, Query Tool, Previous Queries, Query Viewer and the header of the
  * page). All of the sections are available to access from the test.
  * Any other functionality that is either too small to be included in a separate section (like the
  * divider bar) and dropping tree items into the query tool should be included here.
 *
  * @param session for manipulating the page.
  */
case class Home(protected val session: Session) extends Page {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "header" -> WebDriverVisibility .waitForSingle  (How.ID, "topBarTable"),
    "splitter_bar" -> WebDriverVisibility .waitForSingle  (How.ID, "main.splitter"),
    "navigate_terms_top_tab" -> JSoupPresence .waitForSingle  (How.ID, "ontTopTabs"),
    "navigate_terms_bottom_tab" -> JSoupPresence .waitForSingle  (How.ID, "ontMainDisp"),
    "query_tool_top_tab" -> JSoupPresence .waitForSingle  (How.CSS, "#crcStatusBox > div.TopTabs"),
    "query_tool_bottom_tab" -> WebDriverVisibility .waitForSingle  (How.ID, "crcQueryToolBox.bodyBox"),
    "query_viewer_top_tab" -> WebDriverVisibility .waitForSingle (How.ID, "crcStatusBox") .--> (WebDriverVisibility .waitForSingle (How.CLASS_NAME, "TopTabs")),
    "previous_queries_bottom_tab" -> JSoupPresence .waitForSingle  (How.ID, "crcHistoryData"),
    "previous_queries_top_tab" -> (JSoupPresence waitForSingle  (How.ID, "crcHistoryBox")) .--> (JSoupPresence waitForSingle  (How.CLASS_NAME, "TopTabs")),
    "query_run_dialogue_box" -> (WebDriverVisibility waitForSingle  (How.ID, "dialogQryRun_c")))
  waitUntilVitalElementsAreLoaded()

  override def waitUntilVitalElementsAreLoaded():Unit = waitUntilElementsAreLoaded("splitter_bar",
  "navigate_terms_top_tab", "navigate_terms_bottom_tab", "query_tool_top_tab", "query_tool_bottom_tab",
  "query_viewer_top_tab")

  lazy val headerSection: Header = Header(session, "header")

  lazy val previousQueriesTopTab: PreviousQueriesTopTab =
    PreviousQueriesTopTab(session, "previous_queries_bottom_tab")

  lazy val previousQueriesBottomTab: PreviousQueriesBottomTab =
    PreviousQueriesBottomTab(session, "previous_queries_bottom_tab")

  lazy val navigate_FindTermsBottomTab: Navigate_FindTermsBottomTab =
    Navigate_FindTermsBottomTab(session, "navigate_terms_bottom_tab")

  lazy val navigate_FindTermsTopTab: Navigate_FindTermsTopTab =
    Navigate_FindTermsTopTab(session, "navigate_terms_top_tab")

  lazy val queryToolBottomTab: QueryToolBottomTab = QueryToolBottomTab(session, "query_tool_bottom_tab")

  lazy val queryToolTopTab: QueryToolTopTab = QueryToolTopTab(session, "query_tool_top_tab")

  lazy val queryViewerTopTab: QueryViewerTopTab = QueryViewerTopTab(session, "query_viewer_top_tab")

  lazy val queryViewerBottomTab: QueryViewerBottomTab = QueryViewerBottomTab(session, "query_viewer_bottom_tab")

  /**
    * Drags a `TreeItem` into the specified Query Panel.
    * @param treeItem From either the Navigation tab or the previous queries tab. It is also
    *                 possible to attempt to drag a TreeItem from the Query Tool itself, but nothing
    *                 should happen.
    * @param toolNumber The number of query tool you intend to drop the TreeItem in, which is between
    *                   1 and 3. Any other number will result to an Exception.
    */
  def dragTreeItemToQueryTool(treeItem: TreeItem, toolNumber: Int): Unit = {
    session.dragAndDrop(treeItem.getItem, queryToolBottomTab.getPanel(toolNumber).getQueryPanelElement).perform()
  }

  //TODO: Get rid of either xOffset or yOffset.
  /**
    * Drags the Divider bar to the specified offset.
    * @param xOffset
    * @param yOffset
    */
  def dragAndDropDividerBar(xOffset: Int, yOffset: Int): Unit = {
    "splitter_bar".dragAndDropBy(xOffset, yOffset).perform()
  }


  def getRunQueryDialogueBox: QueryRunDialogueBox = QueryRunDialogueBox(session, "query_run_dialogue_box")
}
