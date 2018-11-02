package webclient.section.bottomtab

import java.util.concurrent.ConcurrentHashMap

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.How
import webclient.section.component.TreeItem

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

/**
  * Navigation Terms tab in the Web Client.
  * @param tabElementCapture for Capturing the outer element.
  * @param ontologyParentElementCapture for ontology.
  */
private[webclient] case class NavTermsTab (protected val session: Session,
                                           protected val parentElementProcedure: SingleLocatingProcedure) extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap("ontology_wrapper" -> JSoupPresence .waitForSingle (How.CLASS_NAME, "ygtvitem"))
  SINGLE_ELEMENT_LOCATOR_MAP += ("parent_item" -> JSoupPresence  .waitForSingle  (How.ID, "ygtv4"))



  private lazy val ontology: TreeItem = TreeItem(session, "parent_item".getIDAttribute)

  def getTreeOfOntology: TreeItem = ontology

  def isTabDisplayed: Boolean = captureModelDomainElement.isDisplayed
}
