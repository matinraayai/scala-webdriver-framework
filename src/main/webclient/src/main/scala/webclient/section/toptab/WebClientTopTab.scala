package webclient.section.toptab
import tools.component.Section
import tools.capture.CaptureSchemes.JSoupPresence
import tools.infrastructure.enums.{How, ParserEngine}
import org.openqa.selenium.NoSuchElementException

import scala.collection.concurrent.TrieMap
import scala.collection.{immutable, mutable}
import scala.collection.immutable.HashMap

/**
  * Represents common functionality between top tabs in Webclient.
  */
//TODO: Add documentation.
private[webclient] trait WebClientTopTab extends Section {

  override protected lazy val SINGLE_ELEMENT_LOCATOR_MAP: SMap = TrieMap(
    "tool_icon_wrapper" -> JSoupPresence .waitForSingle (How.CLASS_NAME, "opXML")
  )

  protected def clickOnIcon(iconNumber: Int): Unit = {
    val elementAsOption = "tool_icon_wrapper".child(iconNumber)
    if(elementAsOption.isDefined)
      elementAsOption.get.click()
    else
      throw new NoSuchElementException("%d-th icon was not found.\n".format(iconNumber))
  }

  /**
    * Switches the tab to the desired tabName
    * @param tabName Name of the desired tab.
    * @throws NoSuchElementException if the tab name was not found.
    */
  protected def changeTab(tabName: String): Unit = parentElementProcedure.run(session).
    findElement(ParserEngine.WEBDRIVER, How.LINK_TEXT, tabName).getOrElse(throw new NoSuchElementException(s"Unable to find tab: $tabName")).click()
}
