package steward.page

import tools.component.Page
import tools.capture.CaptureSchemes.WebDriverVisibility
import tools.capture.SingleLocatingProcedure
import steward.component.{StewardHeaderSection, StewardTabSwitcher}
import tools.infrastructure.enums.How

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap

/**
  * Has all the basic functionality common between major Steward Page Instances; For example, the
  * header and the tab switcher in the current implementation of Steward is common in between
  * `Topics`, `History` and `Statistics` Page objects.
  */
trait StewardOutline extends Page {

  val tabSwitcher: StewardTabSwitcher = StewardTabSwitcher(session, "steward_navigation_sidebar")

  val stewardHeaderSection: StewardHeaderSection = StewardHeaderSection(session, "steward_home_header_navigation_bar")

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: TrieMap[String, SingleLocatingProcedure] = TrieMap(
    "steward_navigation_sidebar" -> (WebDriverVisibility waitForSingle (How.ID, "side-menu")),
    "steward_home_header_navigation_bar" -> (WebDriverVisibility waitForSingle (How.CSS, "ul[class='nav navbar-top-links navbar-right']")),
    "steward_header_logo" -> (WebDriverVisibility waitForSingle (How.CSS,"div.shrine-brand"))
  )
}
