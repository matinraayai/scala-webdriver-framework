package webclient.section.bottomtab

import tools.infrastructure.browser.Session
import tools.component.{Section, SelectDropDown}
import tools.capture.CaptureSchemes.{Clickable, JSoupPresence, WebDriverPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.{How, ParserEngine}
import tools.infrastructure.wait.{Conditions, Wait}
import webclient.section.component.QueryToolSinglePanel

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable


//TODO: Add more methods.
//TODO: Add documentation.
private[webclient] case class QueryToolBottomTab (protected val session: Session,
                                                  protected val parentElementProcedure: SingleLocatingProcedure) extends Section {
  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "query_name" -> WebDriverVisibility .waitForSingle (How.ID, "queryName"),
    "temporal_constraint_dropdown_button" -> WebDriverVisibility .waitForSingle (How.ID, "queryTiming"),
    "temporal_constraint_dropdown_list" -> WebDriverVisibility .waitForSingle (How.ID, "temporalConstraintBar") .--> (WebDriverVisibility .waitForSingle (How.CLASS_NAME, "yui-menu-button-menu")),
    "define_order_of_events_dropdown_button" -> WebDriverVisibility .waitForSingle (How.ID, "defineTemporal"),
    "define_order_of_events_dropdown_list" -> WebDriverVisibility .waitForSingle (How.ID, "defineTemporalBar") .--> (WebDriverVisibility .waitForSingle (How.CLASS_NAME, "yui-menu-button-menu")),
    "new_event_button" -> WebDriverVisibility .waitForSingle (How.ID, "addDefineGroup-button"),
    "remove_last_event_button" -> WebDriverVisibility .waitForSingle (How.ID, "addDefineGroup-button"),
    "query_panels_wrapper" -> WebDriverVisibility .waitForSingle (How.ID, "crc.innerQueryPanel"),
    "temporal_relations_wrapper" -> WebDriverVisibility .waitForSingle (How.ID, "crc.temoralBuilder"),
    "add_temporal_relationship_button" -> WebDriverVisibility .waitForSingle (How.LINK_TEXT, "Add Temporal Relationship"),
    "remove_last_temporal_relationship_button" -> WebDriverVisibility .waitForSingle (How.LINK_TEXT, "Remove Last Temporal Relationship"),
    "pagination_wrapper" -> WebDriverVisibility .waitForSingle (How.ID, "scrollBox"),
    "topic_dropdown" -> WebDriverVisibility .waitForSingle (How.ID, "queryTopicSelect"),
    "request_new_topic_button" -> WebDriverVisibility .waitForSingle (How.CLASS_NAME, "topicButton"),
    "run_query_button" -> Clickable .waitForSingle (How.ID, "runBoxText"),
    "clear_query_panel_button" -> WebDriverVisibility .waitForSingle (How.ID, "newBox"),
    "number_of_groups_text" -> WebDriverVisibility .waitForSingle (How.ID, "groupCount")
  )

  private val panels: Array[QueryToolSinglePanel] = {
    val locatingProcedures: Array[SingleLocatingProcedure] =
      Array("query_panels_wrapper" .--> (WebDriverVisibility .waitForSingle(How.CSS, "div:nth-child(1)[class ='qryPanel']")),
        "query_panels_wrapper" .--> (WebDriverVisibility .waitForSingle(How.CSS, "div:nth-child(2)[class ='qryPanel']")),
          "query_panels_wrapper" .--> (WebDriverVisibility .waitForSingle (How.CSS, "div:nth-child(3)[class ='qryPanel']")))
    locatingProcedures.map(QueryToolSinglePanel(session, _))
  }

  private val topicDropDown: SelectDropDown = SelectDropDown(session, "topic_dropdown")

  def getPanel(idx: Int): QueryToolSinglePanel = panels(idx)


  def getCurrentQueryName: String = "query_name".textFromJSoup

  def openTemporalConstraintDropDown(): Unit = "temporal_constraint_dropdown_button".click()

  def selectTreatAllGroupsIndependently(): Unit =
    "temporal_constraint_dropdown_list".findElement(ParserEngine.WEBDRIVER,
      How.LINK_TEXT, "Treat all groups independently").getOrElse(
      throw new NoSuchElementException("Unable to find option \'Treat all groups independently.\'")).click()

  def selectSameFinancialEncounter(): Unit = "temporal_constraint_dropdown_list".findElement(ParserEngine.WEBDRIVER,
    How.LINK_TEXT, "Selected groups occur in the same financial encounter").getOrElse(
    throw new NoSuchElementException("Unable to find option \'Same Financial Encounter.\'")).click()

  def selectDefineSequenceOfEvents(): Unit = "temporal_constraint_dropdown_list".findElement(ParserEngine.WEBDRIVER,
    How.LINK_TEXT, "Define sequence of Events").getOrElse(
    throw new NoSuchElementException("Unable to find option \'Define Sequence of Events.\'")).click()

  def openSequenceOfEventsDropDown(): Unit = "define_order_of_events_dropdown_button".click()

  def selectPopulationInWhichOccursInSequenceOfEventsDropDown(): Unit = ???

  def selectDefineOrderOfEventsInSequenceOfEventsDropDown(): Unit = ???

  def selectEventNumberInSequenceOfEventsDropDown(eventIdx: Int): Unit = ???

  def pressNewEventButton(): Unit = "new_event_button".click()

  def pressRemoveLastEventButton(): Unit = "remove_last_event_button".click()

  def pressAddNewTemporalRelationship(): Unit = "add_temporal_relationship_button".click()

  def pressRemoveLastTemporalRelationship(): Unit = "remove_last_temporal_relationship_button".click()

  def getAllAvailableUserTopics: mutable.Buffer[String] = topicDropDown.getAllSelectedOptions.map(_.getTextFromWebDriver)

  def selectTopicByIndex(index: Int): Unit = topicDropDown.selectByIndex(index)

  def selectTopicByField(value: String): Unit = topicDropDown.selectByValue(value)

  def pressRequestNewTopic(): Unit = "request_new_topic_button".click()

  def pressClear(): Unit = "clear_query_panel_button".click()

  def pressRun(): Unit = "run_query_button".click()

  def addNewGroup(): Unit = ???

  def navigateToFirstPageOfGroups(): Unit = ???

  def navigateToPreviousPageOfGroups(): Unit = ???

  def navigateToNextPageOfGroups(): Unit = ???

  def navigateToLastPageOfGroups(): Unit = ???

  def getNumberOfGroupsActive: Int = ???



}
