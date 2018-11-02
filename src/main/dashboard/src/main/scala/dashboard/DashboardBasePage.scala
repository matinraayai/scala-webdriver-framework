package dashboard

import dashboard.component.{DashboardHeader, DashboardTabSwitcher}
import tools.component.Page
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable


/**
  * Represents the common functionality between all Dashboard pages, which includes the header section
  * and the Tab Switcher Section Objects.
  * @author Matin Ardakani
  */
trait DashboardBasePage extends Page {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "tab_selector_panel" -> WebDriverVisibility  .waitForSingle (How.CSS, "div[class='list-group panel']"),
    "header_wrapper" -> WebDriverVisibility   .waitForSingle (How.CSS, "nav class='navbar navbar-default navbar-static-top shrine-navbar'"),
    "table_wrapper" -> WebDriverVisibility .waitForSingle (How.ID, "page-wrapper"))

  override def waitUntilVitalElementsAreLoaded(): Unit = waitUntilElementsAreLoaded(
    "tab_selector_panel", "table_wrapper")

  val headerSection: DashboardHeader = DashboardHeader(session, "header_wrapper")

  final val tabSwitcher: DashboardTabSwitcher = DashboardTabSwitcher(session,"dashboard_tab_selector_panel")

}