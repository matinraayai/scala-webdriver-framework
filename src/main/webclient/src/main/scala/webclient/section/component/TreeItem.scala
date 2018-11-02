package webclient.section.component

import java.time.Duration

import tools.infrastructure.browser.Session
import tools.component.Section
import tools.infrastructure.element.DOMElement
import tools.capture.CaptureSchemes.{JSoupPresence, WebDriverPresence, WebDriverVisibility}
import tools.capture.SingleLocatingProcedure
import tools.infrastructure.enums.{How, ParserEngine}
import java.util.function

import tools.infrastructure.wait.{Conditions, Wait}

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashMap
import scala.collection.mutable

//TODO: Think about methods to add.
//TODO: Add Documentation
/**
  * Represents an Item of a tree inside the Webclient. This can be an ontology item, or a query info.
  * @param session Session object responsible for extracting info off the section.
  * @param IDAttribute ID of the tree element on the DOM.
  * @author Matin Raayai Ardakani
  */
case class TreeItem(protected val session: Session,
                    private val IDAttribute: String) extends Section {

  protected val parentElementProcedure: SingleLocatingProcedure =
    JSoupPresence  .waitForSingle(How.ID, IDAttribute)

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "item_information_table" -> JSoupPresence .waitForSingle (How.TAG_NAME, "table"),
    "children_wrapper" -> JSoupPresence .waitForSingle (How.CLASS_NAME, "ygtvchildren"),

  )
  SINGLE_ELEMENT_LOCATOR_MAP ++= TrieMap(
    "expanded_expand_button" -> "item_information_table" .--> (WebDriverVisibility   .waitForSingle (How.CLASS_NAME, "ygtvlm")),
    "item_content" -> "item_information_table" .--> (WebDriverVisibility .waitForSingle(How.CLASS_NAME, "ygtvcontent"))
  )



  override protected lazy val OPTIONAL_ELEMENT_LOCATOR_MAP: OMap = TrieMap(
    "expand_button" -> JSoupPresence .captureSingle (How.CLASS_NAME, "ygtvspacer")
  )

  //Checks whether the
  if(captureModelDomainElement.className != "ygtvitem")
    throw new IllegalArgumentException("Invalid Element.\n")


  /**
    * @return the parent element of this item, if: <ul>
    *           <li> Parent element exists on the DOM.
    *           <li> Parent element has a table element present under it (i.e. it has information).
    * </ul>
    */
  def parentAsOptionalTreeItem: Option[TreeItem] = {
    val optionalChildWrapperElement = captureModelDomainElement.parent
    if(optionalChildWrapperElement.isDefined)
      if(optionalChildWrapperElement.get.hasClass("ygtvchildren")) {
        val parent = optionalChildWrapperElement.get.parent.get
        if(parent.child(0).get.hasClass("ygtvtable"))
          Option(TreeItem(session, parent.getIDAttribute))
        else None
      }
      else None
    else None
  }

  def rightClick(): Unit = "item_information_table".contextClick().perform()

  /**
    * @return Children of this TreeItem that are present in the DOM. An empty buffer if none are present.
    */
  def getCurrentlyLoadedChildren: mutable.Buffer[TreeItem] =
    "children_wrapper".children(ParserEngine.JSOUP).map(i => TreeItem(session, i.getIDAttribute))

  def waitUntilExpansionOperationIsComplete(): Unit = {
    Wait.until(Conditions.childrenAreLoaded("children_wrapper"))
  }

  def expandAndGetChildren: mutable.Buffer[TreeItem] = {
    changeExpansionState()
    waitUntilExpansionOperationIsComplete()
    getCurrentlyLoadedChildren
  }

  def expandAllChildren(): Unit = {
    if(!isLeaf)
      if(!isExpanded) {
        changeExpansionState()
        val children: mutable.Buffer[TreeItem] = {
          waitUntilExpansionOperationIsComplete()
          getCurrentlyLoadedChildren
        }

        for(child <- children) {
          child.expandAllChildren()
        }
      }
  }

  /**
    * @return True if an expansion button is found; False otherwise.
    */
  def isLeaf: Boolean = !"expand_button".isDefined

  /**
    * Changes the expansion state of the treeItem if this is not a leaf (An expansion button is found).
    * @throws IllegalArgumentException if this is a leaf (an expansion button is not found.)
    */
  def changeExpansionState(): Unit = {
    if(!isLeaf) {
      "item_information_table".click()
    }
    else {
      throw new IllegalStateException("Expansion button was not found.\n")
    }
  }

  def isExpanded: Boolean = {
    if(!isLeaf) {
      "expand_button".get.hasClass("ygtvtm")
    }
    else
      true
  }

  def isDisplayed: Boolean = captureModelDomainElement.isDisplayed

  def getText: String = "item_content".textFromJSoup

  override def toString: String = getText

  private[webclient] def getItem: DOMElement = "item_information_table".findElement(ParserEngine.JSOUP, How.CLASS_NAME, "ygtvcontent").get
}
